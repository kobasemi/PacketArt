//package org.jnetpcap.examples;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

//-----Protocols

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
// SIP,HTTPなどなど・・・
//-----

//
public class ProtoSumNTest {
    public static void main(String[] args) {
        if(args.length < 1) {
                System.out.println("Usage: java ProtoSumN test.cap");
                System.exit(-1);
        }
        try {
        ProtoSumN psn = new ProtoSumN(args[0]);
        psn.openPcap();//pcapファイルのio処理
        psn.run();//loop開始
        System.out.println(psn.getErrbuf() );//jnetpcap内部エラー情報を表示
        } catch (Exception e) { // エラーは個別に対処するほうがいい
            e.printStackTrace();
            System.exit(-2);
        }
    }
}

class ProtoSumN {
    private StringBuilder myErrbuf;
    //StringBufferではなくStringBuilderなので。本格的にするならStringBuffer継承しまくる。
    private String myFile;
    private ProtoSumNPacketHandler myHandler;
    private Pcap myPcap;

    ProtoSumN(String filename) {
        myErrbuf = new StringBuilder();
        myHandler = new ProtoSumNPacketHandler();
        myFile = filename;
    }
    
    public StringBuilder getErrbuf() {
        return myErrbuf;
    }
    public void openPcap() {
        myPcap = Pcap.openOffline(myFile, myErrbuf);//pcapファイルを開く
        if (myPcap == null) {
            System.err.printf("Error while opening a file for capture: "
                + myErrbuf.toString());
            System.exit(-2);
        }
    }

    public void run() {
        try {
            myPcap.loop(1000, myHandler,"DummyUserData");
            //1000個パケット読んでmyHandlerに渡す
        } finally {
            //1000回読んだらこっちに引きこむ
            myPcap.close();//開けたら閉める
            //PacketArt.close();
        }
    }
}



class ProtoSumNPacketHandler implements PcapPacketHandler<String> {
    //identifierを宣言
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
    
    //ProtoSumNPacketHandler(){
        //System.sout.println("This Packet has been captured by " + user);
    //}
    //<String>とか、いろんなオブジェクトをパケットハンドラに持ってこれるらしい。
    //使わんがな。
    public void nextPacket(PcapPacket packet,String user) {
        //packet.scan();
        if ( packet.hasHeader(TCP_PACKET) ) {  
            System.out.print("T");
            //Tcp tcp = packet.getHeader(TCP_PACKET);
            //System.out.println("\nTCP.dport = "tcp.destination());
        } else if ( packet.hasHeader(UDP_PACKET) ) {  
            System.out.print("U");
        } else if ( packet.hasHeader(IP6_PACKET) ) {  
            System.out.print("6");//IPv6 over IPv4を考えて、
        } else if ( packet.hasHeader(IP4_PACKET) ) {  
            System.out.print("U");
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
            System.out.print("?");
        }
    }
}
