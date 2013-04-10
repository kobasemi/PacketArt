
//コロブチカ風BGMの鍵盤生成を担当するクラス．
public class ScaleMaker {
	public static void main(String[] args) {
	}
	
	public static int[] korobassscaleScaleMaker(){
	 
	/**
	 * koroXXXXscale[0] = E
	 * koroXXXXscale[1] = F# 
	 * koroXXXXscale[2] = G
	 * koroXXXXscale[3] = A
	 * koroXXXXscale[4] = B
	 * koroXXXXscale[5] = C
	 * koroXXXXscale[6] = D
	 * koroXXXXscale[7] = E#8va
	 * koroXXXXscale[8] = F#8va
	 * koroXXXXscale[9] = G8va
	 * 
	 */
	 
		int[] korobassscale = new int[9];
		korobassscale[0] = 40;
		for(int i = 1; i < 9; i++){
			if ((i == 2) || (i == 5)){
				korobassscale[i] = korobassscale[i-1] + 1;
			}else{
				korobassscale[i] = korobassscale[i-1] + 2;
			}
		}
		return korobassscale;
		}
 
	public static int[] koroMelodyScaleMaker(){
		
		int[] koromelodyscale = new int[10];
		koromelodyscale[0] = 64;
		for(int i = 1; i < 9; i++){
			if ((i == 2) || (i == 5)){
				koromelodyscale[i] = koromelodyscale[i-1] + 1;
			}else{
				koromelodyscale[i] = koromelodyscale[i-1] + 2;
			}
		}
		return koromelodyscale;
	}
}

