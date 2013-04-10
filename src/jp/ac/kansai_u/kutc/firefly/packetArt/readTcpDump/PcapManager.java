package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.nio.JMemory;
import org.jnetpcap.packet.PcapPacket;


/*

    TODO: デバイスからの読み込み{
        デバイス名、IPアドレスから内部デバイスIDを取得できるようにする
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
    private final int INFINITE = 0;
    private final int SNAPLEN = 64 * 1024;           // 大きなパケットも読む
    private final int FLAGS = Pcap.MODE_PROMISCUOUS; // プロミスキャスモード  
    private final int TIMEOUT = 10 * 1000;// 10秒でタイムアウト  

    private static StringBuilder errBuf;//libpcapからのエラーをここに

    private File pcapFile;
    private PcapIf pcapDev;

    private boolean fromFile = false;
    private boolean fromDev = false;
    private boolean readyRun = false;
    
    public Pcap pcap;

	public File getPcapFile() { return pcapFile; }
    public PcapIf getPcapDev() { return pcapDev; }
    public boolean isFromFile() { return fromFile; } 
    public boolean isfromDev() { return fromDev; } 
    public boolean isReadyRun() { return readyRun; } 
    public String getErrBuf() { return errBuf.toString(); }

    /**
     * 空コン
     * openFile
     * openDev
     * のいずれかを手動で使おう。
    */
    public PcapManager() {
        System.err.println("PcapManager()");
        System.err.println("CALL open !!!!");
        readyRun = false;
        errBuf = new StringBuilder();
    }

    /**
     * Linuxならeth0だが、WindowsでデバイスIDを取得するのはタイヘン。
     * そこで、様々な文字列からお目当てのパケットを取得できるようにする。
     *
     * @param name ファイル名もしくはデバイスのIPを、文字列で。
    */
    public PcapManager(String name) {
        System.err.println("PcapManager(String " + name +")");
        errBuf = new StringBuilder();

        //name はFilePathか？
        pcapFile = new File(name);
        if (pcapFile.exists() ) {
            System.err.println("*guess* " + name + " = File");
            this(pcapFile);
            return;
        }

        //nameはIP(IPaddress)か？
        pcapDev = getDevByIP(name);
        if (pcapDev != null) {
            this(pcapDev);
            return;
        }
        //name == 無理ぽ・・・
        System.err.println("PcapManager failed guess what the " + name + " is.");
        return;
    }

    /**
     * ローカルのファイルからパケットを読み出す。
    * existsチェックしてません。
    */
    public PcapManager(File file) {
        errBuf = new StringBuilder();
        System.err.println("PcapManager(File " + file.getName() +")");
        readyRun = openFile(file.getName());
        fromFile = readyRun;
    }

    /**
     * ローカルのデバイスからパケットを読み出す。
     * existsチェックしてません。
    */
    public PcapManager(PcapIf dev) {
        errBuf = new StringBuilder();
        System.err.println("PcapManager(PcapIf " + dev.getName() +")");
        readyRun = openDev(dev.getName());
        fromDev = readyRun;
    }

        
    /*
    public static ArrayList<Tuple<ArrayList<String>,String>> getDeviceList() {

        ArrayList<Tuple<ArrayList<String>,String>> ret = new ArrayList<Tuple<ArrayList<String>,String>>();
        ArrayList<String> ipAddrs = new ArrayList<String>();
        
        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        int r = Pcap.findAllDevs(alldevs, errBuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {  
            System.err.printf("Can't read list of devices, error is %s", errBuf.toString());
        } else {
            List<PcapAddr> pcapAddrs = null;
            PcapSockAddr pcapSockAddr = null;
            for (PcapIf dev : alldevs){
                if (dev != null) {
                    try {
                        pcapAddrs = dev.getAddresses();
                        for (PcapAddr pcapAddr : pcapAddrs ) {
                            pcapSockAddr = pcapAddr.getAddr();
                            String ipAddr = InetAddress.getByAddress(pcapSockAddr.getData()).getHostAddress();
                            if(ipAddr != null) {
                                ipAddrs.add(ipAddr);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                ret.add(new Tuple(ipAddrs,dev.getName() + dev.getDescription()));
                }
            }
        }
        return ret;
    }
    */

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
        pcap = Pcap.openLive(devName, snaplen, flags, timeout, errBuf);
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
     * デバイスのIPからデバイスID（PcapIf）を取得します。
     *
     * @param ip デバイスの持つIPアドレス。
     * @return dev デバイスオブジェクト。該当無しならNULL。
    */
    public PcapIf getDevByIP(String ip) {
        System.err.println("getDevByIP(" + ip + ")");
        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        int r = Pcap.findAllDevs(alldevs, errBuf);
        List<PcapAddr> pcapAddrs = null;
        PcapSockAddr pcapSockAddr = null;
        String ipAddress = "";
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {  
            System.err.printf("Can't read list of devices, error is %s", errBuf.toString());
        } else {
            for (PcapIf dev : alldevs){
                if (dev != null) {
                    try {
                        pcapAddrs = dev.getAddresses();
                        for (PcapAddr pcapAddr : pcapAddrs ) {
                            pcapSockAddr = pcapAddr.getAddr();
                            ipAddress = InetAddress.getByAddress(pcapSockAddr.getData()).getHostAddress();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.err.println(ipAddress);
                if (ipAddress.equals(ip) ) {
                    System.err.println("*guess* " + ip + " = IPv4(6) Address");
                    return dev;
                }
            }
        }
        return null;
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
