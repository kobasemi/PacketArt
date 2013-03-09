//package ...

import java.lang.StringBuilder;
import java.net.URL;
import java.net.InetAddress;
import java.io.File;
import java.io.InputStream;
import java.io.PipedInputStream;//openURL用
import java.io.PipedOutputStream;//openURL用
import java.util.List;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;//openURL用
import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.nio.JMemory;

/**
 * 完全にパケットアート用。
 * 使い方:
 * PcapManager pm = new PcapManager(new File(filename))
 * if (pm.isReadyRun) { pm.run(); }
 * //run()ではPcapPacketHandlerBaseが動く。このクラス自体は例外を発射しません。
 * 
 * その他アクセスできる変数はget??というメソッドを参照してください。
 * 正常にパケットを読めたか確認するにはisReadyRunを使います。
*/
class PcapManager {
    private final int INFINITE = 0;
    private static StringBuilder errBuf;

    private boolean fromFile = false;
    private File pcapFile;

    private boolean fromUrl = false;
    private URL pcapUrl;

    private boolean fromDev = false;
    private PcapIf pcapDev;
    private final int snaplen = 64 * 1024;           // 大きなパケットも読む
    private final int flags = Pcap.MODE_PROMISCUOUS; // プロミスキャスモード  
    private final int timeout = 10 * 1000;// 10秒でタイムアウト？  

    private static boolean readyRun = false;
    
    public Pcap pcap;
    public PcapPacketHandlerBase packetHandler;

    public File getPcapFile() { return pcapFile; }
    public URL getPcapUrl() { return pcapUrl; }
    public PcapIf getPcapDev() { return pcapDev; }
    public boolean isFromFile() { return fromFile; } 
    public boolean isFromUrl() { return fromUrl; } 
    public boolean isfromDev() { return fromDev; } 
    public static boolean isReadyRun() { return readyRun; } 
    public String getErrBuf() { return errBuf.toString(); }

    /**
     * 空コン
     * openURL
     * openFile
     * openDev
     * のいずれかを手動で使おう。
    */
    PcapManager() {
    }

    /**
     * @param name ファイル名もしくはデバイス名もしくはURL。万能コンストラクタ！
    */
    PcapManager(String name) {
        System.err.println("PcapManager(String " + name +")");
        errBuf = new StringBuilder();
        packetHandler = new PcapPacketHandlerBase();

        //name はFilePathか？
        pcapFile = new File(name);
        if (pcapFile.exists() ) {
            System.err.println("*guess* " + name + " = File");
            fromFile = true;
            readyRun = openFile(name);
            return;
        }

        //name はURLか？
        String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (name.matches(urlRegex) ) {
            System.err.println("*guess* " + name + " = URL");
            //完全では無いが、URLっぽいのは確かだ・・・
            fromUrl = true;
            try {
                pcapUrl = new URL(name);
                readyRun = openURL(pcapUrl);
                return;
            } catch (Exception e){
            }//あれ、URLちゃう・・・
        }

        // name はIPアドレスか？
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
                if (ipAddress.equals(name) ) {
                    System.err.println("*guess* " + name + " = IPv4(6) Address");
                    fromDev = true;
                    pcapDev = dev;
                    readyRun = openDev(pcapDev.getName());
                    return;
                }
            }
        }
        //name == 無理ぽ・・・
        System.err.println("PcapManager failed guess what the " + name + " is.");
        return;
    }
    
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

    /**
     * Web上のファイルからパケットを読み出す。
    */
    PcapManager(URL url) {
        System.err.println("PcapManager(URL " + url.toString() +")");
        readyRun = openURL(url);
    }

    /**
     * ローカルのファイルからパケットを読み出す。
    */
    PcapManager(File file) {
        System.err.println("PcapManager(File " + file.getName() +")");
        readyRun = openFile(file.getName());
    }

    /**
     * ローカルのデバイスからパケットを読み出す。
     * 
    */
    PcapManager(PcapIf dev) {
        System.err.println("PcapManager(PcapIf " + dev.getName() +")");
        readyRun = openDev(dev.getName());
    }

    /**
     * URLオブジェクトがコンストラクタの引数の場合に呼ばれる。
     * 800MBものパケットファイルをいちいち全部ダウンロードするのは億劫。
     * だから、この関数でWeb上のパケットファイルを擬似的にロードする。
     * 例外は発生しない。
     * gzipファイルに対応している。他の圧縮形式に対応するにはこの関数を編集。
     *
     * 未実装
     *
     * @return wasOK 成功か失敗か。
    */
    public boolean openURL(URL url) {
        System.err.println("openURL(" + url.toString() +")");
        boolean wasOK = false;
        PipedOutputStream pw = new PipedOutputStream();
        try {
            System.setIn(new PipedInputStream(pw));
            InputStream temp = url.openStream();
            if (url.toString().matches(".*\\.gz(?:ip)?$") ) {//URLで判断する？
                GZIPInputStream in = new GZIPInputStream(temp);
                System.err.println("PcapManager.openURL opened GZinputstream");
                System.setIn(in);
            } else if (false) { 
                //ここで他の形式に対応させる
            } else {
                InputStream in = temp;
                System.err.println("PcapManager.openURL opened inputstream");
                System.setIn(in);
            }
            wasOK = openFile("-");
        } catch (Exception e) {
            System.err.println("Error in PcapManager.openURL");
            e.printStackTrace();
        }
        return wasOK;
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
        return wasOK;
    }

    /**
     * コンストラクタで指定されたpcapをPcapPacketHandlerBaseに与えます。
     * ループは「無限」にしてあります。
     * 一気にロードします。
    */
    public void handlePackets() {
        try {
            pcap.loop(INFINITE, packetHandler,"DummyUserData");
            //ユーザー定義引数は今回は使わないので、適当に埋めている。
        } finally {
            //全部読んだらこっちに引きこむ
            pcap.close();//開けたら、閉めるのを忘れない。
            System.err.println(errBuf );//最後にjnetpcap内部エラー情報を表示。
            System.err.println("Closing PcapManager...");//デバッグ用。
            //return pleaseCloseProgram;パケット読み終わったらどうする？（いわゆる、弾切れ）
        }
    }
    /**
     * 一個ずつロードします。
    */
    public PcapPacket nextPacket() {
        PcapPacket packet = new PcapPacket(JMemory.POINTER);
        if ( pcap.nextEx(packet) == Pcap.NEXT_EX_OK ) {
            return new PcapPacket(packet);
        } else {
            return null;
        }
    }
    /**
     * ファイル、ロックを開放します。
    */
    public void close() {
        pcap.close();
    }
}
