package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.List;

/**
 * PcapPacketを高速でlibpcapから読み込んだり、一時的に保持したりするクラスです。
 *
 * @see PcapPacket
 * @see PcapPacketHandler
 * @author sya-ke
*/
public class PacketQueue implements PcapPacketHandler<Object>{

    private LimitedQueue<PcapPacket> q;

    /**
     * 唯一のコンストラクタです。
     *
     * @param max キューの最大保持可能数です。
    */
    public PacketQueue(int max) {
        q = new LimitedQueue<PcapPacket>(max);
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
        q.add(packet);
    }

    /**
     * パケットをキューに放り込みます。
     * 
     * @return 成功ならtrueが返ります。基本的にtrueしか返しません。
    */
    public boolean pushPacket(PcapPacket packet) {
        return q.add(packet);
    }

    /**
     * パケットをキューから取り出します。
     *
     * @return キューから取り出したPcapPacketもしくはnull（キューが空のとき）を返します。
    */
    public PcapPacket pollPacket() {
        return q.poll();
    }

    /**
     * パケットをキューから複数取り出します。<br>
     * キューの装填数が少ない場合は<br>
     * 取り出せるMAXまで搾り取り、Listで返します。
     * 
     * @param howMany 何個取り出すかという数値です。
     * @return ListでPcapPacketが返ってきます。空のリストも返ってくることがあります。
    */
    public List<PcapPacket> pollPackets(int howMany) {
        return q.poll(howMany);
    }

    /**
     * 残りパケット数を返します。
     *
     * @return 現在キューに入っている要素の数を返します。
    */
    public int size() {
        return q.size();
    }

    /**
     * パケット最大装填数を返します。
     * 
     * @return max このインスタンスが持つ最大装填数を返します。
    */
    public int maxSize() {
        return q.getMaxSize();
    }

    /**
     * 装填数が限界に達した場合にtrueを返します。
     *
     * @return 限界ならtrue,まだ余裕あるならfalseを返します。
    */
    public boolean isFull() {
        if (size() == maxSize()) {
            return true;
        } else {
            return false;
        }
    }
}
