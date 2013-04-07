package jp.ac.kansai_u.kutc.firefly.packetArt;

import java.util.Arrays;

import jp.ac.kansai_u.kutc.firefly.packetArt.*;

import org.jnetpcap.packet.JHeader;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.*;
import org.jnetpcap.protocol.network.Arp.ProtocolType;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 1つのブロックを表すクラスです。このブロックを複数個組み合わせることで、盤面や落下中のテトリミノおよびペントミノを表現します。
 * このブロックは1つのパケットを保持します。このブロックはテトリミノの形状を保持します。
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
	public BlockType getBlockType() {
		return blockType;
	}

	public void setBlockType(BlockType blockType) {
		this.blockType = blockType;
	}

	public Mino getMino() {
		return mino;
	}

	public void setMino(Mino mino) {
		this.mino = mino;
	}

	public PcapPacket getPacket() {
		return packet;
	}

	public void setPacket(PcapPacket packet) {
		this.packet = packet;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setLocation(int x, int y) {
		location = new Location(x, y);
	}

	// constructor
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
