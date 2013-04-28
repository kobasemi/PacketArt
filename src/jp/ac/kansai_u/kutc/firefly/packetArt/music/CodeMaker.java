package jp.ac.kansai_u.kutc.firefly.packetArt.music;


/**
 * ゲーム中BGMに使用されるコードが入っているだけのクラスです。
 * 最終的にはどこかのクラスに統合するかもしれません。
 * @author Lisa
 *
 */
public class CodeMaker {
	
	/**
	 * ゲーム中BGMに使用されるコードが入っているだけのメソッドです。
	 * MelodyAlgorithmからのみ呼び出されます。
	 * @return Code[]
	 */
	public static String[] codeMaker(){
		
		//配列の長さは24。1/2小節ごとにコードを使用。
		String[] Code = {"Em", "B7", "Em", "B7", "B7", "B7", "Em", "Em", "Am", "Am", "Em", "Em"
				, "B7", "B7", "Em", "Em", "Am", "Am", "Em", "Em", "B7", "B7", "Em", "Em"};
		return Code;
	}
}
