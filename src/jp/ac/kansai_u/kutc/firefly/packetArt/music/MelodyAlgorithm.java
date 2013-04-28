package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketHolder;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketUtil;

import java.util.List;
import java.util.ArrayList;

import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.packet.PcapPacket;


/**
 * 
 * ゲーム中BGMのメインメロディを生成アルゴリズムを持つクラスです。
 * @author Lisa
 *
 */

public class MelodyAlgorithm {

	/**
	 * 
	 * 処理されたパケット情報から音高情報を作成し、配列に格納するメソッドです。
	 * 返されるint配列は0～9の数値のどれかとなります。
	 * MelodyMakerからのみ呼び出されます。
	 * 
	 * @return melody[]
	 */
	public static int[] melodyAlgorithm(){
		
		//コード情報が入ったString配列を持ってくる．
		String[] code = CodeMaker.codeMaker();
//----------------------------------------------------------------
		PacketHolder ph = new PacketHolder();
		PcapManager pm = PcapManager.getInstance();

		while (!pm.isReadyRun() ){
		}

		List<Integer> desposedipArrayList = new ArrayList<Integer>(24);
		while (desposedipArrayList.size() < 24) {
			PcapPacket pkt = pm.nextPacketFromQueue();
			if (pkt != null) {
				ph.setPacket(pkt);
				Ip4 ip4 = ph.getIp4();
				if (ip4 != null) {
					int[] ints = PacketUtil.bytes2ints(ip4.source());
					for (int j : ints) {
						desposedipArrayList.add(j);
					}
				}
			}
		}
		Integer[] desposedip = desposedipArrayList.toArray(new Integer[desposedipArrayList.size()]);
//----------------------------------------------------------------
		
		//音程情報が入る配列．
		int[] melody = new int[24];
		
		//使用する鍵盤配列を持ってくる．
		int[] melodyscale = ScaleMaker.setMelodyScale();
		
		//各コードに対応する音程．(出現比率)
		//Em : E G B (4:3:3)
		//Am : A C E (4:3:3)
		//B7 : F# A B D# (3:3:3:1)
		//D#に関してはScaleMakerに無いのでここで数値を弄って作る．		
		int[] emmel = {melodyscale[0], melodyscale[2], melodyscale[4]};
		int[] ammel = {melodyscale[3], melodyscale[5], melodyscale[7]};
		int[] b7mel = {melodyscale[1], melodyscale[3], melodyscale[4]}; //melodyscale[6] + 1
		
		//メロディの設定．
		for (int i = 0; i < 24; i++) {	
			int judge = 0;
			if(desposedip[i] < 4){
				judge = 0;
			}else if(desposedip[i] < 7){
				judge = 1;
			}else if(desposedip[i] < 10){
				judge = 2;
			}
			
			if("Em".equals(code[i])){
				melody[i] = emmel[judge];
			}else if("Am".equals(code[i])){
				melody[i] = ammel[judge];
			}else if("B7".equals(code[i])){
				melody[i] = b7mel[judge];
			}
		}
		return melody;
	}
}
