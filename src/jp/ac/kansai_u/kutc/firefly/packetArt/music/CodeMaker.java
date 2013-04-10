
//コロブチカ風BGMのコード生成を担当するクラス．
public class CodeMaker {
	public static void main(String[] args) {
	}
	
	public static String[] koroCodeMaker(){
		
		//配列の長さは24。1/2小節ごとにコードを使用。
		String[] koroCode = {"Em", "B7", "Em", "B7", "B7", "B7", "Em", "Em", "Am", "Am", "Em", "Em"
				, "B7", "B7", "Em", "Em", "Am", "Am", "Em", "Em", "B7", "B7", "Em", "Em"};
		return koroCode;
	}
}
