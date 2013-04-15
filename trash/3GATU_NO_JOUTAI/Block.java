package jp.ac.kansai_u.kutc.firefly.packetArt;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Arp.ProtocolType;

/**
 * 1つのブロックを表すクラスです。このブロックを複数個組み合わせることで、盤面や落下中のテトリミノおよびペントミノを表現します。
 * このブロックは1つのパケットを保持することができます。このブロックはテトリミノの形状を保持します。<br>
 * もし、テトリミノの形状を保持していない場合、nullを持ちます。パケットを持たない場合も、nullを持ちます。
 * 
 * @author midolin
 */
public class Block {
	Location location;
	BlockType blockType;
	Mino mino;
	ProtocolType protocolType;
	PcapPacket packet;

	// getter and setter
	/**
	 * ブロックの種類を取得します。
	 * @return ブロックの種類
	 */
	public BlockType getBlockType() {
		return blockType;
	}
	/**
	 * ブロックの種類を設定します。
	 * @param blockType ブロックの種類
	 */
	public void setBlockType(BlockType blockType) {
		this.blockType = blockType;
	}

	/**
	 * 設定されているミノを取得します。ミノはテトリミノ、ペンタミノ、もしくはnullが設定されています。
	 * @return
	 */
	public Mino getMino() {
		return mino;
	}

	/**
	 * ミノを設定します。ミノの設定後は自動的にBlockTypeがMinoに変更されます。
	 * @param mino ミノ
	 */
	public void setMino(Mino mino) {
		this.mino = mino;
		blockType = BlockType.Mino;
	}

	/**
	 * 設定されているパケットを取得します。パケットが設定されていない場合、nullを返します。
	 * @return 設定されているJnetPcap.PcapPacketパケット
	 */
	public PcapPacket getPacket() {
		return packet;
	}

	/**
	 * パケットを設定します。
	 * @param packet
	 */
	public void setPacket(PcapPacket packet) {
		this.packet = packet;
	}

	/**
	 * ブロックに位置指定がされている場合、位置を返します。
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 位置を設定します。
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * 位置を設定します。
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {
		location = new Location(x, y);
	}

	public Block() {
		location = new Location();
		blockType = BlockType.Void;
	}

	public Block(Location location, Mino mino) {
		this.location = location;
		this.mino = mino;
		this.blockType = BlockType.Mino;
	}
}
