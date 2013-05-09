//;javac -encoding utf-8 PcapManagerTest.java;java jp.ac.kansai_u.kutc.firefly.packetArt.test.PcapManagerTest;exit;
package jp.ac.kansai_u.kutc.firefly.packetArt.test;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.*;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.*;

/**
 * jnetpcap1.3のPcapPacket.toString()はTCPのデコードに失敗するという<br>
 * エラーを出すようです。それを実証するクラスです。
*/
public class PcapManagerTest extends PacketHolder{
    public static void main(String[] args) {
        final PcapManager pm = PcapManager.getInstance();
        final PacketHolder ph = new PacketHolder();
        pm.setBPFfilter("arp");
        pm.start();
        pm.openFile("10000.cap");
        try {
            System.out.println("wait a 1 sec..");
            Thread.sleep(1000);
        } catch(Exception e) {
        }
        final Object stdoutLock = new Object();
        new Thread(new Runnable(){
            public void run(){
                synchronized(stdoutLock) {
                    PcapPacket pkt = pm.nextPacketFromQueue();
                    if (pkt != null) {
                        ph.setPacket(pkt);
                    }
                    Ip4 ip4 = ph.getIp4();
                    if (ip4 != null) {
                        System.out.print(PacketUtil.bytes2ints(ip4.source()));
                        System.out.print(" => ");
                        System.out.println(PacketUtil.bytes2ints(ip4.destination()));
                    }
                }
            };
        }).start();
        new Thread(new Runnable(){
            public void run(){
                synchronized(stdoutLock) {
                    PcapPacket pkt = pm.nextPacketFromQueue();
                    if (pkt != null) {
                        ph.setPacket(pkt);
                    }
                    Ip4 ip4 = ph.getIp4();
                    if (ip4 != null) {
                        System.out.print(PacketUtil.bytes2ints(ip4.source()));
                        System.out.print(" => ");
                        System.out.println(PacketUtil.bytes2ints(ip4.destination()));
                    }
                }
            };
        }).start();
        try {
            System.out.println("wait a 1 sec..");
            Thread.sleep(1000);
        } catch(Exception e) {
        }
        pm.debugMe("Before kill");
        pm.kill();
        pm.debugMe("After kill");
        pm.close();
    }
}
