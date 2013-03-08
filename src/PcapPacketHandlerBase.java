import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

//*-----識別出来るプロトコル達-----*
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.wan.PPP;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;//Typeof IPv4
import org.jnetpcap.protocol.vpn.L2TP;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
//*-----使用出来るプロトコル達-----*
//パケットアートで使わないプロトコルはインポートしていない。

/**
 * このクラスはパケットアート用のパケットハンドラです。PcapPacketHandlerを継承。
 * ちなみにPcapPacketHandlerはnextPacket関数だけを上書きすればよいinterfaceです。
 * PcapPacketオブジェクトをnextPacket関数で一個ずつ貰い受けます。
 * jnetpcapのプロトコルの識別機能をパケットアート用に抽象的に提供します。
 * nextPacket関数ではプロトコルを識別し、それに対応した動作をします。（未）
 * 
*/
class PcapPacketHandlerBase implements PcapPacketHandler<String> {

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

    /**
     * デフォルトコンストラクタ。new PcapPacketHandlerBase();で呼び出される。
     * @param user ユーザー定義。型はString。使わないならダミーで。
    */
    PcapPacketHandlerBase() {
        super();
    }
    //userはパケットハンドラ呼び出し元がよこしたユーザー定義引数。
    //他にも<Integer>とか色んなものをパケットハンドラに持ってこれるらしい。
    //多分使わんと思うから、放置。

    /**
     * このクラスの核。Pcapから送り込まれたのが何のパケットかを判別する関数。
     * この関数でjnetpcapがよく分からん例外を生成するが、頻度は低い。
     * よって、例外は握りつぶすことにする。この関数は例外を一切放出しない。
     * Pcap.loopによって自動で呼び出されるので、引数を気にする心配は無い。はず。
     *
     * @param packet Pcap.loopによって一個ずつ送り込まれる生パケット
     * @param user 
     * @see org.jnetpcap.packet.Pcap#loop(int cnt, int id, PcapPacketHandler<T> handler, T user)
    */
    public void nextPacket(PcapPacket packet,String user) {
        try {
            //count++;
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
            } else {
                otherPacketHandler(packet);//ETHERNET_PACKETを含まないパケットって、なんだろ
            }
            //レイヤーが高い順にすることで、最上階のレイヤーを扱う。
            //実際には
            //もっといい方法ありそうだけど。
        } catch (Exception e) {
            //e.printStackTrace();
            //jnetpcapが解析できないパケットを受け取ったらここに飛ぶ
            //何もしない。
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
    public void otherPacketHandler(PcapPacket packet) {
    }
}
