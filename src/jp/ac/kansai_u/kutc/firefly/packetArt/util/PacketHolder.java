package jp.ac.kansai_u.kutc.firefly.packetArt.util;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.JProtocol;
import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Arp;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.wan.PPP;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;//Typeof IPv4
import org.jnetpcap.protocol.vpn.L2TP;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

/**
 * 一つのパケットを保持し、好きなプロトコルを抽出できるクラスです。<br>
 * プロトコルハンドラインターフェースを使いたくない人向けです。<br>
 * 常に一個分のメモリしか食わないプロトコルハンドラには劣りますが、<br>
 * このクラスでも通常利用で一個分、最高でも10個分のパケットしか<br>
 * メモリを食わないことが保証されるで、メモリ効率が良いです。
 *
 * @author sya-ke
*/
public class PacketHolder {

    //ここで代入することで二度とアロケートしないようにする
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

    /**
     * コンストラクタです。何もしません。後からsetPacketをしてください。
    */
    public PacketHolder() {
    }

    /**
     * 初期化と同時にsetPacketで引数のパケットを搭載します。
     *
     * @param packet プロトコルを抽出したいパケット
    */
    public PacketHolder(PcapPacket packet) {
        setPacket(packet);
    }

    /**
     * 新らしいパケットを解析するために使います。<br>
     * 受け皿をさらに使いまわします。
     *
     * @param packet プロトコルを抽出したいパケット
     * 
    */
    public void setPacket(PcapPacket packet) {
        pkt = packet;
    }

    /**
     * 現在保持しているパケットを返します。
     *
     * @return 現在解析しているパケット
    */
    public PcapPacket getPacket() {
        return pkt;
    }

    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public Tcp getTcp() {
        if (pkt != null && pkt.hasHeader(tcp) ) {
            return tcp;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public Udp getUdp() {
        if (pkt != null && pkt.hasHeader(udp) ) {
            return udp;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public Ip6 getIp6() {
        if (pkt != null && pkt.hasHeader(ip6) ) {
            return ip6;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public Ip4 getIp4() {
        if (pkt != null && pkt.hasHeader(ip4) ) {
            return ip4;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public PPP getPPP() {
        if (pkt != null && pkt.hasHeader(ppp) ) {
            return ppp;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public L2TP getL2TP() {
        if (pkt != null && pkt.hasHeader(l2tp) ) {
            return l2tp;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public Icmp getIcmp() {
        if (pkt != null && pkt.hasHeader(icmp) ) {
            return icmp;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public Arp getArp() {
        if (pkt != null && pkt.hasHeader(arp) ) {
            return arp;
        } else {
            return null;
        }
    }
    /**
     * パケットからプロトコルを抽出します。
     *
     * @return プロトコルヘッダ。パケットにそのプロトコルが存在しない場合、パケットそのものがnullの場合はnull
    */
    public Ethernet getEthernet() {
        if (pkt != null && pkt.hasHeader(ethernet) ) {
            return ethernet;
        } else {
            return null;
        }
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasTcp() {
        return  pkt != null & pkt.hasHeader(JProtocol.TCP_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasUdp() {
        return  pkt != null & pkt.hasHeader(JProtocol.UDP_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasIp6() {
        return  pkt != null & pkt.hasHeader(JProtocol.IP6_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasIp4() {
        return  pkt != null & pkt.hasHeader(JProtocol.IP4_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasPPP() {
        return  pkt != null & pkt.hasHeader(JProtocol.PPP_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasL2TP() {
        return  pkt != null & pkt.hasHeader(JProtocol.L2TP_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasICMP() {
        return  pkt != null & pkt.hasHeader(JProtocol.ICMP_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasARP() {
        return  pkt != null & pkt.hasHeader(JProtocol.ARP_ID);
    }

    /**
     * パケットにプロトコルが含まれているかを判定します。
     * @return プロトコルがあるか否か。
    */
    public boolean hasEthernet() {
        return  pkt != null & pkt.hasHeader(JProtocol.ETHERNET_ID);
    }
}
