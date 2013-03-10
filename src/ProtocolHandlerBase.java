import org.jnetpcap.packet.PcapPacket;
//*-----識別するプロトコル達-----*
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.wan.PPP;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;//Typeof IPv4
import org.jnetpcap.protocol.vpn.L2TP;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
//パケットアートで使わないプロトコルはインポートしていない。
//他にはHTTPとか使えるので、知っておくと結構役に立つかもしれない。

/**
 * このクラスはパケットアート用のパケット識別クラスです。
 * protocolHandlerではプロトコルの識別機能をパケットアート用に提供します。
 * 例：もしTCPパケットが読み込まれたらtcpHandlerが呼ばれます。
 * ＃＃＃＃この関数はスーパークラスとして使います。＃＃＃＃
 * 使い方：
 *  class TcpDport extends ProtocolHandlerBase {
 *      public int dport;
 *      public void tcpHandler(Tcp tcp) {
 *          dport = tcp.getDestination();
 *      }
 *  }
 * PcapManager pm = new PcapManager("filename");
 * TcpDport td = new Test();
 * PcapPacket pkt;
 * pkt = pm.nextPacket();
 * td.inspect(pkt);
 * System.err.println("TCP.dport = " + td.dport * 1024);
 *
*/
class ProtocolHandlerBase {

    //パケット識別子兼、「受け皿」を宣言。
    private Ethernet ethernet = new Ethernet();
    private Arp arp = new Arp();
    private L2TP l2tp = new L2TP();
    private PPP ppp = new PPP();
    private Icmp icmp = new Icmp();
    private Ip4 ip4 = new Ip4();
    private Ip6 ip6 = new Ip6();
    private Tcp tcp = new Tcp();  
    private Udp udp = new Udp();

    ProtocolHandlerBase() {
    }

    /**
     * この関数は、プロトコル毎に呼び出す関数を変えます。
     * 最後に、一度関数を呼び出す猶予を与えています。
    */
    public void inspect(PcapPacket packet) {
        try {
            if (packet.hasHeader(tcp) ) {  
                tcpHandler(tcp);//40％ここに来る
            } else if (packet.hasHeader(udp) ) {  
                udpHandler(udp);//30％ここに来る
            } else if ( packet.hasHeader(ip6) ) {  
                ip6Handler(ip6);//IPv4より上にしてIPv6 over IPv4に対応させる
            } else if ( packet.hasHeader(ip4) ) {  
                ip4Handler(ip4);//20％ここニ来る
            } else if ( packet.hasHeader(ppp) ) {  
                pppHandler(ppp);//実際PPPが来ることは殆ど無い
            } else if ( packet.hasHeader(l2tp) ) {  
                l2tpHandler(l2tp);//実際くることはないだろう。
            } else if ( packet.hasHeader(icmp) ) {  
                icmpHandler(icmp);//ちょっとしか来ない。
            } else if ( packet.hasHeader(arp) ) {  
                arpHandler(arp);//実際来ることは無いだろう。
            } else if ( packet.hasHeader(ethernet) ) {  
                ethernetHandler(ethernet);
            }
            packetHandler(packet);//ETHERNET_PACKETを含まないパケットって、なんだろ
            //レイヤーが高い順にすることで、最上階のレイヤーを扱う。
            //実際には
            //もっといい方法ありそうだけど。
        } catch (Exception e) {
            //e.printStackTrace();
            //jnetpcapが解析できないパケットを受け取ったらここに飛ぶ
            //どうしようもない。なので、何もしない。
        }
    }

    /**
     * @see org.jnetpcap.protocol.tcpip.Tcp
    */
    public void tcpHandler(Tcp tcp) {
    }

    /**
     * @see org.jnetpcap.protocol.tcpip.Udp
    */
    public void udpHandler(Udp udp) {
    }

    /**
     * @see org.jnetpcap.protocol.network.Ip6
    */
    public void ip6Handler(Ip6 ip6) {
    }

    /**
     * @see org.jnetpcap.protocol.network.Ip4
    */
    public void ip4Handler(Ip4 ip4) {
    }

    /**
     * @see org.jnetpcap.protocol.wan.PPP
    */
    public void pppHandler(PPP ppp) {
    }

    /**
     * @see org.jnetpcap.protocol.vpn.PPP
    */
    public void l2tpHandler(L2TP lt2p) {
    }

    /**
     * @see org.jnetpcap.protocol.network.Icmp
    */
    public void icmpHandler(Icmp icmp) {
    }

    /**
     * @see org.jnetpcap.protocol.network.Arp
    */
    public void arpHandler(Arp arp) {
    }

    /**
     * @see org.jnetpcap.protocol.lan.Ethernet
    */
    public void ethernetHandler(Ethernet ethernet) {
    }

    /**
     * @see org.jnetpcap.packet.PcapPacket
    */
    public void packetHandler(PcapPacket packet) {
    }

}
