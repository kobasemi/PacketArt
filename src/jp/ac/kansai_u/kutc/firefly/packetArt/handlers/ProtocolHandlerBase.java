package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;
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
 * 対応するレイヤのそれぞれのハンドラを自動で呼び出します。<br>
 * 例：<br>
 *     もしTCP on IP on Etherというありがちなパケットが送られてきた場合、<br>
 *     パケットがinspectメソッドで読み込まれたらまずhandleTcpが呼ばれます。<br>
 *     次にhandleIp4が呼ばれます。次にhandleEthernetが呼ばれます。<br>
 *     次にhandlePacketが呼ばれます。パケットがnullだった場合は<br>
 *     willHandlePackets == true の時のみ一個前のパケットについてもう一度以上のことを行います。<br>
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

    /**
     * 使わなくなった関数です。
     *
     * @param will パケットを受け取るの意志を示します。
    */
    public void setWillHandlePackets(boolean will) {
        willHandlePackets = will;
    }

    /**
     * 使わなくなった関数です。
     * 使い古しのパケットかどうかを判別します。
     *
     * @return 古いやつならtrue
    */
    public boolean isHandledPacket() {
        return handled;
    }

    /**
     * ただのコンストラクタです。
    */
    protected ProtocolHandlerBase() {
        handled = false;
        setWillHandlePackets(true);
    }

    /**
     * この関数は、プロトコル毎に関数を呼び出します。
     * 
     * @param packet PcapManager.nextPacket()で読みだしたパケットです。
     * @see org.jnetpcap.packet.PcapPacket
    */
    public synchronized void inspect(PcapPacket packet) {
        if (willHandlePackets == false) {
            if (packet != null) {
                pkt = new PcapPacket(packet);
                handled = false;
            }
            return;
            //もうパケットいらないよ！っていう人向け。何もしない。
            //本当に何もしないので、非常に高速。
        } else if (packet == null && pkt != null) {
            handlePacket(pkt);
            return;
            //呼び出し元でinspect(null)された場合に発動。
            //inspectが呼ばれたという事実は残したいので、
            //一つ前のパケットを呼び出すことにした。
            //一つ前のパケットか、否かはisHandledPacket()で判断できる。
        }
        try {
            handled = false;
            if (packet.hasHeader(tcp) ) {  
                handleTcp(tcp);//40％
            }
            if (packet.hasHeader(udp) ) {  
                handleUdp(udp);//30％
            }
            if ( packet.hasHeader(ip6) ) {  
                handleIp6(ip6);//20%
            }
            if ( packet.hasHeader(ip4) ) {  
                handleIp4(ip4);//80％
            }
            if ( packet.hasHeader(ppp) ) {  
                handlePPP(ppp);//0.1%?
            }
            if ( packet.hasHeader(l2tp) ) {  
                handleL2TP(l2tp);//0%?
            }
            if ( packet.hasHeader(icmp) ) {  
                handleIcmp(icmp);//15%
            }
            if ( packet.hasHeader(arp) ) {  
                handleArp(arp);//2%?
            }
            if ( packet.hasHeader(ethernet) ) {  
                handleEthernet(ethernet);//100%
            }
            handlePacket(packet);//100%

            //pktを上書き。次のパケットが来れば参照は切断される。
            //new PcapPacketすることで、厄介な参照問題を防いでいる。
            pkt = new PcapPacket(packet);
            handled = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Maybe jnetpcapBug... Ignoring...");
            //jnetpcapが解析できないパケットを受け取ったらここに飛ぶ
            //どうしようもない。なので、何もしない。
        }
    }


//以下、オーバーライドする関数達。
//使いたい関数だけオーバーライドする。
    /**
     * @see org.jnetpcap.protocol.tcpip.Tcp
    */
    public void handleTcp(Tcp tcp){};

    /**
     * @see org.jnetpcap.protocol.tcpip.Udp
    */
    public void handleUdp(Udp udp){};

    /**
     * @see org.jnetpcap.protocol.network.Ip6
    */
    public void handleIp6(Ip6 ip6){};

    /**
     * @see org.jnetpcap.protocol.network.Ip4
    */
    public void handleIp4(Ip4 ip4){};

    /**
     * @see org.jnetpcap.protocol.wan.PPP
    */
    public void handlePPP(PPP ppp){};

    /**
     * @see org.jnetpcap.protocol.vpn.PPP
    */
    public void handleL2TP(L2TP lt2p){};

    /**
     * @see org.jnetpcap.protocol.network.Icmp
    */
    public void handleIcmp(Icmp icmp){};

    /**
     * @see org.jnetpcap.protocol.network.Arp
    */
    public void handleArp(Arp arp){};

    /**
     * @see org.jnetpcap.protocol.lan.Ethernet
    */
    public void handleEthernet(Ethernet ethernet){};

    /**
     * @see org.jnetpcap.packet.PcapPacket
    */
    public void handlePacket(PcapPacket packet){};

}
