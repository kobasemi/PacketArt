package jp.ac.kansai_u.kutc.firefly.packetArt;
//import java.util.Arrays;

/**
 * アプリケーションのエントリーポイントを含むクラスです。
 * @author midolin
 *
 */
public class Main {
	/**
	 * アプリケーションのエントリーポイントです。
	 * @param args
	 */
	public static void main(String [] args) {
		//new Form("Entry", EntryForm.class).show();
		Form form = new Form("Entry", EntryForm.class);
		//if(Arrays.asList(args).contains("-verbose"))
		form.setVisible(true);
		//frame.show();
		System.out.println("Closed.");
	}
}
