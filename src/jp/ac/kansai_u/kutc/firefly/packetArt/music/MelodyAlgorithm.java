package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.util.Random;



//コロブチカ風BGMのメロディを作成するアルゴリズム
public class MelodyAlgorithm {

	public static void main(String[] args) {
	}

	public static int[] koroMelodyArgorithm(){
		
		//コード情報が入ったString配列を持ってくる．
		String[] code = CodeMaker.koroCodeMaker();
		
		//音程情報が入る配列．
		int[] melody = new int[24];
		
		//使用する鍵盤配列を持ってくる．
		int[] koromelodyscale = ScaleMaker.koroMelodyScaleMaker();
		
		//各コードに対応する音程．()内は実験的に追加．
		//Em : E G B
		//Am : A C E
		//B7 : F# A B D#
		//D#に関してはKoroScaleMakerに無いのでここで数値を弄って作る．		
		int[] emmel = {koromelodyscale[0], koromelodyscale[2], koromelodyscale[4]};
		int[] ammel = {koromelodyscale[3], koromelodyscale[5], koromelodyscale[7]};
		int[] b7mel = {koromelodyscale[1], koromelodyscale[3], koromelodyscale[4]}; // koromelodyscale[6] + 1
		
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
