package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.Ip4Handler;
import org.jnetpcap.protocol.network.Ip4;

/**
 * PacketDisposerクラス <br>
 * このクラスはパケットを受信し、それを処理しやすい形に変換するためのクラスです。<br>
 * 
 * @author midolin
 */
public class PacketDisposer implements Ip4Handler{
    private final int MAX_PACKETS = 100;//スレッドは急には止まれないかも
    private int[] data;
    private int counter;
    private PcapManager pm = PcapManager.getInstance();
    private PacketDisposer me;

    PacketDisposer() {
        data = new int[MAX_PACKETS * 4];//IPv4は4オクテット
        counter = 0;
        me = this;
        System.out.println("PacketDisposer.handler atttached");
    }

    public int[] bytes2ints(byte[] b) {
        int[] a = new int[b.length];
        for (int i=0; i<b.length; i++) {
            a[i] = b[i] & 0xff;
        }
        return a;
    }

    public void addData(byte[] b) {
        int[] buf = bytes2ints(b);
        for (int i=0;i<4;i++) {
            data[counter + i] = buf[i];
        }
    }

    public void handleIp4(Ip4 ip4) {
        System.out.println("IPv4 COMES!");
        counter++;
        if (counter < MAX_PACKETS) {
            addData(ip4.destination());
            addData(ip4.source());
        }
    }

    public int[] disposePacket() {
        pm.addHandler(me);
        while(counter < MAX_PACKETS){
        }
        System.out.println("PacketDisposer.handler detached");
        pm.removeHandler(me);
        try{
            Thread.sleep(10000);
        } catch(Exception e){
        }
        //これが呼ばれた時点でこのhandleIp4関数はPcapManagerに呼ばれない
        return data;
    }
}
