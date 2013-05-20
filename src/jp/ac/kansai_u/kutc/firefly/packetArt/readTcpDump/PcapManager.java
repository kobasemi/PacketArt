package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;
import java.util.List;

import org.jnetpcap.Pcap;//こいつが心臓
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.PcapDLT;
import org.jnetpcap.PcapBpfProgram;

import org.jnetpcap.PcapClosedException;


/**
 * PcapManagerはjnetpcapを使い、パケットの読み込みと<br>
 * パケットの保持、パケットハンドラの搭載、<br>
 * ハンドラを含む他のクラスパケットの受け渡し、<br>
 * BPFフィルタの保持、デバイス情報の取得と提供を<br>
 * シングルトンのスレッドで提供しますので、初めにstart()関数を呼び出してください。<br>
 * このスレッドは一定の保持キューサイズを超えた時点で<br>
 * 最も古いパケットから順に捨てていきます。<br>
 * すべての完全にリアルタイムなパケットを使用する場合は<br>
 * パケットハンドラを使用してください。<br>
 * その他の場合はnextPacketFromQueue()関数が確実です。<br>
 *
 * jnetpcapの仕様に沿って、このクラスは一切例外を投げません。<br>
 *
 * 使い方:<br>
 * <br>
 * //他のクラスで、handlerをTcpHandler.javaをimplementしたものとすると<br>
 * PcapManager pm = PcapManager.getInstance();<br>
 * pm.start();
 * pm.openFile(....);pm.openDev(...);
 * pm.addHandler(handler);<br>
 * ~~~~~~この間のTCPパケットは自動的に到着次第ハンドラに渡される
 * pm.removeHandler(handler);<br>
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
    private static final PcapManager instance = new PcapManager();//シングルトン！
    /**
     * @return シングルトンのインスタンスを返します。
    */
    public static  synchronized PcapManager getInstance(){
        return instance;
    }

    private volatile boolean killThis;//スレッド停止フラグ（安全にスレッドを停止する）
    private PcapPacket pkt;//一個しか保持しません。run関数でしか使ってません。
    private final static StringBuilder errBuf = new StringBuilder();//libpcapからのエラーをここに格納します。
    private File pcapFile;//オープンしているファイルの情報を保持します。
    private PcapIf pcapDev;//オープンしているデバイスの情報を保持します。
    private boolean fromFile;//ファイルからオープン？
    private boolean fromDev;//デバイスからオープン？
    private boolean readyRun;//パケットが読み込める？＞
    private boolean filtered;//BPFフィルタが有効になっている？
    private Pcap pcap;//jnetpcapの核。
    private PcapBpfProgram bpfFilter;//BPFフィルタへのポインタ
    private String bpfText;//BPFフィルタのテキスト

    public final static int TIMEOUT_OPENDEV = 10;//デバイスからの読み込みで、0.01秒のパケット待ちを許す。
    public final static int QUEUE_SIZE = 30000;//30000パケットの保持をする。MAXでだいたい3MBくらいメモリを食う。

    private final PacketQueue packetQueue = new PacketQueue(QUEUE_SIZE);//別ファイルを参照のこと
    private final HandlerHolder handlerHolder = new HandlerHolder();//別ファイルを参照のこと

    private Object openPcapLock = new Object();//ロック用オブジェクト
    private Object filterLock = new Object();//ロック用オブジェクト

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
        filtered = false;
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
     * このスレッドを止めるにはkill関数を呼び出してください。
    */
    public void run() {
        debugMe("pm.run");
        killThis = false;//pcapManager.start()でスレッドを開始させる
        while (!isKilled()) {//pcapManager.kill()呼び出されたらループを抜ける
            while (readyRun == false || pcap == null){//読むパケットが無い。
                if (isKilled()) {
                    return;//ループを抜けます。
                    //スレッドの実行を停止します。
                }
            }
            pkt = null;//前回のパケットのポインタを破棄します。
            pkt = nextPacket();//0.01秒間パケットが来なかったらタイムアウトします。
            //パケットが来なかった場合、pktにはnullが入ります。
            savePacket(pkt);//nullは捨てられます。
            //キューは古いパケットを捨てていくので、この関数を空撃ちしてパケットを間引けます。
            if (pkt != null ) {
                handlerHolder.inspect(pkt);//そのパケットをプロトコルハンドラへ渡し、ハンドラを実行します。
            } else {
                if (fromFile) {//デバイスからの場合、到着パケットは未知。
                    handlerHolder.onNoPacketsLeft();
                    //パケットが無くなった場合、OnNoPacketsLeftハンドラを呼び出します。
                    //おそらく0.01秒ごとに呼び出されます。
                }
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
    public synchronized boolean addHandler(final Object o) {
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
    public synchronized boolean removeHandler(final Object o) {
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
    public synchronized boolean openFile(final String fname) {
        synchronized(openPcapLock) {
            if (pcap != null) {
                System.out.println("You've already opened pcap! closing previous one..");
                close();//前回のファイルのロックを切ります。
            }
            pcap = Pcap.openOffline(fname,errBuf);
            if (pcap == null) {
                System.err.println("Error while opening a file for capture: "
                    + errBuf.toString());
                return false;
            }
            pcapFile = new File(fname);
            fromDev = false;
            fromFile = true;
            filtered = false;
            readyRun = true;
            handlerHolder.onPcapOpened();
            debugMe("PcapManager.openFile(" + fname +") Done");
            setBPFfilter(bpfText);//再度フィルタをセットします。
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
        final String devName = Pcap.lookupDev(errBuf);
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
    public synchronized boolean openDev(final String devName) {
        synchronized(openPcapLock) {
            System.out.println("PcapManager.openDev(" + devName +")");
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

    /**
     * 現在開いているtcpdumpのファイルオブジェクトを返します。<br>
     * ファイル名はフルパスです。ファイル名を取得したい場合に使ってください。
     *
     * @return Fileオブジェクトです。ファイルからパケットを読んでいない場合はnullが返ります。
    */
    public File getPcapFile() {
        return pcapFile;
    }

    /**
     * 現在開いているデバイス(PcapIF)オブジェクトを返します。<br>
     * 開いているデバイス名を取得したい場合、DevUtilと組み合わせて使ってください。
     *
     * @return PcapIFオブジェクトです。デバイスを開いてなければnullを返します。
    */
    public PcapIf getPcapDev() {
        return pcapDev;
    }

    /**
     * 現在設定されているBPFフィルタを返します。
     *
     * @return BPFフィルタの構文
    */
    public String getBpfText() {
        return bpfText;
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
        final PcapPacket packet = new PcapPacket(JMemory.POINTER);//ポインタを作成
        try {
            if ( pcap != null ) {
                synchronized(pcap) {
                    if ( pcap.nextEx(packet) == Pcap.NEXT_EX_OK ) {//ポインタにアロケート
                        return packet;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } catch (PcapClosedException e) {//呼ばれない時もあるが、問題ない。
            System.out.println("GIVE ME MORE PCAP FILES!!!!");
            handlerHolder.onPcapClosed();
            //一応呼び出してはいるが、OnNoPacketsLeftハンドラがあるから不要かも
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
        final PcapPacket pkt = nextPacket();
        if (pkt != null) {
            return new PcapPacket(pkt);//newすることで参照を断ち切る
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
            DLT = pcap.datalink();//オープンしているPcapからDLTを取得できる
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
    public void savePacket(final PcapPacket pkt) {
        if (pkt == null) {
            return;
        }
        packetQueue.add(new PcapPacket(pkt));
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
    public int getQueueLeft() {
        return packetQueue.size();
    }

    /**
     * 「非常食」の最大パケット数を返します。
     *
     * @return キューに保持できる最大パケット数を返します。
    */
    public int getQueueLimit() {
        return packetQueue.getMaxSize();
    }

    /**
     * 「非常食」の最大パケット数を設定します。
     *
     * @param size キューに保持させる最大パケット数です。
     * @return size負の数等無効な数値。場合、false。
    */
    public boolean setQueueLimit(int size) {
        return packetQueue.setLimit(size);
    }

    /**
     * 「非常食」からパケットを一つ取り出します。
     *
     * @return PcapPacketを返します。非常食が空の場合はnullが返ります。
    */
    public PcapPacket nextPacketFromQueue() {
        final PcapPacket pkt = packetQueue.poll();
        if (pkt == null && fromFile == true) {
            readyRun = false;
            //デバイスからロードの場合、まだ来る可能性がある。
            //よって、その時は常にreadyRun = true。
        }
        return pkt;
    }

    /*
    TODO: 動的にパケットの中央値平均を算術し、ファイルサイズから
          残りパケット数を予測するのもありかもしれない。
          キャプチャ方法が未定でMTU値も当てにならない以上、
          この関数の実装は断念する。

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
    public boolean setBPFfilter(final String bpf) {
        if (bpf == null) {
            errBuf.append("BPF You Entered is NULL");
            return false;
        }
        synchronized(filterLock) {
            if (isFiltered()) {//依然に設定したフィルタは削除
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
     * アロケートしたメモリや開いたファイルを閉じます。<br>
     * この関数では自分（シングルトンinstance）を解放しません。<br>
     * ここで注意してほしいことは、BPFフィルタ構造体も開放しているところです。
    */
    public synchronized void close() {
        if ( pcap != null && fromFile == true) {
            pcap.close();
        }
        if ( bpfFilter != null ) {
            Pcap.freecode(bpfFilter);
            //bpfText = null;
        }
        //pcap = null;
        //readyRun = false;
        fromFile = false;
        fromDev = false;
        filtered = false;
    }

    /**
     * デバッグ用。<br>
     *
     * @param header デバッグのヘッダ
    */
    public void debugMe(final String header) {
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
