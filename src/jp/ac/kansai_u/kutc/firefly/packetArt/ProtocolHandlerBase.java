package jp.ac.kansai_u.kutc.firefly.packetArt;
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
 * ############このクラスは継承して使います##############<br>
 * このクラスはパケットアート用のパケット保持クラスです。<br>
 * jnetpcapが特殊なメモリ管理をしており、 受け皿を使いまわすことで<br>
 * メモリ使用量を増やさないようにすることができます。<br>
 * そのため、このクラスは一つのパケットしか保持できません。<br>
 * 新たにパケットをこのクラスに保持させるにはinspectメソッドを使います。<br>
 * inspectメソッドではパケットアートとjnetpcapで扱える、<br>
 * 最もレイヤーの高いプロトコルを識別し、対応するハンドラを自動で呼び出します。<br>
 * 例：<br>
 *     もしTCPパケットがinspectメソッドで読み込まれたらtcpHandlerが呼ばれます。<br>
 *     次に「inspectが呼び出された」ということを認識させる用に、<br>
 *     packetHandlerが呼ばれます。最後に保持用にパケットがpktに代入されます。<br>
 *     pktを保持するので、inspect後もgetTcp()を使う事でTCPのデータを取得することができます。<br>
 * 使い方：
 * <code>
 * class ExtendedProtocolHandler extends ProtocolHandlerBase {
 *     public void tcpHandler(Tcp t) {
 *         Syste.err.println("TCP COMES!!");
 *     }
 * }
 * PcapManager pm = new PcapManager("filename");
 * ExtendedProtocolHandler extendedProtocolHandler = new ExtendedProtocolHandler();
 * PcapPacket pkt;
 * pkt = pm.nextPacket();
 * ExtendedProtocolHandler.inspect(pkt);
 * Ip4 ip4;
 * int addr = 0;
 * if ( (ip4 = extendedProtocolHandler.getIp4()) != null) {
 *      addr = ip4.destinationToInt();
 * }
 * </code>
 *
 * @author syake
 *
*/
public class ProtocolHandlerBase {

    //パケット識別子兼、「受け皿」を宣言。
    //メモリの効率化を図るため
    //「受け皿」を使いまわす関数しか用意しない。
    private Ethernet ethernet = new Ethernet();
    private Arp arp = new Arp();
    private L2TP l2tp = new L2TP();
    private PPP ppp = new PPP();
    private Icmp icmp = new Icmp();
    private Ip4 ip4 = new Ip4();
    private Ip6 ip6 = new Ip6();
    private Tcp tcp = new Tcp();  
    private Udp udp = new Udp();
    private PcapPacket pkt;
    private boolean willHandlePackets;
    private boolean handled;

    public void setWillHandlePackets(boolean will) {
        willHandlePackets = will;
    }
    public boolean isHandledPacket() {
        return handled;
    }

    protected ProtocolHandlerBase() {
        handled = false;
        setWillHandlePackets(true);
    }

    /**
     * この関数は、プロトコル毎に呼び出す関数を変えます。
     * 最後に、一度関数を呼び出す猶予を与えています。
     * 
     * @param packet PcapManager.nextPacket()で読みだしたパケット。
     * 
     * @see org.jnetpcap.packet.PcapPacket
    */
    public void inspect(PcapPacket packet) {
        if (willHandlePackets == false) {
            if (packet != null) {
                pkt = new PcapPacket(packet);
                handled = false;
            }
            return;
            //もうパケットいらないよ！っていう人向け。何もしない。
            //本当に何もしないので、非常に高速。
        } else if (packet == null && pkt != null) {
            packetHandler(pkt);
            return;
            //呼び出し元でinspect(null)された場合に発動。
            //inspectが呼ばれたという事実は残したいので、
            //一つ前のパケットを呼び出すことにした。
            //一つ前のパケットか、否かはisHandledPacket()で判断できる。
        }
        try {
            handled = false;
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
            packetHandler(packet);
            //レイヤーが高い順にすることで、最上階のレイヤーを扱う。

            //pktを上書き。次のパケットが来れば参照は切断される。
            //new PcapPacketすることで、厄介な参照問題を防ぐ。
            pkt = new PcapPacket(packet);
            handled = true;
        } catch (Exception e) {
            System.err.println("jnetpcapBug! Ignoring...");
            //jnetpcapが解析できないパケットを受け取ったらここに飛ぶ
            //どうしようもない。なので、何もしない。
        }
    }


//以下、オーバーライドする関数達。
//使いたい関数だけオーバーライドする。
    /**
     * @see org.jnetpcap.protocol.tcpip.Tcp
    */
    public void tcpHandler(Tcp tcp){};

    /**
     * @see org.jnetpcap.protocol.tcpip.Udp
    */
    public void udpHandler(Udp udp){};

    /**
     * @see org.jnetpcap.protocol.network.Ip6
    */
    public void ip6Handler(Ip6 ip6){};

    /**
     * @see org.jnetpcap.protocol.network.Ip4
    */
    public void ip4Handler(Ip4 ip4){};

    /**
     * @see org.jnetpcap.protocol.wan.PPP
    */
    public void pppHandler(PPP ppp){};

    /**
     * @see org.jnetpcap.protocol.vpn.PPP
    */
    public void l2tpHandler(L2TP lt2p){};

    /**
     * @see org.jnetpcap.protocol.network.Icmp
    */
    public void icmpHandler(Icmp icmp){};

    /**
     * @see org.jnetpcap.protocol.network.Arp
    */
    public void arpHandler(Arp arp){};

    /**
     * @see org.jnetpcap.protocol.lan.Ethernet
    */
    public void ethernetHandler(Ethernet ethernet){};

    /**
     * @see org.jnetpcap.packet.PcapPacket
    */
    public void packetHandler(PcapPacket packet){};

//以下、「保険」。TCPとIP、UDPとICMPとか、複数のプロトコルを使いたい人用。
//ここでも受け皿をぶん回して使う。これらの関数を呼び出すのは
//inspect前に呼び出すと、一つ前の情報が帰ってきます。どうでもいい？

    public Tcp getTcp(PcapPacket packet) {
        if (packet != null && packet.hasHeader(tcp) ) {
            return tcp;
        } else {
            return null;
        }
    }
    public Tcp getTcp() {
        if (pkt != null && pkt.hasHeader(tcp) ) {
            return tcp;
        } else {
            return null;
        }
    }
    public Udp getUdp(PcapPacket packet) {
        if (packet != null && packet.hasHeader(udp) ) {
            return udp;
        } else {
            return null;
        }
    }
    public Udp getUdp() {
        if (pkt != null && pkt.hasHeader(udp) ) {
            return udp;
        } else {
            return null;
        }
    }
    public Ip6 getIp6(PcapPacket packet) {
        if (packet != null && packet.hasHeader(ip6) ) {
            return ip6;
        } else {
            return null;
        }
    }
    public Ip6 getIp6() {
        if (pkt != null && pkt.hasHeader(ip6) ) {
            return ip6;
        } else {
            return null;
        }
    }
    public Ip4 getIp4(PcapPacket packet) {
        if (packet != null && packet.hasHeader(ip4) ) {
            return ip4;
        } else {
            return null;
        }
    }
    public Ip4 getIp4() {
        if (pkt != null && pkt.hasHeader(ip4) ) {
            return ip4;
        } else {
            return null;
        }
    }
    public PPP getPPP(PcapPacket packet) {
        if (packet != null && packet.hasHeader(ppp) ) {
            return ppp;
        } else {
            return null;
        }
    }
    public PPP getPPP() {
        if (pkt != null && pkt.hasHeader(ppp) ) {
            return ppp;
        } else {
            return null;
        }
    }
    public L2TP getL2TP(PcapPacket packet) {
        if (packet != null && packet.hasHeader(l2tp) ) {
            return l2tp;
        } else {
            return null;
        }
    }
    public L2TP getL2TP() {
        if (pkt != null && pkt.hasHeader(l2tp) ) {
            return l2tp;
        } else {
            return null;
        }
    }
    public Icmp getIcmp(PcapPacket packet) {
        if (packet != null && packet.hasHeader(icmp) ) {
            return icmp;
        } else {
            return null;
        }
    }
    public Icmp getIcmp() {
        if (pkt != null && pkt.hasHeader(icmp) ) {
            return icmp;
        } else {
            return null;
        }
    }
    public Arp getArp(PcapPacket packet) {
        if (packet != null && packet.hasHeader(arp) ) {
            return arp;
        } else {
            return null;
        }
    }
    public Arp getArp() {
        if (pkt != null && pkt.hasHeader(arp) ) {
            return arp;
        } else {
            return null;
        }
    }
    public Ethernet getEthernet(PcapPacket packet) {
        if (packet != null && packet.hasHeader(ethernet) ) {
            return ethernet;
        } else {
            return null;
        }
    }
    public Ethernet getEthernet() {
        if (pkt != null && pkt.hasHeader(ethernet) ) {
            return ethernet;
        } else {
            return null;
        }
    }
    public PcapPacket getPacket() {
        return pkt;
    }
}
