package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.util.Random;

public class MelodyAlgorithm {
	public static int[] defCheerfulAlgorithm(int length){
		String[] code = CodeMaker.setCheerfulCode(length);
		int[] cheerfulmelody = new int[code.length];
		int[] melodyscale = ScaleMaker.setCheerfulMelodyScale();
		
		int[] listc = {melodyscale[0], melodyscale[2], melodyscale[4]};
		int[] listf = {melodyscale[0], melodyscale[3], melodyscale[5]};
		int[] listg7 = {melodyscale[1], melodyscale[3], melodyscale[4]};
		
		for(int i = 0; i < length; i++){
			Random rnd = new Random();
			int tmp = rnd.nextInt(3);
			
			if("C".equals(code[i])){
				cheerfulmelody[i] = listc[tmp];
			}else if("F".equals(code[i])){
				cheerfulmelody[i] = listf[tmp];
			}else if("G7".equals(code[i])){
				cheerfulmelody[i] = listg7[tmp];
			}
		}
		return cheerfulmelody;
	}
	
	public static int[] defGloomyAlgorithm(int length){
		String[] code = CodeMaker.setGloomyCode(length);
		int[] gloomymelody = new int[code.length];
		int[] melodyscale = ScaleMaker.setGloomyMelodyScale();
		
		int[] listem = {melodyscale[0], melodyscale[2], melodyscale[4]};
		int[] listam = {melodyscale[3], melodyscale[5], melodyscale[7]};
		int[] listb7 = {melodyscale[1], melodyscale[3], melodyscale[4]};
		
		for(int i = 0; i < length; i++){
			Random rnd = new Random();
			int tmp = rnd.nextInt(3);
			
			if("Em".equals(code[i])){
				gloomymelody[i] = listem[tmp];
			}else if("Am".equals(code[i])){
				gloomymelody[i] = listam[tmp];
			}else if("B7".equals(code[i])){
				gloomymelody[i] = listb7[tmp];
			}
		}
		return gloomymelody;
	}
}
