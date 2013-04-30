package jp.ac.kansai_u.kutc.firefly.packetArt.music;

/**
 * ゲーム中BGMに使用する鍵盤を作成するクラスです。
 * @author Lisa
 *
 */
public class ScaleMaker {

	/**
	 * ベースに使用する鍵盤を作成するメソッドです。
	 * AccompanimentMakerからのみ呼び出されます。
	 * 
	 * @return bassscale[]
	 */
	
	/*
	XXXXscale[0] = E
	XXXXscale[1] = F# 
	XXXXscale[2] = G
	XXXXscale[3] = A
	XXXXscale[4] = B
	XXXXscale[5] = C
	XXXXscale[6] = D
	XXXXscale[7] = E#8va
	XXXXscale[8] = F#8va
	XXXXscale[9] = G8va
	*/
	
	public static int[] setBassScale(){
		int[] bassscale = new int[9];
		bassscale[0] = 40;
		for(int i = 1; i < 9; i++){
			if ((i == 2) || (i == 5)){
				bassscale[i] = bassscale[i-1] + 1;
			}else{
				bassscale[i] = bassscale[i-1] + 2;
			}
		}
		return bassscale;
		}

	
	/**
	 * メロディ作成に使用する鍵盤を作成するメソッドです。
	 * AccompanimentMakerからのみ呼び出されます。
	 * 
	 * @return melodyscale
	 */
	public static int[] setMelodyScale(){
		int[] melodyscale = new int[10];
		melodyscale[0] = 64;
		for(int i = 1; i < 9; i++){
			if ((i == 2) || (i == 5)){
				melodyscale[i] = melodyscale[i-1] + 1;
			}else{
				melodyscale[i] = melodyscale[i-1] + 2;
			}
		}
		return melodyscale;
	}
}


