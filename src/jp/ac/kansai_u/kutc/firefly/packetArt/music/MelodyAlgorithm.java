package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import java.util.Random;

//BGMのメロディを作成するアルゴリズム
public class MelodyAlgorithm {

	public static void main(String[] args) {
	}

	public static int[] melodyAlgorithm(){
		
		//コード情報が入ったString配列を持ってくる．
		String[] code = CodeMaker.codeMaker();
		
		//Disposeされたパケットが入っている配列を持ってくる．
		int[] desposedip = PacketDisposer.disposePacket();
		
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
		}
		return melody;
	}
}
