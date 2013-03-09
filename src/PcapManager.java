//package ...

import java.lang.StringBuilder;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

/**
 * 完全にパケットアート用。
*/
class PcapManager {
    private Pcap pcap;
    private final int INFINITE = 0;
    private StringBuilder errBuf;
    private PcapPacketHandlerBase packetHandler;

    private boolean fromFile = false;
    private File pcapFile;

    private boolean fromUrl = false;
    private URL pcapUrl;

    private boolean fromDev = false;
    private PcapIf pcapDev;
    private final int snaplen = 64 * 1024;           // 大きなパケットも読む
    private final int flags = Pcap.MODE_PROMISCUOUS; // プロミスキャスモード  
    private final int timeout = 10 * 1000;// 10秒でタイムアウト？  

    private boolean readyRun = false;

    public File getPcapFile() { return pcapFile;}
    public URL getPcapUrl() { return pcapUrl; }
    public PcapIf getPcapDev() { return pcapDev; }
    public boolean isFromFile() { return fromFile; } 
    public boolean isFromUrl() { return fromUrl; } 
    public boolean isfromDev() { return fromDev; } 
    public boolean isReadyRun() { return readyRun; } 
    public String getErrBuf() { return errBuf.toString(); }

    /**
     * @param name ファイル名もしくはデバイス名もしくはURL。万能コンストラクタ！
    */
    PcapManager(String name) {
        errBuf = new StringBuilder();
        packetHandler = new PcapPacketHandlerBase();

        //name == FilePath
        pcapFile = new File(name);
        if (pcapFile.exists() ) {
            fromFile = true;
            openFile(name);
            return;
        }

        //name == URL
        String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (name.matches(urlRegex) ) {
            //完全では無いが、URLっぽいのは確かだ・・・
            fromUrl = true;
            try {
                pcapUrl = new URL(name);
                openURL(pcapUrl);
                return;
            } catch (Exception e){
            }//あれ、URLちゃう・・・
        }

        // name == "xx:xx:xx:xx:xx:xx" (mac address)
        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        int r = Pcap.findAllDevs(alldevs, errBuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {  
            System.err.printf("Can't read list of devices, error is %s", errBuf  
                .toString());
        } else {
            byte[] macAddr = null;
            String macAddress = "";
            for (PcapIf dev : alldevs){
                try {
                    macAddr = dev.getHardwareAddress();
                } catch (Exception e) {
                }//あれ、Macアドレス見られないんか。
                for(byte b : macAddr){
                    macAddress += String.format("%02x",b);
                }
                name = name.replace(":","");
                name = name.replace("-","");
                if(macAddress == name){
                    fromDev = true;
                    pcapDev = dev;
                    openDev(pcapDev.getName());
                    return;
                }
            }
        }
        //name == 無理ぽ・・・
        System.err.println("Can't find how to work with String Constructor!");
        return;
    }

    /**
     * Web上のファイルからパケットを読み出す。
    */
    PcapManager(URL url) {
        openURL(url);
    }

    /**
     * ローカルのファイルからパケットを読み出す。
    */
    PcapManager(File file) {
        openFile(file.getName());
    }

    /**
     * ローカルのデバイスからパケットを読み出す。
     * 
    */
    PcapManager(PcapIf dev) {
        openDev(dev.getName());
    }

    /**
     * URLオブジェクトがコンストラクタの引数の場合に呼ばれる。
     * 800MBものパケットファイルをいちいち全部ダウンロードするのは億劫。
     * だから、この関数でWeb上のパケットファイルを擬似的にロードする。
     * 例外は発生しない。
     *
     * @return wasOK 成功か失敗か。
    */
    public boolean openURL(URL url){
        boolean wasOK = false;
        try{
            InputStream in = url.openStream();
            System.setIn(in);
            wasOK = openFile("-");
        }catch (Exception e){
            System.err.println("Error in PcapManager.openURL");
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
        boolean wasOK = false;
        pcap = Pcap.openOffline(fname,errBuf);
        if (pcap == null) {
            System.err.printf("Error while opening a file for capture: "
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
        boolean wasOK = false;
        pcap = Pcap.openLive(devName, snaplen, flags, timeout, errBuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                + errBuf.toString());
            return wasOK;
        }
        fromDev = true;
        wasOK = true;
        return wasOK;
    }

    public void run() {
        try {
            pcap.loop(INFINITE, packetHandler,"DummyUserData");
            //ユーザー定義引数は今回は使わないので、適当に埋めている。
        } finally {
            //全部読んだらこっちに引きこむ
            pcap.close();//開けたら、閉めるのを忘れない。
            System.err.println(errBuf );//最後にjnetpcap内部エラー情報を表示。
            //PacketArt.close();パケット読み終わったらどうする？（いわゆる、弾切れ）
        }
    }

}
