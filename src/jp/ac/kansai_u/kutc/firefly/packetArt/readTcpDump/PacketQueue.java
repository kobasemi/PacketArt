package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import java.util.List;

/**
 * PcapManager.nextPacket()を30000回ぶん回すよりこっちのが早い
 *
 * @author sya-ke
*/
public class PacketQueue implements PcapPacketHandler<Object>{

    private LimitedQueue<PcapPacket> q;

    /**
     * @param max キューの最大保持可能数
    */
    public PacketQueue(int max) {
        q = new LimitedQueue<PcapPacket>(max);
    }

    /**
     * インターフェースから実装を強制された関数です。パケットを高速でキューに放り込みます。
    */
    public void nextPacket(PcapPacket packet, Object dummy) {
        q.add(packet);
    }

    /**
     * パケットをキューに放り込みます
     * 
     * @return q.add() 成功ならtrue。基本的にtrueしか帰って来ません。
    */
    public boolean pushPacket(PcapPacket packet) {
        return q.add(packet);
    }

    /**
     * パケットをキューから取り出します。
     * @return q.poll() キューに入っていたPcapPacketもしくはnul（キューは空）
    */
    public PcapPacket pollPacket() {
        return q.poll();
    }

    /**
     * パケットをキューから複数取り出します。キューの装填数が少ない場合は<br>
     * 取り出せるMAXまで搾り取り、Listで返します。
     * 
     * @param howMany 何個取り出すか
     * @return q.poll(howMany) ListでPcapPacketが返ってきます。
    */
    public List<PcapPacket> pollPackets(int howMany) {
        return q.poll(howMany);
    }

    /**
     * 現在のサイズ、つまり残りパケット数を返します。
     *
     * @return q.size() キューのサイズ。
    */
    public int size() {
        return q.size();
    }

    /**
     * パケット最大装填数を返します。
     * 
     * @return max このインスタンスが持つ最大装填数です。
    */
    public int maxSize() {
        return q.getMaxSize();
    }

    /**
     * 装填数が限界に達した場合にtrueを返します。
     *
     * @return T/F 限界ならtrue,まだ余裕あるならfalseを返します。
    */
    public boolean isFull() {
        if (size() == maxSize()) {
            return true;
        } else {
            return false;
        }
    }
}
