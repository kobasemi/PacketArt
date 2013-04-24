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
 * 最もレイヤーの高いプロトコルを識別し、対応するハンドラを自動で呼び出します。<br>
 * 例：<br>
 *     もしTCPパケットがinspectメソッドで読み込まれたらhandleTcpが呼ばれます。<br>
 *     次に「inspectが呼び出された」ということを認識させる用に、<br>
 *     handlePacketが呼ばれます。最後に保持用にパケットがpktに代入されます。<br>
 *     pktを保持するので、inspect後もgetTcp()を使う事でTCPのデータを取得することができます。<br>
 * 使い方：
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
     * この関数は、プロトコル毎に呼び出す関数を変えます。
     * 最後に、一度関数を呼び出す猶予を与えています。
     * 
     * @param packet PcapManager.nextPacket()で読みだしたパケットです。
     * 
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
                handleTcp(tcp);//40％ここに来る
            }
            if (packet.hasHeader(udp) ) {  
                handleUdp(udp);//30％ここに来る
            }
            if ( packet.hasHeader(ip6) ) {  
                handleIp6(ip6);//IPv4より上にしてIPv6 over IPv4に対応させる
            }
            if ( packet.hasHeader(ip4) ) {  
                handleIp4(ip4);//20％ここニ来る
            }
            if ( packet.hasHeader(ppp) ) {  
                handlePPP(ppp);//実際PPPが来ることは殆ど無い
            }
            if ( packet.hasHeader(l2tp) ) {  
                handleL2TP(l2tp);//実際くることはないだろう。
            }
            if ( packet.hasHeader(icmp) ) {  
                handleIcmp(icmp);//ちょっとしか来ない。
            }
            if ( packet.hasHeader(arp) ) {  
                handleArp(arp);//実際来ることは無いだろう。
            }
            if ( packet.hasHeader(ethernet) ) {  
                handleEthernet(ethernet);
            }
            handlePacket(packet);
            //レイヤーが高い順にすることで、最上階のレイヤーを扱う。

            //pktを上書き。次のパケットが来れば参照は切断される。
            //new PcapPacketすることで、厄介な参照問題を防ぐ。
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
