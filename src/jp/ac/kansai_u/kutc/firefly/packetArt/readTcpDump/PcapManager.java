package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;//File.existsに必要
import java.util.ArrayList;
import java.util.List;

import java.lang.Runnable;

import org.jnetpcap.Pcap;//こいつが心臓
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.PcapDLT;//イーサネット２。
import org.jnetpcap.PcapBpfProgram;

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
 * <br>
 * その他アクセスできる変数はget??という<br>
 * メソッドを参照してください。<br>
 * 正常にパケットを読めたか確認するには<br>
 * isReadyRunを使ってください。<br>
 *
 * @author sya-ke
*/
public class PcapManager implements Runnable{
    static final PcapManager instance = new PcapManager();
    /**
     * @return シングルトンのインスタンスを返します。
    */
    public static PcapManager getInstance(){
       return instance;
    }

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

    private HandlerHolder handlerHolder;//便利そうだから、public
    private PacketQueue packetQueue;//便利そうだから、public

    public final int TIMEOUT_OPENDEV = 10;//デバイスからの読み込みで、0.01秒のパケット待ちを許す。
    public final int QUEUE_SIZE = 30000;//30000パケットの保持をする。

    /**
     * 空のコンストラクタです。このコンストラクタを<br>
     * 使う場合は、オブジェクト生成後にopenDev(name)<br>
     * もしくはopenFile(name)をしないとパケットが読めません。
    */
    private PcapManager() {
        init();
        System.out.println("PcapManager()");
    }

    /**
     * コンストラクタで最初に呼ばれます。<br>
     * ただの初期化関数です。<br>
     * 何のエラーも引数も返り値もありません。
    */
    public void init() {
        System.out.println("PcapManager.init()");
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
    }

    /**
     * シングルトンでスレッドが無限ループします。<br>
     * PcapManagerは空中のグローバル空間を漂いパケットを拾い、蓄え続けます。<br>
     * PcapManagerは常にパケットを拾い続けつつ一部を捨て、libpcapが浪費する待ちパケットのメモリを開放します。<br>
     * PcapManagerはパケットが欲しいというオブジェクトに惜しみなくパケットを譲ります。<br>
     * PcapManagerはパケットをプロトコルの種類別に譲ります。<br>
    */
    public void run() {
        while(true) {
            while(readyRun == false && pcap == null){
                //
            }
            pkt = null;

            savePackets(3);
            //savePacketsの保存先のキューは満タンになった時点で
            //パケットを捨てていくので、この関数を空撃ちしてパケットを間引くこともできます。

            pkt = nextPacket();//0.01秒間パケットが来なかったらタイムアウトします。
            //パケットが来なかった場合、pktにはnullが入ります。
            if (pkt == null) {
                //パケットが来なくても、キューから非常食のパケットを取り出します。
                pkt = nextPacketFromQueue();
            }
            //それでもパケットが来ないなら、どうしようもありません。
            if (pkt != null ) {
                System.out.println("Inspect!");
                handlerHolder.inspect(pkt);
            }
        }
    }

    /**
     * パケットハンドラを追加し、実行の登録をします。<br>
     * 追加するパケットハンドラはなんらかのhandlerを<br>
     * implementsしていなければなりません。
     * 
     * @param o パケットハンドラをimplementsしたオブジェクト。
     * @return oがパケットハンドラじゃなかった場合、falseが返ります。
    */
    public boolean addHandler(Object o) {
        return handlerHolder.classify(o);
    }

    /**
     * パケットハンドラを削除し、実行の登録解除をします。<br>
     * オブジェクトの参照を比較しているので、登録したものと<br>
     * 全く同一のパケットハンドラが引数でなければなりません。<br>
     * 
     * @param o パケットハンドラをimplementsしたオブジェクト。
    */
    public void removeHandler(Object o) {
        handlerHolder.classify(o);
    }

    /**
     * Fileがコンストラクタの引数の場合に呼ばれる関数です。<br>
     * ファイルの有無チェックはそちらでやってください。<br>
     * この関数はエラーを出しません。エラー内容はgetErrカンスで取得可能です。
     *
     * @param fname ファイル名を文字列で。フルパスでお願いします。
     * @return 成功ならtrueを返します。失敗ならerrBufにエラーが入ってます。
    */
    public boolean openFile(String fname) {
        System.out.println("openFile(" + fname +")");
        if (pcap != null) {
            System.out.println("You've already opened pcap! closing previous one..");
            close();
        }
        pcapFile = new File(fname);
        boolean wasOK = false;
        if (pcapFile.exists() == false) {
            readyRun = false;
        }
        pcap = Pcap.openOffline(fname,errBuf);
        if (pcap == null) {
            System.err.println("Error while opening a file for capture: "
                + errBuf.toString());
            return wasOK;
        }
        fromDev = false;
        fromFile = true;
        filtered = false;
        wasOK = true;
        readyRun = true;
        return wasOK;
    }

    /**
     * デバイス名を指定せずにこの関数を実行した場合、<br>
     * jnetpcapによって最適デバイスを選択されます。<br>
     * この関数はエラーを出しません。<br>
     * エラー内容はgetErr関数で取得可能です。
     * 
     * @return 成功ならtrueを返します。失敗ならerrBufにエラーが入ってます。
    */
    public boolean openDev() {
        String devName = Pcap.lookupDev(errBuf);
        System.out.println("Jnetpcap did a big assumption but....Yes...");
        return openDev(devName);
    }

    /**
     * デバイス名がコンストラクタの引数の場合に<br>
     * 呼ばれます。よってこの関数はエラーを出しません。<br>
     * エラー内容はgetErr関数で取得可能です。
     *
     * @return 成功ならtrueを返します。失敗ならerrBufにエラーが入ってます。
    */
    public boolean openDev(String devName) {
        System.out.println("openDev(" + devName +")");
        boolean wasOK = false;
        //pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.MODE_PROMISCUOUS, Pcap.DEFAULT_TIMEOUT, errBuf);
        pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.MODE_PROMISCUOUS, TIMEOUT_OPENDEV, errBuf);
        if (pcap == null) {
            System.err.println("Error while opening device for capture: "
                + errBuf.toString());
            return wasOK;
        }
        fromDev = true;
        if (fromFile) {
            close();
            fromFile = false;
        }
        wasOK = true;
        filtered = false;
        readyRun = wasOK;
        return wasOK;
    }

    /**
     * 現在開いているtcpdumpのファイルオブジェクトを返します。
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
        return pcap.getErr();
    }

    /**
     * 未テストです。フィルターが有効か否かを返します。
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
    public PcapPacket nextPacket() {
        PcapPacket packet = new PcapPacket(JMemory.POINTER);
        if ( pcap.nextEx(packet) == Pcap.NEXT_EX_OK ) {
          return packet;
        } else {
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
    public PcapPacket nextPacketCopied() {
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
            System.out.println("PCAP NOT OPENED!");
            return false;
        }

        DLT = pcap.datalink();
        howManyEnqeued = pcap.dispatch(howManyPackets, DLT, packetQueue, dummy);
        if (howManyEnqeued < 0) {
            return false;//ループ中にブレークループシグナルを受け取った。
        }
        return true;
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
        return packetQueue.pollPackets(howManyPackets);
    }

    /**
     * 「非常食」の残りパケットを返します。
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
        return packetQueue.pollPacket();
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
        if (isFiltered()) {
            System.out.println("ERRO: Has been Fltered!");
            return false;
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
            System.out.println("PcapManager.getBPF('"+ bpf +"') Failed!");
            if (pcap != null) {
                System.out.println("ERROR is " + pcap.getErr());
            }
            return false;
        }
        if ( pcap != null && pcap.setFilter(filter) == Pcap.NOT_OK){
            System.out.println("PcapManager.getBPF('"+ bpf +"') Failed!");
            if (pcap != null) {
                System.out.println("ERROR is " + pcap.getErr());
            }
            return false;
        }
        bpfFilter = filter;//解放用ポインタの保持。
        filtered = true;
        return true;
    }

    /**
     * アロケートしたメモリや開いたファイルをガベコレの前に閉じます。<br>
     * この関数では自分（シングルトンinstance）を解放しません。<br>
     * 別のクラスから開放してください。
    */
    public void close() {
        if ( pcap != null && fromFile == true) {
            pcap.close();
        }
        if ( bpfFilter != null ) {
            Pcap.freecode(bpfFilter);
        }
        pcap = null;
        readyRun = false;
        fromFile = false;
        fromDev = false;
    }
}
