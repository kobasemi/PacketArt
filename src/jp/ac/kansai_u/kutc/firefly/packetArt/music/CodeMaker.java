package jp.ac.kansai_u.kutc.firefly.packetArt.music;

/**
 * コード進行を定義するクラスです。
 * MelodyAlgorithmとAccompanimentMakerからのみ呼び出されます。
 * @author Lisa
 */
public class CodeMaker {

	/**
	 * 明るいコード進行を定義します。
	 * @param length
	 * @return cheerfulcode
	 */
	public static String[] setCheerfulCode(int length){
		String[] cheerfulcode = new String[length];
		String[] codesource = {"C", "C", "C", "C", "F", "F", "G7", "G7",
							   "C", "C", "C", "C", "F", "F", "G7", "G7",
							   "C", "C", "C", "C", "F", "F", "G7", "G7"};
		int saya = 0;
		// 指定の曲の長さに対応させる。
		for(int i = 0; i < length; i++){
			cheerfulcode[i] = codesource[saya];
			saya++;
			if(saya == 24){
				saya = 0;
			}
		}
		return cheerfulcode;
	}
	
	/**
	 * 暗いコード進行を定義します。
	 * @param length
	 * @return gloomycode
	 */
	public static String[] setGloomyCode(int length){
		String[] gloomycode = new String[length];
		String[] codesource = {"Em", "B7", "Em", "B7", "B7", "B7", "Em", "Em",
							   "Am", "Am", "Em", "Em", "B7", "B7", "Em", "Em",
							   "Am", "Am", "Em", "Em", "B7", "B7", "Em", "Em"};
		int saya = 0;
		for(int i = 0; i < length; i++){
			gloomycode[i] = codesource[saya];
			saya++;
			if(saya == 24){
				saya = 0;
			}
		}
		return gloomycode;
	}
}
