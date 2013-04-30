package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.List;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.LimitedQueue;

/**
 * PcapPacketを高速でlibpcapから読み込んだり、一時的に保持したりするクラスです。
 *
 * @see PcapPacket
 * @see PcapPacketHandler
 * @author sya-ke
*/
public class PacketQueue extends LimitedQueue<PcapPacket> implements PcapPacketHandler<Object>{

    /**
     * 唯一のコンストラクタです。
     *
     * @param max キューの最大保持可能数です。
    */
    public PacketQueue(int max) {
        super(max);
    }

    /**
     * インターフェースから実装を強制された関数です。<br>
     * パケットを高速でキューに放り込みます。<br>
     * この関数を直に呼び出すことはありません。
     *
     * @see PcapPacketHandler
     * @param packet 関数によって定められたPcapPacketです。キューに加えられます。
     * @param dummy 気にしないでください。。
    */
    public void nextPacket(PcapPacket packet, Object dummy) {
        if (packet != null) {
            add(packet);
        }
    }
}
