package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.*;

public class PcapManagerTest {
    public static void main(String[] args) {
        PcapManager pm = PcapManager.getInstance();
        BufferedReader r =
            new BufferedReader(new InputStreamReader(System.in), 1);
        DevUtil devUtil = new DevUtil();
        String devName = null;
        int i = 0;
        for (String info : devUtil.getGoodInformations() ) {
            System.out.println(info);
            //System.out.flush();             // 強制出力
            try {
                i = System.in.read();
                System.in.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 0x31) {
                System.out.println("devUtil.getNameByGoodInformation(" + info +  ")");
                devName = devUtil.getNameByGoodInformation(info);
                System.out.println("devName -> " + devName);
            }
        }

        if (devName != null) {
            pm.openDev(devName);
            System.out.println( pm.nextPacket() );
            System.out.println( "NEXT PACKET" );
            pm.savePackets(2);
            System.out.println( "2 PACKET COMES" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "NEXT PACKET FROM QUEUE" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "NEXT PACKET FROM QUEUE" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "UE isNULL?" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "UE isNULL?" );
            System.out.println( pm.nextPacket() );
            System.out.println( "NEXT PACKET" );
            pm.close();
            pm.openDev(devName);
            pm.savePackets(2);
            System.out.println( "2 PACKET COMES" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "NEXT PACKET FROM QUEUE" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "NEXT PACKET FROM QUEUE" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "UE isNULL?" );
            System.out.println( pm.nextPacketFromQueue() );
            System.out.println( "UE isNULL?" );
            pm.close();
        }
        pm = null;
    }
}
