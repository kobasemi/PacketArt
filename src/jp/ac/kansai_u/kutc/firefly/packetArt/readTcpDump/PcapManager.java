package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;//File.existsに必要
import java.util.List;

import org.jnetpcap.Pcap;//こいつが心臓
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.PcapDLT;
import org.jnetpcap.PcapBpfProgram;

import org.jnetpcap.PcapClosedException;

/*
    TODO: 残りパケットの（概算値）の保持{
        ファイルサイズ(Byte)を標準MTU,1500byteで割る。WIDEプロジェクトの
        パケットはMTU=1500が最頻値なので、逐次ロードでもおおよそは出せるはずだ
    }
*/

/**
 * 完全にパケットアート用。<br>
 * シングルトンになったので、<br>
 * close時にnullを代入してください。<br>
 * 使い方:<br>
 * <br>
 * //他のクラスで<br>
 * PcapManager pm = PcapManager.getInstance();<br>
 * pm.addHandler(handler);<br>
 * pm.removeHandler(handler);<br>
 * //handler はhandlersの中の~Handlerをimplementsしたものです。<br>
 * <br>
 * その他アクセスできる変数はget??という<br>
 * メソッドを参照してください。<br>
 * 正常にパケットを読めるか確認するには<br>
 * isReadyRunを使ってください。<br>
 *
 * プロトコルに関係なくハンドラ以外で生パケットを<br>
 * 取得するにはnextPacket系関数を使ってください。<br>
 *
 * @author sya-ke
*/
public final class PcapManager extends Thread{
    private static final PcapManager instance = new PcapManager();
    /**
     * @return シングルトンのインスタンスを返します。
    */
    public static  synchronized PcapManager getInstance(){
        System.out.println("PcapManager.getInstance()");
        return instance;
    }

    private volatile boolean killThis;
    private PcapPacket pkt;
    private static StringBuilder errBuf;//libpcapからのエラーをここに
    private File pcapFile;
    private PcapIf pcapDev;
    private boolean fromFile;
    private boolean fromDev;
    private boolean readyRun;
    private boolean filtered;
    private Pcap pcap;//jnetpcapの核。
    private PcapBpfProgram bpfFilter;
    private String bpfText;

    private HandlerHolder handlerHolder;
    private PacketQueue packetQueue;

    public final int TIMEOUT_OPENDEV = 10;//デバイスからの読み込みで、0.01秒のパケット待ちを許す。
    public final int QUEUE_SIZE = 30000;//30000パケットの保持をする。MAXでだいたい3MBくらいメモリを食う。
    public final int PUSH_SIZE = 1;//1パケットのロードにつき2パケットの確保をする。0.01秒ごとにパケットを間引く意味もある。

    private Object openPcapLock = new Object();
    private Object filterLock = new Object();

    /**
     * 空のコンストラクタです。このコンストラクタを<br>
     * 使う場合は、オブジェクト生成後にopenDev(name)<br>
     * もしくはopenFile(name)をしないとパケットが読めません。
    */
    private PcapManager() {
        init();
    }
    //このコンストラクタはfinalで一度しか呼べないのでスレッドセーフ

    /**
     * コンストラクタで最初に呼ばれます。<br>
     * ただの初期化関数です。<br>
     * 何のエラーも引数も返り値もありません。
    */
    public void init() {
        File pcapFile = null;
        PcapIf pcapDev = null;
        pcap = null;
        fromFile = false;
        fromDev = false;
        readyRun = false;
        packetQueue = new PacketQueue(QUEUE_SIZE);
        filtered = false;
        errBuf = new StringBuilder();
        handlerHolder = new HandlerHolder();
        killThis = false;
        bpfText = null;
        //debugMe("PcapManager.initDone()");
    }
    //この関数はfinalで一度しか呼べないのでスレッドセーフ

    /**
     * シングルトンでスレッドが無限ループします。<br>
     * PcapManagerは空中のグローバル空間を漂いパケットを拾い、蓄え続けます。<br>
     * PcapManagerは常にパケットを拾い続けつつ一部を捨て、libpcapが浪費する待ちパケットのメモリを開放します。<br>
     * PcapManagerはパケットが欲しいというオブジェクトに惜しみなくパケットを譲ります。<br>
     * PcapManagerはパケットをプロトコルの種類別に譲ります。<br>
    */
    public void run() {
        debugMe("pm.run");
        //debugMe("PcapManager.run() start");
        killThis = false;
        while (killThis == false) {
            while (readyRun == false || pcap == null){
                if (isKilled()) {
                    return;
                    //スレッドの実行を停止します。
                }
            }
            pkt = null;
            pkt = nextPacket();//0.01秒間パケットが来なかったらタイムアウトします。
            //パケットが来なかった場合、pktにはnullが入ります。
            savePacket(pkt);
            //キューは古いパケットを捨てていくので、この関数を空撃ちしてパケットを間引けます。
            if (pkt != null ) {
                handlerHolder.inspect(pkt);//そのパケットをプロトコルハンドラへ渡し、ハンドラを実行します。
            } else {
      //          pkt = nextPacketFromQueue();
        //        if (pkt !=null) {
          //          handlerHolder.inspect(pkt);
            //    } else {
                    //パケットが無くなった場合、OnNoPacketsLeftハンドラを呼び出します。
                    //その場合、おそらく0.01秒ごとに呼び出されます。
                    if (fromFile)
                        handlerHolder.onNoPacketsLeft();
            //    }
            }
        }
    }
    //Threadのstart関数によってのみ呼び出されるので、スレッドセーフ（能動的に呼ばないで・・）

    /**
     * ハンドラを追加し、実行の登録をします。<br>
     * 追加するハンドラはなんらかのhandlerを<br>
     * implementsしていなければなりません。
     * 
     * @param o ハンドラをimplementsしたオブジェクト。
     * @return oがハンドラじゃなかった場合、falseが返ります。
    */
    public synchronized boolean addHandler(Object o) {
        boolean wasOK = false;
         wasOK = handlerHolder.classify(o);
        return wasOK;
    }

    /**
     * ハンドラを削除し、実行の登録解除をします。<br>
     * オブジェクトの参照を比較しているので、登録したものと<br>
     * 全く同一のハンドラが引数でなければなりません。<br>
     * 
     * @param o ハンドラをimplementsしたオブジェクト。
     * @return oというハンドラが登録されていなかった場合、falseを返します。
    */
    public synchronized boolean removeHandler(Object o) {
        return handlerHolder.removeHandler(o);
    }

    /**
     * Fileがコンストラクタの引数の場合に呼ばれる関数です。<br>
     * ファイルの有無チェックはそちらでやってください。<br>
     * この関数はエラーを出しません。エラー内容はgetErrカンスで取得可能です。
     *
     * @param fname ファイル名を文字列で。フルパスでお願いします。
     * @return 成功ならtrueを返します。失敗ならerrBufにエラーが入ってます。
    */
    public synchronized boolean openFile(String fname) {
        synchronized(openPcapLock) {
            if (pcap != null) {
                System.out.println("You've already opened pcap! closing previous one..");
                close();
            }
            pcapFile = new File(fname);
            //if (pcapFile.exists() == false) {
            //    readyRun = false;
            //}
            pcap = Pcap.openOffline(fname,errBuf);
            if (pcap == null) {
                System.err.println("Error while opening a file for capture: "
                    + errBuf.toString());
                return false;
            }
            fromDev = false;
            fromFile = true;
            filtered = false;
            readyRun = true;
            handlerHolder.onPcapOpened();
            debugMe("PcapManager.openFile(" + fname +") Done");
            setBPFfilter(bpfText);
            return true;
        }
    }

    /**
     * デバイス名を指定せずにこの関数を実行した場合、<br>
     * jnetpcapによって最適デバイスを選択されます。<br>
     * Jnetpcapが意味不明なデバイスを取ってくるので、<br>
     * #####まるで役に立ちません#####<br>
     * この関数はエラーを出しません。<br>
     * エラー内容はgetErr関数で取得可能です。
     * 
     * @return 成功ならtrueを返します。失敗ならerrBufにエラーが入ってます。
    */
    public synchronized boolean openDev() {
        String devName = Pcap.lookupDev(errBuf);
        //}
        return openDev(devName);
    }
    //呼び出し先がスレッドセーフなので、スレッドセーフ。

    /**
     * デバイス名がコンストラクタの引数の場合に<br>
     * 呼ばれます。よってこの関数はエラーを出しません。<br>
     * エラー内容はgetErr関数で取得可能です。
     *
     * @return 成功ならtrueを返します。失敗ならerrBufにエラーが入ってます。
    */
    public synchronized boolean openDev(String devName) {
        synchronized(openPcapLock) {
            System.out.println("PcapManager.openDev(" + devName +")");
            //pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.MODE_PROMISCUOUS, Pcap.DEFAULT_TIMEOUT, errBuf);
            if (pcap != null) {
                System.out.println("You've already opened pcap! closing previous one..");
                close();
            }
            pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.MODE_PROMISCUOUS, TIMEOUT_OPENDEV, errBuf);
            if (pcap == null) {
                System.err.println("Error while opening device for capture: "
                    + errBuf.toString());
                return false;
            }
            fromDev = true;
            fromFile = false;
            filtered = false;
            readyRun = true;
            pcapDev = (new DevUtil()).getDevByName(devName);
            handlerHolder.onPcapOpened();
            debugMe("PcapManager.openDev(" + devName +") Done");
            setBPFfilter(bpfText);
            return true;
        }
    }
    //まるごとスレッドセーフ

    /**
     * 現在開いているtcpdumpのファイルオブジェクトを返します。
     * ファイル名はフルパスです。
     *
     * @return Fileオブジェクトです。ファイルからパケットを読んでいない場合はnullが返ります。
    */
    public File getPcapFile() {
        return pcapFile;
    }

    /**
     * 現在開いているデバイス(PcapIF)オブジェクトを返します。
     *
     * @return PcapIFオブジェクトです。デバイスを開いてなければnullを返します。
    */
    public PcapIf getPcapDev() {
        return pcapDev;
    }

    /**
     * 現在「ファイルから」パケットを読み込んでいるか、否かを返します。<br>
     * 性質上、isFromDevとは裏表の関係になりがちです。
     *
     * @return ファイルからパケットを読み込んでいるならばtrueを返します。
    */
    public boolean isFromFile() {
        return fromFile;
    } 

    /**
     * 現在「デバイスから」パケットを読み込んでいるか、否かを返します。<br>
     * 性質上、isFromFileとは裏表の関係になりがちです。。
     *
     * @return デバイスからパケットを読み込んでいるならばtrueを返します。
    */
    public boolean isFromDev() {
        return fromDev;
    } 

    /**
     * 現在パケットを読み出せる状態か、否かを返します。<br>
     * ファイルが閉じている、「非常食」のキューが空の場合はfalseが返ります。
     *
     * @return パケットを読める状態ならばtrueを返します。falseならばopenFile,openDevをしましょう。
    */
    public boolean isReadyRun() {
        return readyRun;
    } 

    /**
     * 現在スレッドで動いている状態か、否かを返します。<br>
     *
     * @return スレッドで動いているならばtrueを返します。falseならば(new Thread(pm)).start()をしてください。
    */
    public boolean isKilled() {
        return killThis;
    } 

    /**
     * スレッドを停止させます。
    */
    public void kill() {
        killThis = true;
    }

    /**
     * これまでlibpcapで発生した全てのエラーを返します。
     *
     * @return 現在保持しているエラー情報をすべて返します。
    */
    public String getAllErr() {
        return errBuf.toString();
    }

    /**
     * 最も最近発生したlibpcapのエラーを返します。
     *
     * @return libpcapに関する最新のエラー情報だけを返します。
    */
    public String getErr() {
        if (pcap != null) {
            return pcap.getErr();
        } else {
            return getAllErr();
        }
    }

    /**
     * フィルターが有効か否かを返します。
     *
     * @return BPFフィルターが適用されているならtrueを返します。
    */
    public boolean isFiltered() {
        return filtered;
    }

    /**
     * 一個ずつロードします。packetのメモリはlibpcapのメモリを共有しています。<br>
     * こいつに関する参照を無くすとlibpcapのメモリもFreeされます。<br>
     * メモリのアロケート処理が入らないので、高速です。
     *
     * @return パケット、というかlibpcapの保持するパケットへのポインタを返します。エラーならnull。
    */
    public synchronized PcapPacket nextPacket() {
        PcapPacket packet = new PcapPacket(JMemory.POINTER);
        try {
            if ( pcap != null ) {
                synchronized(pcap) {
                    if ( pcap.nextEx(packet) == Pcap.NEXT_EX_OK ) {
                        return packet;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } catch (PcapClosedException e) {
            System.out.println("GIVE ME MORE PCAP FILES!!!!");
            handlerHolder.onPcapClosed();
            //ここでユーザにファイルのパケットをすべて消費したことを告げる。
            pcap = null;
            readyRun = false;
            fromFile = false;
            return null;
        }
    }

    /**
     * 一個ずつロードします。packetはのメモリはJavaで管理されます。<br>
     * libpcapの保持するパケットはすぐに解放され、その代わりにJavaのメモリを食います<br>
     * 参照の複雑化は起こりませんが、メモリのアロケート処理が入るので、低速です。
     *
     * @return パケットを返します。libpcapの方はすぐに解放されます。エラーならnull。
    */
    public synchronized PcapPacket nextPacketCopied() {
        PcapPacket pkt = nextPacket();
        if (pkt != null) {
            return new PcapPacket(pkt);
        }
        return null;
    }

    /**
     * Javaのメモリ上にある、「非常食」、デバイスまたはファイルから<br>
     * パケットを読み込み、同時にキューに高速に蓄えます。
     *
     * @param howManyPackets どのくらいの数のパケットを読み込むかという数値です。 
     * @return 成功ならtrueを返します。
    */
    public boolean savePackets(int howManyPackets) {
        final int DLT;
        final Object dummy = null;
        final int howManyEnqeued;

        if (pcap == null) {
            return false;
        }

        synchronized(pcap) {
            DLT = pcap.datalink();
            howManyEnqeued = pcap.dispatch(howManyPackets, DLT, packetQueue, dummy);
        }
        if (howManyEnqeued < 0) {
            return false;//ループ中にブレークループシグナルを受け取った。
        }
        return true;
    }

    /**
     * Javaのメモリ上にある、「非常食」へパケットを蓄えます。<br>
     * nullなら捨てられます。
     *
     * @param pkt 突っ込むパケットです。
     * @return 成功ならtrueを返します。
    */
    public void savePacket(PcapPacket pkt) {
        packetQueue.add(pkt);
    }

    /**
     * 「非常食」のパケットを複数個読み込み、配列もしくで返します。<br>
     * まとまったパケットを不定期に欲しい人にとっては嬉しい関数です。<br>
     * ただし、非常食が足りなかった場合、残り0になった時点でのパケットを返します。
     *
     * @param howManyPackets 何個のパケットを持ってくるかをという数値です。Listの配列数です。
     * @return 実際のリストが返ってきます。非常食が無かった場合は空のリストが返ります。
    */
    public List<PcapPacket> restorePackets(int howManyPackets) {
        return packetQueue.poll(howManyPackets);
    }

    /**
     * 「非常食」の残りパケット数を返します。
     *
     * @return キューに保持している残りパケット数を返します。
    */
    public int packetsLeftIs() {
        return packetQueue.size();
    }

    /**
     * 「非常食」からパケットを一つ取り出します。
     *
     * @return PcapPacketを返します。非常食が空の場合はnullが返ります。
    */
    public PcapPacket nextPacketFromQueue() {
        return packetQueue.poll();
    }

    /*TODO:
    public float nokoriPacket() {
        int MTU = 1500;
        int FILESIZE = File.getSize();
        int howManyPackets = FILESIZE/MTU;
        float ret = count / howManyPackets;
    }
    */

    /**
     * BPFという記法で取得するパケットを意図的に制御します。
     *
     * @param bpf BPF構文で書かれたフィルタリングの記号文字列です。
     * @return 成功ならtrue、エラーならfalse。getErr()でエラー内容は見れます。
    */
    public boolean setBPFfilter(String bpf) {
        if (bpf == null) {
            errBuf.append("BPF You Entered is NULL");
            return false;
        }
        synchronized(filterLock) {
            if (isFiltered()) {
                Pcap.freecode(bpfFilter);
                filtered = false;
                System.out.println("PcapManager.setBPFfilter(), removing previous BPF !");
            }
            final int OPTIMIZE = 1;//立てといた方がいいんでしょ？多分。
            final int NETMASK = 0;//今回はWANのお話なので。。。
            final int DLT = PcapDLT.CONST_EN10MB;//イーサネット２。
            PcapBpfProgram filter = new PcapBpfProgram();//BPFポインタを取得

            int retCode;
            if (readyRun && pcap != null) { //オープン済み
                retCode = pcap.compile(filter, bpf, OPTIMIZE, NETMASK);
            } else { //未オープン。snaplenとDLTの指定が必要。
                retCode = Pcap.compileNoPcap(Pcap.DEFAULT_SNAPLEN,
                                    DLT,
                                    filter,
                                    bpf,
                                    OPTIMIZE,
                                    NETMASK);
            }
            if ( retCode == Pcap.NOT_OK ) { // 多分、String bpfがBPFの構文にあってない
                System.out.println("PcapManager.setBPF('"+ bpf +"') Failed!");
                if (pcap != null) {
                    System.out.println("ERROR is " + pcap.getErr());
                }
                return false;
            }
            if ( pcap != null && pcap.setFilter(filter) == Pcap.NOT_OK){
                System.out.println("PcapManager.setBPF('"+ bpf +"') Failed!");
                if (pcap != null) {
                    System.out.println("ERROR is " + pcap.getErr());
                }
                return false;
            }
            bpfFilter = filter;//解放用ポインタの保持。
            bpfText = bpf;
            filtered = true;
            System.out.println("PcapManager.setBPF('"+ bpf +"') Success!");
        }
        return true;
    }

    /**
     * アロケートしたメモリや開いたファイルをガベコレの前に閉じます。<br>
     * この関数では自分（シングルトンinstance）を解放しません。<br>
    */
    public synchronized void close() {
        if ( pcap != null && fromFile == true) {
            pcap.close();
        }
        if ( bpfFilter != null ) {
            Pcap.freecode(bpfFilter);
            bpfText = null;
        }
        pcap = null;
        readyRun = false;
        fromFile = false;
        fromDev = false;
        filtered = false;
    }

    /**
     * デバッグ用。<br>
    */
    public void debugMe(String header) {
        System.out.println("----------------------------------" + header);
        System.out.print("PcapManager Thread is ");
        if (this.isAlive()) {
            System.out.println("Alive");
        } else {
            System.out.println("Dead");
        }
        System.out.println("File pcapFile = " + pcapFile);
        System.out.println("PcapIf pcapDev = " + pcapDev);
        System.out.println("pcap = " + pcap);
        System.out.println("fromFile = " + fromFile);
        System.out.println("fromDev = " + fromDev);
        System.out.println("readyRun = " + readyRun);
        System.out.println("packetQueue.size = " + packetQueue.size());
        System.out.println("filtered = " + filtered);
        System.out.println("errBuf = " + errBuf.toString());
        System.out.println("killThis = " + killThis);
        System.out.println("----------------------------------");
    }
}
