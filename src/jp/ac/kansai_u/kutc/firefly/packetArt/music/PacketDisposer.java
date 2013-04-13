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
		
		//何とかしてIPv4アドレスをString[]で持ってくる．
		String[] rawip = foo();
		String ipsentence = "";
		
		//splitを使って"."を処理後配列に入れたいので2度手間っぽいが一度String型にする．
		for(int i = 0; i < rawip.length; i++){
			ipsentence = ipsentence + rawip[i] + ".";
		}
		
		//オクテットごとにString配列に格納．
		String[] stringip = ipsentence.split("\\.");
		
		
		int[] ipdata = new int[stringip.length]; //String→intの作業を行うために一時的に使用する配列．
		int[] deposedip = new int[stringip.length]; //こいつの中身の値が後のメロディ生成に影響する．
		
		//IPのオクテットの数字をとにかく1桁の数字に変換する．
		for(int j = 0; j < stringip.length; j++){
			ipdata[j] = Integer.parseInt(stringip[j]);
			if(ipdata[j] < 9){
				deposedip[j] = ipdata[j];
			}else if(ipdata[j] < 100){
				deposedip[j] = Math.round(ipdata[j]/10);
			}else if(ipdata[j] < 200){
				deposedip[j] = Math.round((ipdata[j]-100)/10);
			}else if(ipdata[j] < 256){
				deposedip[j] = Math.round((ipdata[j]-200)/10);
			}
		}
		return deposedip;
	}
	
	@Override
	public void ip4Handler(Ip4 pkt){
		destQueue.push(pkt.destination());
		srcQueue.push(pkt.source());
		
	}
}
