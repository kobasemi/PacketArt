package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.util.ArrayList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketHolder;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketUtil;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.network.Ip4;

/**
 * IPv4を処理し、主旋律の決定要素を作成するクラスです。
 * MelodyMakerからのみ呼び出されます。
 * @author Lisa
 */
public class MelodyAlgorithm {
	
	/**
	 * IPv4をオクテット毎に処理し、配列に格納するメソッドです。
	 * @param length
	 * @return desposedip
	 */
	private static Integer[] desposeIp(int length){
		PacketHolder ph = new PacketHolder();
		PcapManager pm = PcapManager.getInstance();
		
		while(!pm.isReadyRun()){
		}
		
		List<Integer> desposedipArrayList = new ArrayList<Integer>(length);
		while(desposedipArrayList.size() < length){
			PcapPacket pkt = pm.nextPacketFromQueue();
			if(pkt != null){
				ph.setPacket(pkt);
				Ip4 ip4 = null;
				try{
					ip4 = ph.getIp4();
				}catch(Exception e){
				}
				if(ip4 != null){
					int[] ints = PacketUtil.bytes2ints(ip4.source());
					for(int j : ints){
						desposedipArrayList.add(j);
					}
				}
			}
		}
		Integer[] desposedip = desposedipArrayList.toArray(new Integer[desposedipArrayList.size()]);
		return desposedip;
	}
	
	/**
	 * 明るい曲の主旋律を生成するメソッドです。
	 * @param length
	 * @return cheerfulmelody
	 */
	public static int[] defCheerfulAlgorithm(int length){
		String[] code = CodeMaker.setCheerfulCode(length);
		Integer[] desposedip = desposeIp(length);
		int[] cheerfulmelody = new int[code.length];
		int[] melodyscale = ScaleMaker.setCheerfulMelodyScale();
		
		int[] listc = {melodyscale[0], melodyscale[2], melodyscale[4]};
		int[] listf = {melodyscale[0], melodyscale[3], melodyscale[5]};
		int[] listg7 = {melodyscale[1], melodyscale[3], melodyscale[4]};
		
		for(int i = 0; i < length; i++){
			int judge = 0;
			if(desposedip[i] < 85){
				judge = 0;
			}else if(desposedip[i] < 170){
				judge = 1;
			}else if(desposedip[i] < 256){
				judge = 2;
			}
			
			if("C".equals(code[i])){
				cheerfulmelody[i] = listc[judge];
			}else if("F".equals(code[i])){
				cheerfulmelody[i] = listf[judge];
			}else if("G7".equals(code[i])){
				cheerfulmelody[i] = listg7[judge];
			}
		}
		return cheerfulmelody;
	}
	
	/**
	 * 暗い曲の主旋律を生成するメソッドです。
	 * @param length
	 * @return gloomymelody
	 */
	public static int[] defGloomyAlgorithm(int length){
		String[] code = CodeMaker.setGloomyCode(length);
		Integer[] desposedip = desposeIp(length);
		int[] gloomymelody = new int[code.length];
		int[] melodyscale = ScaleMaker.setGloomyMelodyScale();
		
		int[] listem = {melodyscale[0], melodyscale[2], melodyscale[4]};
		int[] listam = {melodyscale[3], melodyscale[5], melodyscale[7]};
		int[] listb7 = {melodyscale[1], melodyscale[3], melodyscale[4]};
		
		for(int i = 0; i < length; i++){
			int judge = 0;
			if(desposedip[i] < 85){
				judge = 0;
			}else if(desposedip[i] < 170){
				judge = 1;
			}else if(desposedip[i] < 256){
				judge = 2;
			}
			
			if("Em".equals(code[i])){
				gloomymelody[i] = listem[judge];
			}else if("Am".equals(code[i])){
				gloomymelody[i] = listam[judge];
			}else if("B7".equals(code[i])){
				gloomymelody[i] = listb7[judge];
			}
		}
		return gloomymelody;
	}
}
