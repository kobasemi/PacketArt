/**
 *
 */
package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import jp.ac.kansai_u.kutc.firefly.packetArt.Location;
import org.jnetpcap.packet.PcapPacket;

/**
 * パケットを含むブロックです。
 *
 * @author midolin
 * @see jp.ac.kansai_u.kutc.firefly.packetArt.playing.Block
 */
public class PacketBlock extends Block {
	PcapPacket packet;

	/**
	 * パケットを含むブロックを生成します。
	 *
	 * @param location 位置
	 * @param t        設定されているミノの種類
	 */
	public PacketBlock(Location location, Mino t) {
		super(location, t);
	}

	/**
	 * パケットを含むブロックを生成します。
	 */
	public PacketBlock() {
		super();
	}

	/**
	 * パケットを含むブロックを生成します。
	 *
	 * @param x    位置のx座標
	 * @param y    位置のy座標
	 * @param mino 設定されているミノの種類
	 */
	public PacketBlock(int x, int y, Mino mino) {
		this(new Location(x, y), mino);
	}

	/**
	 * 設定されているパケットを取得します。パケットが設定されていない場合、nullを返します。
	 *
	 * @return 設定されているJnetPcap.PcapPacketパケット
	 */
	public PcapPacket getPacket() {
		return packet;
	}

	/**
	 * パケットを設定します。
	 *
	 * @param packet
	 */
	public void setPacket(PcapPacket packet) {
		this.packet = packet;
	}

	@Override
	public PacketBlock clone() {
		Block b = super.clone();
		PacketBlock p = new PacketBlock(b.location, b.mino);
		p.blockType = b.blockType;
		p.packet = packet;
		return p;
	}
}
