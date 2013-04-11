package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.util.LinkedList;

import jp.ac.kansai_u.kutc.firefly.packetArt.ProtocolHandlerBase;

import org.jnetpcap.protocol.network.Ip4;

/**
 * PacketDisposerクラス <br>
 * このクラスはパケットを受信し、それを処理しやすい形に変換するためのクラスです。<br>
 * このクラスはSingletonとして動作します。そのため、このクラスのインスタンスを生成することはできません。<br>
 * このクラスのインスタンスを取得するためには、以下のように記述します。
 * <code>
 * PacketDisposer instance = PacketDisposer.getInstance();
 * </code>
 * 
 * @author midolin
 *
 */
public class PacketDisposer extends ProtocolHandlerBase{
	static final PacketDisposer instance = new PacketDisposer();
	
	LinkedList<byte[]> destQueue;
	LinkedList<byte[]> srcQueue;

	PacketDisposer() {
		destQueue = new LinkedList<byte[]>();
	}
	
	public static PacketDisposer getInstance(){
		return instance;
	}

	public int[] disposePacket(){
		return null;
	}
	
	@Override
	public void ip4Handler(Ip4 pkt){
		destQueue.push(pkt.destination());
		srcQueue.push(pkt.source());
		
	}
}
