import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.wan.PPP;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.vpn.L2TP;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.packet.PcapPacket;
//-----------ここまで、必要なimport。コメントアウト不要。



/**
 * このクラスは継承するものではなく、コピーして名前を変えて利用する用です。
 * 使い方：
 * １：このファイルをコピーする。
 * ２：名前を適当に変える。変えた名前に応じてクラス名とコンストラクタ名を変える
 * ３：コンストラクタにアートを初期化するコードを付け加える。
 * ４：使いたいプロトコルを持ってくる関数をオーバーライドするため、該当する箇所のコメントを外す。
 * ５：クラスに、プロトコルのデータを使ってアートを生成する関数を作る
 * ６：Formの適当なところでこのクラスのインスタンスを生成する
 * ６：親クラスから継承したinspect関数を実行する。
 * ７：inspect後に5:で作った関数を呼ぶと、inspectしたパケットに関するデータが5:で使われる。
 *
 *     Test test = new Test();
 *     test.inspect(packet);
 *     test.paint(g);
 */
class HinagataHandler extends ProtocolHandlerBase {
//コピーするとき、HinagataHandlerというクラス名、変えるの忘れずに

//--ここから
    private Tcp tcp;
    private Udp udp;
    private Ip6 ip6;
    private Ip4 ip;
    private PPP ppp;
    private L2TP l2tp;
    private Icmp icmp;
    private Arp arp;
    private Ethernet ethernet;
    private PcapPacket base;

    HinagataHandler() {//コンストラクタ名、書き換えるの忘れずに
        tcp = null;
        udp = null;
        ip6 = null;
        ip = null;
        ppp = null;
        l2tp = null;
        icmp = null;
        arp = null;
        ethernet = null;
        base = null;

        //初期化はここで。
    }
//--ここまで、別にコメントアウトしなくてもいい部分。そんなメモリ食わんし。


    /**
     * ======= 使いたいプロトコルを選んでコメントアウトをはずす箇所 =======
     * ProtocolHandlerBaseから継承したinspect(PcapPacket packet)関数で、
     * packetのプロトコルに応じて以下の内のどれかの関数が初めて呼び出されます。
     * これらの関数は親関数で空宣言されており、それをオーバーライドする事で
     * パケットをプロトコル別に扱うことができるようになります。
     * 使用したいプロトコルのみコメントアウトをはずしてください。
     * この中で主に呼び出されやすい(パケットファイルに含まれている)と思うのは
     * UDPとTCPとIPとIPv6の関数です。コメントアウトしない、というのも一つの手ですが
     * 行が増えるだけなので、使わないプロトコルの関数は、消しても構いません。
    */
    //public void tcpHandler(Tcp tcpPacket){tcp = tcpPacket};//40%呼ばれる
    //public void udpHandler(Udp udpPacket){udp = udpPacket};//30%
    //public void ip6Handler(Ip6 ip6Packet){ip6 = ip6Packet};//20%
    //public void ip4Handler(Ip4 ip4Packet){ip = ipPacket};//8%
    //public void pppHandler(PPP pppPacket){ppp = pppPacket};//0%?
    //public void l2tpHandler(L2TP l2tpPacket){l2tp = l2tpPacket};//0%
    //public void icmpHandler(Icmp icmpPacket){icmp = icmpPacket};//2%
    //public void arpHandler(Arp arpPacket){arp = arpPacket};//0%
    //public void ethernetHandler(Ethernet ethernetPacket){ethernet = ethernetPacket};//0%//?
    /**
     * こっちのpacketHandler関数はプロトコルに関わらず、最後に必ず呼ばれます。
     * 使いたいときは、コメントアウトをはずしてください。
    */
    //public void packetHandler(PcapPacket pcapPacket){base = pcapPacket};//100%

    /**
     * フォームから呼ぶための関数の例です。
     * 上で代入した、使用するプロトコルのデータがここで使えます。
     * 使用出来るプロトコルのデータは以下を参照してください。
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/lan/Ethernet.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Arp.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Icmp.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/wan/PPP.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Ip4.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Ip6.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/vpn/L2TP.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/tcpip/Udp.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/tcpip/Tcp.html
     * http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/packet/PcapPacket.html
    */
    //public void paint(Graphics g) {
    //    int dport = tcp.destination();
          
    //}
    //public void sound() {
    //}
    

}
