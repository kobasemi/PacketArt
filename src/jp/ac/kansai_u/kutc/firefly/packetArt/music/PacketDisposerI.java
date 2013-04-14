package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import jp.ac.kansai_u.kutc.firefly.packetArt.ProtocolHandlerBase;
import org.jnetpcap.protocol.network.Ip4;

public class PacketDisposerI extends ProtocolHandlerBase{
    private boolean fully;
    private final int MAX_PACKETS = 3000;
    private int[] data;
    private int counter;

    public PacketDisposerI() {
        data = new int[MAX_PACKETS * 4];//IPv4は4オクテット
        fully = false;
        counter = 0;
    }

    public static int[] bytes2ints(byte[] b) {
        int[] a = new int[b.length];
        for (int i=0; i<b.length; i++) {
            a[i] = b[i] & 0xff;
        }
        return a;
    }

    private void addData(byte[] b) {
        int[] buf = bytes2ints(b);
        data[counter] = buf[0];
        data[counter+1] = buf[1];
        data[counter+2] = buf[2];
        data[counter+3] = buf[3];
    }

    /**
     * すぐにpktが解放されるので、高速なnextPacket関数が使えます。
    */
    @Override
    public void ip4Handler(Ip4 pkt){
        counter++;
        if (counter < MAX_PACKETS) {
            addData(pkt.destination());
        } else {
            fully = true;
        }
    }

    public boolean isFull() {
        return fully;
    }

    public int[] getData() {
        return data;
    }
}
