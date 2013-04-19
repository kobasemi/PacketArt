/**
 * 
 */
package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import jp.ac.kansai_u.kutc.firefly.packetArt.Location;

import org.jnetpcap.packet.PcapPacket;

/**
 * @author midolin
 *
 */
public class PacketBlock extends Block {
	PcapPacket packet;
	
	public PacketBlock(Location location, Mino t) {
		super(location,t);
	}

	public PacketBlock() {
		super();
	}

	public PacketBlock(int i, int j, Mino mino) {
		this(new Location(i,j), mino);
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

}
