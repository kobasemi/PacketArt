//package org.jnetpcap.examples;

import org.jnetpcap.Pcap;//パケットダンプクラス //インポートエラー用メッセージ→jnetpcap.jarはインストールしましたか？そのクラスパスは追加しましたか？jarのパーミッションは644が適切ですよ。javaとjavacのバージョンはあっていますか？
import org.jnetpcap.packet.PcapPacket;//パケットクラス
import org.jnetpcap.packet.PcapPacketHandler;//パケットハンドラクラス
//↑プロトコルアナライザとして使うならこんだけのインポートで十分。

/*

=====出力=====
[ root@PacketMonster ~/PacketArt/src]$ curl http://mawi.nezu.wide.ad.jp/mawi/samplepoint-F/2013/201302101400.dump.gz 2>/dev/null | gunzip | java ProtoSumNTest -
TTTT4TTTT4TTUT44TTTTTTTTTTTT4TTTTTTTTTT4TTTTT4TTTTTTTTTTTTTTT4UTTTTUTT4UTTTTUT4T44T4TTTTT4TTT4TTTTTT
TT4UTUUTTUT4TT6666666664T44TTTTTTTTTTTTUUUUUUTT4UTTTTTTTTTTTTTTTTU4UUUUU4TUUUUUUUUUUUTT66TUTTTUTT664
T4TTTTTTTTTTTTTTTTTTUTT4TTUUUUUUUU4TUTTTUUUTUTT4TTTTTTTTUTTTTTUU4T4TTTTTTTUTTTTT44TTTTTTTTUTTTTTTTTT
TTTTTTTTTU66666666666644TTTTTUTTTTT44TTUTTTU4TTTTTTTTTTTT4TTTTTTUTTTTTTTTTTTTUUUT4UTTUTTUUTUTUUTTTUT
......
=====出力=====

*/


//*-----使用出来るプロトコル達-----*
//import org.jnetpcap.protocol.lan.SLL;
//import org.jnetpcap.protocol.lan.IEEESnap;
//import org.jnetpcap.protocol.lan.IEEE802dot1q;
//import org.jnetpcap.protocol.lan.IEEE802dot2;
//import org.jnetpcap.protocol.lan.IEEE802dot3;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.wan.PPP;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;//Typeof IPv4
import org.jnetpcap.protocol.vpn.L2TP;
//import org.jnetpcap.protocol.network.Rip;//RIPv1+RIPv2
//import org.jnetpcap.protocol.network.Rip1;//RIPv1+RIPv2
//import org.jnetpcap.protocol.network.Rip2;//RIPv1+RIPv2
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

//import org.jnetpcap.protocol.voip.Sdp;
//import org.jnetpcap.protocol.voip.Rtp;
//import org.jnetpcap.protocol.voip.Sip;
//import org.jnetpcap.protocol.tcpip.Http;
//import org.jnetpcap.protocol.application.Html;
//*-----使用出来るプロトコル達-----*
//パケットアートでは使(わ、え)ないプロトコルはコメントアウトしている。


/**
 * このjavaファイルはjnetpcapライブラリを試用するためのファイルですが、
 * ちょこっといじるだけですぐに他のプログラムに転用できます。
*/
public class ProtoSumNTest {

    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Usage: java ProtoSumN URL(or Filename)");
            System.exit(-1);
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

}

class ProtoSumN {
    private StringBuilder myErrbuf; //コンストラクタで代入される。
    //StringBuilderはStringBufferと同じように使える。
    //本格的にするならStringBuffer継承しまくるのがいい。
    private String myFile;//コンストラクタで代入される
    private ProtoSumNPacketHandler myHandler;//コンストラクタで代入される
    private Pcap myPcap;//Pcap型はパケットの読み込みの受け皿。

    ProtoSumN(String filename) {
        myErrbuf = new StringBuilder();
        myHandler = new ProtoSumNPacketHandler();
        myFile = filename;
    }
    //コンストラクタは引数の型や組み合わせで複数作るべき。
    
    public StringBuilder getErrbuf() {
        return myErrbuf;
    }

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

/**
 * このクラスはパケットハンドラ（到着パケットいじくり関数）の一つ。
 * こいつがjnetpcapの本体といってもいい。
*/
class ProtoSumNPacketHandler implements PcapPacketHandler<String> {
    //識別子を宣言
    //定数として扱うのでfinalにしている。
    private final Ethernet ETHERNET_PACKET = new Ethernet();
    private final Arp ARP_PACKET = new Arp();
    private final L2TP L2TP_PACKET = new L2TP();
    private final PPP PPP_PACKET = new PPP();
    private final Icmp ICMP_PACKET = new Icmp();
    //private final Rip1 RIP_PACKET = new Rip1();
    private final Ip4 IP4_PACKET = new Ip4();
    private final Ip6 IP6_PACKET = new Ip6();//Typeof IPv4
    private final Tcp TCP_PACKET = new Tcp();  
    private final Udp UDP_PACKET = new Udp();
    //汚い識別子だよなあ。なんで無駄にインスタンス作らんといかんのかなあ。
    //最下位層からペイロードの種類読んで上のレイヤーに連続ジャンプ、とかできんのか？
    
    //ProtoSumNPacketHandler(){
        //System.sout.println("This Packet has been captured by " + user);
    //}
    //userはパケットハンドラ呼び出し元がよこしたユーザー定義引数。
    //<String>とか、いろんなオブジェクトをパケットハンドラに持ってこれるらしい。
    //今回使わんがな。

    public void nextPacket(PcapPacket packet,String user) {
        //packet.scan();//←何につかうのか分からない関数。
        try {
            if ( packet.hasHeader(TCP_PACKET) ) {  
                System.out.print("T");
                //Tcp tcp = packet.getHeader(TCP_PACKET);
                //System.out.println("\nTCP.dport = "tcp.destination());
            } else if ( packet.hasHeader(UDP_PACKET) ) {  
                System.out.print("U");
            } else if ( packet.hasHeader(IP6_PACKET) ) {  
                System.out.print("6");//IPv6 over IPv4を考えて、
            } else if ( packet.hasHeader(IP4_PACKET) ) {  
                System.out.print("4");
            } else if ( packet.hasHeader(PPP_PACKET) ) {  
                System.out.print("P");
            } else if ( packet.hasHeader(L2TP_PACKET) ) {  
                System.out.print("L");
            } else if ( packet.hasHeader(ICMP_PACKET) ) {  
                System.out.print("I");
            } else if ( packet.hasHeader(ARP_PACKET) ) {  
                System.out.print("A");
            } else if ( packet.hasHeader(ETHERNET_PACKET) ) {  
                System.out.print("E");
            } else {
                System.out.print("?");//ETHERNET_PACKETを含まないパケットって、なんだろ
            }
            //レイヤーが高い順にすることで、最上階のレイヤーを扱う。
            //もっといい方法ありそうだけど。
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.print(packet)
            //jnetpcapが解析できないパケットを受け取ったらここに飛ぶ
        }
    }

}
