import java.lang.StringBuilder

import org.jnetpcap.Pcap;

import PcapPacketHandlerBase;

/**
 * 完全にパケットアート用。
*/
class PcapManager {
    private Pcap pcap;
    private StringBuilder errBuf;
    private PcapPacketHandlerBase packetHandler;

    private boolean fromFile = false;
    private File pcapFile;
    private boolean fromUrl = false;
    private URL pcapUrl;
    private boolean fromDevice = false;
    private String deviceName;

    private boolean readyRun = false;

    /**
     * @param name ファイル名もしくはデバイス名もしくはURL。万能コンストラクタ！
    */
    PcapManager(String name) {
        errBuf = new StringBuilder();
        packetHandler = new PcapPacketHandlerBase();

        pcapFile = new File(name);
        if (pcapFile.exists() ) {
            fromFile = true;
            this(pcapFile);
            return;
        }

        String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        if (name.matches(urlRegex) ) {
            //完全では無いが、URLっぽいのは確かだ・・・
            fromUrl = true;
            pcapUrl = new URL(name);
            this(pcapUrl);
            return;
        }

        List<PcapIf> alldevs = new ArrayList<PcapIf>();
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {  
            System.err.printf("Can't read list of devices, error is %s", errbuf  
                .toString());
        } else {
            String macAddress;
            byte[] macAddr;
            for (PcapIf dev : alldevs){
                byte[] macAddr = dev.getHardwareAddress();
                for(byte b : macAddr){
                    macAddress += String.format("%02x",macAddrB[i]);
                }
                if(macAddress.delete(":","-") == name){
                    this(dev);
                    return;
                }
            }
        }
        //無理ぽ・・・
        return;
    }

    PcapManager(URL url) {
    }
    PcapManager(File file) {
        pcap = Pcap.openOffline(file.getName() );
    }
    PcapManager(PcapIf dev){
    }

        try {
            ProtoSumN psn = new ProtoSumN(args[0]);//自作クラスを呼び出す。
            psn.openPcap();//pcapファイルを開く関数の呼び出し
            psn.run();//pcapファイルを読み込ループ開始。
            System.out.println(psn.getErrbuf() );//最後にjnetpcap内部エラー情報を表示。
        } catch (Exception e) { 
            // エラーは個別に対処するほうがいいけど、今はしない。
            //出てくるエラーの網羅にはIDEを使わないと多分できない。
            //今回のパケットアートはおそらくマルチスレッドになるので、エラーの見逃しは命取りだ。
            e.printStackTrace();
            System.exit(-2);
        }
    }
    public StringBuilder getErrBuf() {
        return errBuf;
    }
}

    //コンストラクタは引数の型や組み合わせで複数作るべき。
    

    public void openPcap() {
        //if path exists filename
        //else : URL openInputStream stdin openOffline("-",myerrbuf)
        myPcap = Pcap.openOffline(myFile, myErrbuf);//pcapファイルを開く
        //エラーを放出しない代わりにErrbufにエラー情報が書き込まれる。
        if (myPcap == null) {
            System.err.printf("Error while opening a file for capture: "
                + myErrbuf.toString());
            System.exit(-2);
        }
    }

    public void run() {
        try {
            myPcap.loop(10000, myHandler,"DummyUserData");
            //myPcap.loop(0, myHandler,"DummyUserData");
            //無論、マルチスレッドで実行スべき。
            //1000個パケットを一個一個読んでmyHandlerに渡す。
            //1000を0に変えればなら全読み込み。private final int INFINITE = 0;とでもすればわかりやすい。
            //ユーザー定義引数は今回は使わないので、適当に埋めている。
        } finally {
            //1000回読んだらこっちに引きこむ
            myPcap.close();//pcapダンプファイルを開けたら、閉めるのを忘れない。
            //PacketArt.close();パケット読み終わったらどうする？（いわゆる、弾切れ）
        }
    }

}
