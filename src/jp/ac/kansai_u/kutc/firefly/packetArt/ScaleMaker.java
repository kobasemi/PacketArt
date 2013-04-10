package jp.ac.kansai_u.kutc.firefly.packetArt;


//キーボード(鍵盤)を生成するクラス
public class ScaleMaker {
	public static void main(String[] args){
		ScaleMaker.eMinorBassScale();
	}
	
	//Key=C(ト長調)の鍵盤を定義するメソッド
	public int[] cMajorScale(){
		int[] CMajor = new int[27];
		CMajor[0] = 36;
		for(int i = 1; i < 27; i++){
			if ((i == 3) || (i == 7) || (i == 10) || (i == 13) ||(i == 17) || (i == 20) || (i == 23) || (i == 27)){
				CMajor[i] = CMajor[i-1] + 1;
			}else{
			CMajor[i] = CMajor[i-1] + 2; 
			}
		}
		return CMajor;
	}
	
	//Key=Am(イ単調)の鍵盤を定義するメソッド
	public int[] aMinorScale(){
		int[] AMinor = new int[27];
		AMinor[0] = 33;
		for(int i = 1; i < 27; i++){
			if ((i == 2) || (i == 5) || (i == 9) || (i == 13) || (i == 16) || (i == 20) || (i == 24) || (i == 27)){
				AMinor[i] = AMinor[i-1] + 1;
			}else{
				AMinor[i] = AMinor[i-1] + 2;
			}
		}
		return AMinor;
	}
	
	//ホ短調(コロブチカと同じ調)ベースライン用鍵盤
	/**
	 * EMinorBass[0] = E
	 * EMinorBass[1] = F#
	 * EMinorBass[2] = G
	 * EMinorBass[3] = A
	 * EMinorBass[4] = B
	 * EMinorBass[5] = C
	 * EMinorBass[6] = D
	 * EMinorBass[7] = E
	 * EMinorBass[8] = F#8va
	 * 
	 */
	public static int[] eMinorBassScale(){
		int[] EMinorBass = new int[9];
		EMinorBass[0] = 40;
		for(int i = 1; i < 9; i++){
			if ((i == 2) || (i == 5)){
				EMinorBass[i] = EMinorBass[i-1] + 1;
			}else{
				EMinorBass[i] = EMinorBass[i-1] + 2;
			}
		}
		return EMinorBass;
	}
	
}
