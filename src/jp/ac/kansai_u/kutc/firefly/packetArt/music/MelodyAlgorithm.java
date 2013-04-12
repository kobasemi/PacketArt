package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import java.util.Random;

//BGMのメロディを作成するアルゴリズム
public class MelodyAlgorithm {

	public static void main(String[] args) {
	}

	public static int[] melodyAlgorithm(){
		
		//コード情報が入ったString配列を持ってくる．
		String[] code = CodeMaker.codeMaker();
		
		//音程情報が入る配列．
		int[] melody = new int[24];
		
		//使用する鍵盤配列を持ってくる．
		int[] melodyscale = ScaleMaker.setMelodyScale();
		
		//各コードに対応する音程．()内は実験的に追加．
		//Em : E G B
		//Am : A C E
		//B7 : F# A B D#
		//D#に関してはScaleMakerに無いのでここで数値を弄って作る．		
		int[] emmel = {melodyscale[0], melodyscale[2], melodyscale[4]};
		int[] ammel = {melodyscale[3], melodyscale[5], melodyscale[7]};
		int[] b7mel = {melodyscale[1], melodyscale[3], melodyscale[4], melodyscale[6] + 1}; // melodyscale[6] + 1
		
		//メロディの設定．現在は乱数によって生成しているが，最終的にはIPデータから生成する．
		for (int i = 0; i < 24; i++) {	
			Random rnd1 = new Random();
//			Random rnd2 = new Random();
			int tmp1 = rnd1.nextInt(3);
//			int tmp2 = rnd2.nextInt(4);
			
			if("Em".equals(code[i])){
				melody[i] = emmel[tmp1];
			}else if("Am".equals(code[i])){
				melody[i] = ammel[tmp1];
			}else if("B7".equals(code[i])){
				melody[i] = b7mel[tmp1]; //temp2
			}
		}
		return melody;
	}
}
