package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import org.jnetpcap.protocol.tcpip.Tcp;

import jp.ac.kansai_u.kutc.firefly.packetArt.ProtocolHandlerBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.PacketDisposer;

public class PacketAnalyzer extends ProtocolHandlerBase{

	public PacketAnalyzer() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	@Override
	public void tcpHandler(Tcp tcp){
		PacketDisposer.sendPacket(tcp);
	}

}
