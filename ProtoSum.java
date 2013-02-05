//package net.sourceforge.jpcap.tutorial.example4;
import java.util.*;

import net.sourceforge.jpcap.capture.*;
import net.sourceforge.jpcap.net.*;

/**
 * Jpcapの動作確認と、プロトコルの仕分けテストを兼ねて、パケットデータの読み取りを行う。
 * IPv6は使えないんじゃないか・・・？RARPってARPプロトコルの一種じゃないのか・・？
 * 
 * 
 * CUIで動かしてください。
 * 
 * 8[19:35:35 root@PacketMonster ~/PacketArt]$ java ProtoSum ../a.cap
 * PacketCapture: loading native library jpcap.. ok
 * 
 * Captured 1000000 Packets...
 * 
 * Ether Packets: 0
 * ARP Packets: 0
 * RARP Packets: 0
 * ICMP Packets: 78288
 * IGMP Packets: 0
 * IP Packets: 28028
 * IP6 Packets: 77951
 * TCP Packets: 675358
 * UDP Packets: 140374
 * Unknown Packets: 0
 *
 * ~C (800MBも読み込む時間無いから100万パケットでCtrl+Cして中断した）
 * 
*/
public class ProtoSum{
    private static final int PACKET_COUNT = -1/* どんな数のパケットでも読むぜ */; 
    private static final String FILTER = ""/* どんなパケットでも読むぜ */;
    public static PacketCapture m_pcap;//こいつがJpcap本体。
    public static ProtoSumPacketHandler testPacketHandler;//今回、パケットを操作する関数

    /**
     * @param filename WIDEプロジェクトからとったpcapdumpファイル
     * @see <a href="http://www.firefly.kutc.kansai-u.ac.jp/~k843966/jpcap_docs/">PcapDocs</a>
    */
    public ProtoSum(String filename) throws Exception{
        m_pcap = new PacketCapture();
        m_pcap.openOffline(filename);//ファイルが存在しないエラー無視してます。
        m_pcap.setFilter(FILTER, true);//trueってなんだ
        testPacketHandler=new ProtoSumPacketHandler();
        m_pcap.addPacketListener(testPacketHandler);
        m_pcap.capture(PACKET_COUNT);
    }//Exception無視しちゃった！
    public static void main(String[] args){
        try{
            if(args.length < 1){
                System.out.println("Usage: java ProtoSum test.cap");
                System.exit(2);
            }
            ProtoSum testProgram = new ProtoSum(args[0]);
            testProgram.testPacketHandler.print_stats();
            //最後に統計を表示
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}

class ProtoSumPacketHandler implements PacketListener{
    private static final boolean DEBUG = false;
    //private static final boolean DEBUG = true;
    /**
     * デバッグ用
    */
    public void d(String s){
        if(DEBUG){
            System.out.print(s);
        }
        return;
    } 
    private static int count;

    private static int ether;
    private static int arp;
    private static int rarp;
    private static int icmp;
    private static int igmp;
    private static int ip;
    private static int ip6;
    private static int tcp;
    private static int udp;
    private static int other;
    //private static int ipVersion;
    //なんでもスタティック！
    public void ProtoSumPacketHandler(){
        count = 0;
        ether = 0;
        arp = 0;
        rarp = 0;
        icmp = 0;
        igmp = 0;
        ip = 0;
        ip6 = 0;
        tcp = 0;
        udp = 0;
        other = 0;
    }
    public void packetArrived(Packet packet){
        count++;
        if(count % 1000000 == 0){
            print_stats();
            //100万回に一回、パケットの統計を表示
        }

        //=====Jpcapはトランスポート層以上のプロトコルに対応していない
        if(packet instanceof TCPPacket){
            //TCPPacket p = (TCPPacket)packet;
            //int x =  p. getDestinationPort();
            d("T");
            tcp++;
        }else if(packet instanceof UDPPacket){
            //UDPPacket p = (UDPPacket)packet;
            //int x =  p. getSourcePort();
            d("U");
            udp++;
        //=====ここまでトランスポート層 (DCCP,SCTP,RSVP,ECNは下の階へ～)
        }else if(packet instanceof ICMPPacket){
            //ICMPPacket p = (ICMPPacket)packet;
            //int x = p.getMessageMajorCode();
            d("C");
            icmp++;
        }else if(packet instanceof IGMPPacket){
            //IGMPPacket p = (IGMPPacket)packet;
            //int x =  p.getIGMPChecksum();
            d("G");
            igmp++;
        }else if(packet instanceof IPPacket){
            /*IPPacket p = (IPPacket)packet;
            ipVersion = p.getVersion();
            if(ipVersion == IPVersions.IPV4){ */
                d("4");
                ip++;
            /*}else if(ipVersion == IPVersions.IPV6){
                d("6");
                ip6++;
            }*/
        //=====ここまでネットワーク層 (ICMPv6,IPSecは下の階へ～)
        }else if(packet instanceof ARPPacket){
            //EthernetPacketのget~~Protocolで判断すべきだったか
            ARPPacket p = (ARPPacket)packet;
            int x = p.getOperation();
            if(x > 0x3 && x < 0x8){
                //ARPのOPコードが4~7ならRARP。
                d("R");
                rarp++;
            }else{
                d("A");
                arp++;
            }
        }else if(packet instanceof EthernetPacket){
            EthernetPacket p = (EthernetPacket)packet;
            int x = p.getEthernetProtocol();
            if(x == EthernetProtocols.IPV6){
                //苦肉の策
                d("6");
                ip6++;
            }else{
                d("E");
                ether++;
            }
        //=====ここまでデータリンク層 (NDP,OSPF,L2TP,PPPは破棄)
        }else{
            //エラー処理するか、BPFフィルタで弾くべき。
            d("O");
            other++;
        //=====？バイナリレベル。なんだこのパケット。
        }
        return;
    }
    public void print_stats(){
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Captured " + count + " Packets...");
        System.out.println("");
        System.out.println("Ether Packets: " + ether);
        System.out.println("ARP Packets: " + arp);
        System.out.println("RARP Packets: " + rarp);
        System.out.println("ICMP Packets: " + icmp);
        System.out.println("IGMP Packets: " + igmp);
        System.out.println("IP Packets: " + ip);
        System.out.println("IP6 Packets: " + ip6);
        System.out.println("TCP Packets: " + tcp);
        System.out.println("UDP Packets: " + udp);
        System.out.println("Unknown Packets: " + other);
        System.out.println("");
        System.out.println("");
        System.out.println("");
        return;
    }
}
