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
    private final int MAX_PACKETS = 3000;//スレッドは急には止まれないかも
    private int[] data;
    private int counter;
    private PcapManager pm = PcapManager.getInstance();

    PacketDisposer() {
        data = new int[MAX_PACKETS * 4];//IPv4は4オクテット
        counter = 0;
        pm.addHandler(this);
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
        counter++;
        if (counter < MAX_PACKETS) {
            addData(ip4.destination());
            addData(ip4.source());
        } else {
            pm.removeHandler(this);
            //これが呼ばれた時点でこのhandleIp4関数はPcapManagerに呼ばれない
        }
    }

    public int[] disposePacket() {
        while(counter < MAX_PACKETS){
            return data;
        }
        return null;//ここにはきません。
    }
}
