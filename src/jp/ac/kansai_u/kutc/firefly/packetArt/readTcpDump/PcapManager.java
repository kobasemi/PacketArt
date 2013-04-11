package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;//File.existsに必要
import java.net.InetAddress;//getDevByIPのIPからStringの変換に必要
import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;//こいつが心臓
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;

/*

    TODO: デバイスからの読み込み{
        デバイス名、IPアドレス、MACアドレスから内部デバイスIDを取得できるようにする
        IPアドレスから取得しようとすると、IPv6トンネルが優先的に表示されるけど
        ゲートウェイへのデバイスとしては問題ない。jnetpcapの仕様として、放置。
    }

    TODO: パケット３０００個の保持{
        上記デバイスからの読み込みに際し、jnetpcapにより
        長蛇の待ちパケット行列がメモリ上に生成される可能性が発生した。
        これは普通のtcpdumpにも言える問題であるが、運営から言われたなら
        しかたない。パケットの間引きにBPFを使うのは
        パケットの種類の選択肢が減ることになる。。。
        そのために、パケットをある程度確保したら捨てる方式をとる。
        これなら、使えるパケットの種類を意図的に減らすことはなくなる。
        3000個の確保にはjava.util.Queueを使う。捨てるには、nextPacketを空撃ち。
        nextPacketは透過的に提供する。キューには定期的に補充し、
        キューと、jnetpcapのキューに無くなり次第、関数はnullを返すようにする。
        また、fromFile == Trueのときは空撃ちしない。
    }

    TODO: 残りパケットの（概算値）の保持{
        ファイルサイズ(Byte)を標準MTU,1500byteで割る。WIDEプロジェクトの
        パケットはMTU=1500が最頻値なので、逐次ロードでもおおよそは出せるはずだ
    }

*/

/**
 * 完全にパケットアート用。
 * 使い方:
 * PcapManager pm = new PcapManager(new File(filename))
 * if (pm.isReadyRun) { pm.nextPacket(); }
 * 
 * その他アクセスできる変数はget??というメソッドを参照してください。
 * 正常にパケットを読めたか確認するにはisReadyRunを使います。
*/
public class PcapManager {

    private static StringBuilder errBuf;//libpcapからのエラーをここに
    private File pcapFile;
    private PcapIf pcapDev;
    private boolean fromFile;
    private boolean fromDev;
    private boolean readyRun;
    private Pcap pcap;//jnetpcapの核。

    public File getPcapFile() { return pcapFile; }
    public PcapIf getPcapDev() { return pcapDev; }
    public boolean isFromFile() { return fromFile; } 
    public boolean isfromDev() { return fromDev; } 
    public boolean isReadyRun() { return readyRun; } 
    public String getErrBuf() { return errBuf.toString(); }

    /**
     * 空のコンストラクタ。使わないで！
    */
    public PcapManager() {
        init();
        System.err.println("PcapManager()");
    }


    /**
     * ローカルのファイルからパケットを読み出す。
    */
    public PcapManager(File file) {
        init();
        System.err.println("PcapManager(File " + file.getName() +")");
        openFile(file.getName());
    }

    /**
     * ローカルのデバイスからパケットを読み出す。
    */
    public PcapManager(PcapIf dev) {
        init();
        System.err.println("PcapManager(PcapIf " + dev.getName() +")");
        openDev(dev.getName());
    }

    /**
     * Linuxならeth0だが、WindowsでデバイスIDを取得するのはタイヘン。
     * そこで、様々な文字列からお目当てのパケットを取得できるようにする。
     * 使うなら絶対このコンストラクタ！
     *
     * @param name ファイル名フルパスもしくはデバイスのIPを、Stringで。
    */
    public PcapManager(String name) {
        init();
        System.err.println("PcapManager(String " + name +") -> ***GUESS***");

        //name はFilePathか？
        pcapFile = new File(name);
        if (pcapFile.exists() ) {
            openFile(pcapFile);
            return;
        }

        //nameはデバイスIDか？
        if (name != null) {
            openDev(name);
            return;
        }
        //nameは・・・・何コレ？
        System.err.println("PcapManager failed guess what the " + name + " is.");
        return;
    }

    /**
     * どのコンストラクタでも最初に呼ばれます。
     * 何のエラーも引数も返り値もありません。
    */
    public void init(){
        System.err.println("PcapManager.init()");
        File pcapFile = null;
        PcapIf pcapDev = null;
        pcap = null;
        fromFile = false;
        fromDev = false;
        readyRun = false;
        errBuf = new StringBuilder();
        devUtil = new DevUtil(errBuf);
    }

    /**
     * Fileがコンストラクタの引数の場合に呼ばれる。
     * openOfflineでは例外は発生しない。
     *
     * @return wasOK 成功か失敗か。
    */
    public boolean openFile(String fname){
        System.err.println("openFile(" + fname +")");
        boolean wasOK = false;
        pcap = Pcap.openOffline(fname,errBuf);
        if (pcap == null) {
            System.err.println("Error while opening a file for capture: "
                + errBuf.toString());
            return wasOK;
        }
        pcapFile = new File(fname);
        fromFile = true;
        wasOK = true;
        readyRun = wasOK;
        return wasOK;
    }

    /**
     * デバイス(PcapIf)がコンストラクタの引数の場合に呼ばれる。
     * openLiveでは例外は発生しない。
     *
     * @return wasOK 成功か失敗か。
    */
    public boolean openDev(String devName) {
        System.err.println("openDev(" + devName +")");
        boolean wasOK = false;
        pcap = Pcap.openLive(devName, Pcap.DEFAULT_SNAPLEN, Pcap.MODE_PROMISCUOUS, Pcap.DEFAULT_TIMEOUT, errBuf);
        if (pcap == null) {
            System.err.println("Error while opening device for capture: "
                + errBuf.toString());
            return wasOK;
        }
        fromDev = true;
        wasOK = true;
        readyRun = wasOK;
        return wasOK;
    }

    /**
     * 一個ずつロードします。
    */
    public PcapPacket nextPacket() {
        PcapPacket packet = new PcapPacket(JMemory.POINTER);
        if ( pcap.nextEx(packet) == Pcap.NEXT_EX_OK ) {
            return new PcapPacket(packet);
        } else {
            pcap.close();
            readyRun = false;
            return null;
        }
    }

    //TODO:
    /*public float nokoriPacket() {
        int MTU = 1500;
        int FILESIZE = File.getSize();
        int howManyPackets = FILESIZE/MTU;
        float ret = count / howManyPackets;
    }*/

    public void close() {
    }
}
