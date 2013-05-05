package jp.ac.kansai_u.kutc.firefly.packetArt.music;

public class ScaleMaker {
	public static int[] melodyscale = new int[16];
	public static int[] bassscale = new int[16];
	public static int[] guitarscale = new int[16];
	public static int i;
	
	public static int[] setCheerfulMelodyScale(){
		melodyscale[0] = 60;
		for(i = 1; i < melodyscale.length; i++){
			if((i == 3) || (i == 7) || (i == 10) || (i == 14)){
				melodyscale[i] = melodyscale[i - 1] + 1;
			}else{
				melodyscale[i] = melodyscale[i - 1] + 2;
			}
		}
		return melodyscale;
	}
	
	public static int[] setGloomyMelodyScale(){
		melodyscale[0] = 64;
		for(i = 1; i < melodyscale.length; i++){
			if((i == 2) || (i == 5) || (i == 9) || (i == 12)){
				melodyscale[i] = melodyscale[i - 1] + 1;
			}else{
				melodyscale[i] = melodyscale[i - 1] + 2;
			}
		}
		return melodyscale;
	}
	
	public static int[] setCheerfulBassScale(){
		bassscale[0] = 36;
		for(i = 1; i < bassscale.length; i++){
			if((i == 3) || (i == 7) || (i == 10) || (i == 14)){
				bassscale[i] = bassscale[i - 1] + 1;
			}else{
				bassscale[i] = bassscale[i - 1] + 2;
			}
		}
		return bassscale;
	}
		
	
	public static int[] setGloomyBassScale(){
		bassscale[0] = 40;
		for(i = 1; i < bassscale.length; i++){
			if((i == 2) || (i == 5) || (i == 9) || (i == 12)){
				bassscale[i] = bassscale[i - 1] + 1;
			}else{
				bassscale[i] = bassscale[i - 1] + 2;
			}
		}
		return bassscale;
	}
	
	public static int[] setCheerfulGuitarScale(){
		guitarscale[0] = 48;
		for(i = 1; i < guitarscale.length; i++){
			if((i == 3) || (i == 7) || (i == 10) || (i == 14)){
				guitarscale[i] = guitarscale[i - 1] + 1;
			}else{
				guitarscale[i] = guitarscale[i - 1] + 2;
			}
		}
		return guitarscale;
	}
	
	public static int[] setGloomyGuitarScale(){
		guitarscale[0] = 40;
		for(i = 1; i < guitarscale.length; i++){
			if((i == 2) || (i == 5) || (i == 9) || (i == 12)){
				guitarscale[i] = guitarscale[i - 1] + 1;
			}else{
				guitarscale[i] = guitarscale[i - 1] + 2;
			}
		}
		return guitarscale;
	}
}



