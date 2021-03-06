package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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

import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.*;

/**
 * PcapManagerが呼び出すべきあらゆるハンドラを<br>
 * 保持するクラスです。
 *
 * @author sya-ke
*/
public class HandlerHolder extends ProtocolHandlerBase {

    private final List<TcpHandler> tcpHandlers;
    private final List<UdpHandler> udpHandlers;
    private final List<Ip6Handler> ip6Handlers;
    private final List<Ip4Handler> ip4Handlers;
    private final List<PPPHandler> pppHandlers;
    private final List<L2TPHandler> l2tpHandlers;
    private final List<IcmpHandler> icmpHandlers;
    private final List<ArpHandler> arpHandlers;
    private final List<EthernetHandler> ethernetHandlers;
    private final List<PacketHandler> packetHandlers;

    private final List<OnPcapClosedHandler> onPcapClosedHandlers;
    private final List<OnPcapOpenedHandler> onPcapOpenedHandlers;
    private final List<OnNoPacketsLeftHandler> onNoPacketsLeftHandlers;

    /**
     * 全部空に初期化します。<br>
     * 空の状態でinspect()しても無意味です。
    */
    public HandlerHolder() {
        System.out.println("HandlerHolder()");
        tcpHandlers = Collections.synchronizedList(new ArrayList<TcpHandler>());
        udpHandlers = Collections.synchronizedList(new ArrayList<UdpHandler>());
        ip6Handlers = Collections.synchronizedList(new ArrayList<Ip6Handler>());
        ip4Handlers = Collections.synchronizedList(new ArrayList<Ip4Handler>());
        pppHandlers = Collections.synchronizedList(new ArrayList<PPPHandler>());
        l2tpHandlers = Collections.synchronizedList(new ArrayList<L2TPHandler>());
        icmpHandlers = Collections.synchronizedList(new ArrayList<IcmpHandler>());
        arpHandlers = Collections.synchronizedList(new ArrayList<ArpHandler>());
        ethernetHandlers = Collections.synchronizedList(new ArrayList<EthernetHandler>());
        packetHandlers = Collections.synchronizedList(new ArrayList<PacketHandler>());

        onPcapClosedHandlers = Collections.synchronizedList(new ArrayList<OnPcapClosedHandler>());
        onPcapOpenedHandlers = Collections.synchronizedList(new ArrayList<OnPcapOpenedHandler>());
        onNoPacketsLeftHandlers = Collections.synchronizedList(new ArrayList<OnNoPacketsLeftHandler>());
    }

    /**
     * ハンドラを追加する関数です。<br>
     * これしか方法が思いつきませんでした。<br>
     * そう何回も呼ばれる関数ではないので、これで妥協します。<br>
     *
     * @param o なんらかのパケットハンドラを実装(implement)したオブジェクトです。
     * @return o がなんのパケットハンドラも実装していないオブジェクトだったならfalseを返します。
    */
    public boolean classify(final Object o) {
        boolean added = false;
        System.out.println("classifying protocolHandler: " + o.getClass().getName());
        synchronized(o){ 
            if (o instanceof TcpHandler) {
                addHandler((TcpHandler)o);
                System.out.println("Adding a TcpHandler..");
                added = true;
            }
            if (o instanceof UdpHandler) {
                addHandler((UdpHandler)o);
                System.out.println("Adding a UdpHandler..");
                added = true;
            }
            if (o instanceof Ip6Handler) {
                addHandler((Ip6Handler)o);
                System.out.println("Adding a Ip6Handler..");
                added = true;
            }
            if (o instanceof Ip4Handler) {
                addHandler((Ip4Handler)o);
                System.out.println("Adding a Ip4Handler..");
                added = true;
            }
            if (o instanceof PPPHandler) {
                addHandler((PPPHandler)o);
                System.out.println("Adding a PPPHandler..");
                added = true;
            }
            if (o instanceof L2TPHandler) {
                addHandler((L2TPHandler)o);
                System.out.println("Adding a L2TPHandler..");
                added = true;
            }
            if (o instanceof IcmpHandler) {
                addHandler((IcmpHandler)o);
                System.out.println("Adding a IcmpHandler..");
                added = true;
            }
            if (o instanceof ArpHandler) {
                addHandler((ArpHandler)o);
                System.out.println("Adding a ArpHandler..");
                added = true;
            }
            if (o instanceof EthernetHandler) {
                addHandler((EthernetHandler)o);
                System.out.println("Adding a EthernetHandler..");
                added = true;
            }
            if (o instanceof PacketHandler) {
                addHandler((PacketHandler)o);
                System.out.println("Adding a PacketHandler..");
                added = true;
            }
            if (o instanceof OnPcapClosedHandler) {
                addHandler((OnPcapClosedHandler)o);
                System.out.println("Adding a OnPcapClosedHandler..");
                added = true;
            }
            if (o instanceof OnPcapOpenedHandler) {
                addHandler((OnPcapOpenedHandler)o);
                System.out.println("Adding a OnPcapOpenedHandler..");
                added = true;
            }
            if (o instanceof OnNoPacketsLeftHandler) {
                addHandler((OnNoPacketsLeftHandler)o);
                System.out.println("Adding a OnNoPacketsLeftHandler..");
                added = true;
            }
        }
        return added;
    }

    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
     * @see  ProtocolHandlerBase#inspect(PcapPacket)
    */
    public synchronized void addHandler(final TcpHandler t) {
        tcpHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public synchronized void addHandler(final UdpHandler t) {
        udpHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final Ip6Handler t) {
        ip6Handlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public synchronized void addHandler(final Ip4Handler t) {
        ip4Handlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final PPPHandler t) {
        pppHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final L2TPHandler t) {
        l2tpHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final IcmpHandler t) {
        icmpHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final ArpHandler t) {
        arpHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final EthernetHandler t) {
        ethernetHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final PacketHandler t) {
        packetHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final OnPcapClosedHandler t) {
        onPcapClosedHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final OnPcapOpenedHandler t) {
        onPcapOpenedHandlers.add(t);
    }
    /**
     * パケットのハンドラを登録します。<br>
     * いちいちインターフェース毎に登録するのも<br>
     * 面倒なので、classifyが便利です。
     *
     * @param t interfaceを実装したハンドラです。
    */
    public  synchronized void addHandler(final OnNoPacketsLeftHandler t) {
        onNoPacketsLeftHandlers.add(t);
    }

    /**
     * パケットのハンドラを登録解除します。<br>
     * 登録したオブジェクトと全く同一のものを<br>
     * 引数にしなければ削除されません。
     *
     * @param o 登録解除したいオブジェクトです。
    */
    public boolean removeHandler(final Object o) {
        System.out.println("removing protocolHandler: " + o.getClass().getName());
        boolean removed = false;
        synchronized(this) {
            if ( tcpHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a TcpHandler..");
            }
            if ( udpHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a UdpHandler..");
            }
            if ( ip6Handlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a Ip6Handler..");
            }
            if ( ip4Handlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a Ip4Handler..");
            }
            if ( pppHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a PPPHandler..");
            }
            if ( l2tpHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a L2TPHandler..");
            }
            if ( icmpHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a IcmpHandler..");
            }
            if ( arpHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a ArpHandler..");
            }
            if ( ethernetHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a EthernetHandler..");
            }
            if ( packetHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a PacketHandler..");
            }
            if ( onPcapClosedHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a OnPcapClosedHandler..");
            }
            if ( onPcapOpenedHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a OnPcapOpenedHandler..");
            }
            if ( onNoPacketsLeftHandlers.remove(o) ) {
                removed = true;
                System.out.println("Removing a NoPacketsLeftHandler..");
            }
        }
        return removed;
    }

//-----ProtocolHandlerBaseのメソッドをオーバーライドしたものです
    public void handleTcp(final Tcp tcp) {
        for (final TcpHandler h: tcpHandlers) {
            h.handleTcp(tcp);
        }
    }

    public void handleUdp(final Udp udp) {
        for (final UdpHandler h: udpHandlers) {
            h.handleUdp(udp);
        }
    }

    public void handleIp6(final Ip6 ip6) {
        for (final Ip6Handler h: ip6Handlers) {
            h.handleIp6(ip6);
        }
    }

    public void handleIp4(final Ip4 ip4) {
        for (final Ip4Handler h: ip4Handlers) {
            h.handleIp4(ip4);
        }
    }

    public void handlePPP(final PPP ppp) {
        for (final PPPHandler h: pppHandlers) {
            h.handlePPP(ppp);
        }
    }

    public void handleL2TP(final L2TP lt2p) {
        for (final L2TPHandler h: l2tpHandlers) {
            h.handleL2TP(lt2p);
        }
    }

    public void handleIcmp(final Icmp icmp) {
        for (final IcmpHandler h: icmpHandlers) {
            h.handleIcmp(icmp);
        }
    }

    public void handleArp(final Arp arp) {
        for (final ArpHandler h: arpHandlers) {
            h.handleArp(arp);
        }
    }

    public void handleEthernet(final Ethernet ethernet) {
        for (final EthernetHandler h: ethernetHandlers) {
            h.handleEthernet(ethernet);
        }
    }

    public void handlePacket(final PcapPacket packet) {
        for (final PacketHandler h: packetHandlers) {
            h.handlePacket(packet);
        }
    }
//-----ProtocolHandlerBaseのメソッドをオーバーライドしたものです

//-----PcapManagerのスレッドからのみ呼ばれるので排他制御しない
    public void onPcapClosed() {
        for (final OnPcapClosedHandler h: onPcapClosedHandlers) {
            h.onPcapClosed();
        }
    }
    public void onPcapOpened() {
        for (final OnPcapOpenedHandler h: onPcapOpenedHandlers) {
            h.onPcapOpened();
        }
    }
    public void onNoPacketsLeft() {
        for (final OnNoPacketsLeftHandler h: onNoPacketsLeftHandlers) {
            h.onNoPacketsLeft();
        }
    }
//-----PcapManagerのスレッドからのみ呼ばれるので排他制御しない
}
