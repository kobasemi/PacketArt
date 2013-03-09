//package ...

import java.lang.StringBuilder;
import java.net.URL;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

/**
 * 完全にパケットアート用。
 * 使い方:
 * PcapManager pm = new PcapManager(new [URL,String,File])
 * if (pm.isReadyRun) { pm.run(); }
 * //run()ではPcapPacketHandlerBaseが動く
 * 
 * その他アクセスできる変数はget??というメソッドを参照してください。
 * isReadyRunを使うので、エラーは出しません。
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
            System.err.println("It seems local file!");
            fromFile = true;
            readyRun = openFile(name);
            return;
        }

        //name == URL
        String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (name.matches(urlRegex) ) {
            System.err.println("It seems URL!");
            //完全では無いが、URLっぽいのは確かだ・・・
            fromUrl = true;
            try {
                pcapUrl = new URL(name);
                readyRun = openURL(pcapUrl);
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
                    System.err.println("It seeems a device MAC Address!");
                    fromDev = true;
                    pcapDev = dev;
                    readyRun = openDev(pcapDev.getName());
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
        readyRun = openURL(url);
    }

    /**
     * ローカルのファイルからパケットを読み出す。
    */
    PcapManager(File file) {
        readyRun = openFile(file.getName());
    }

    /**
     * ローカルのデバイスからパケットを読み出す。
     * 
    */
    PcapManager(PcapIf dev) {
        readyRun = openDev(dev.getName());
    }

    /**
     * URLオブジェクトがコンストラクタの引数の場合に呼ばれる。
     * 800MBものパケットファイルをいちいち全部ダウンロードするのは億劫。
     * だから、この関数でWeb上のパケットファイルを擬似的にロードする。
     * 例外は発生しない。
     * gzipファイルに対応している。他の圧縮形式に対応するにはこの関数を編集。
     *
     * @return wasOK 成功か失敗か。
    */
    public boolean openURL(URL url) {
        System.err.println("openURL(" + url.toString() +")");
        boolean wasOK = false;
        try {
            InputStream temp = url.openStream();
            if (url.toString().matches(".*\\.gz(?:ip)?$") ) {//URLで判断する？
                GZIPInputStream in = new GZIPInputStream(temp);
                System.setIn(in);
                System.err.println("PcapManager.openURL opened GZinputstream");
            }else if (false){ 
                //ここで他の形式に対応させる
            } else {
                InputStream in = temp;
                System.setIn(in);
                System.err.println("PcapManager.openURL opened inputstream");
            }
            wasOK = openFile("-");
            //in.closeする必要なかったかもしれんなあ
        } catch (Exception e) {
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
        System.err.println("openFile(" + fname +")");
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
        System.err.println("openDev(" + devName +")");
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

    /**
     * コンストラクタで指定されたpcapをPcapPacketHandlerBaseに与えます。
     * ループは「無限」にしてあります。
    */
    public void run() {
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

}
