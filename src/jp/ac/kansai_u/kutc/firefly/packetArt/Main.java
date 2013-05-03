package jp.ac.kansai_u.kutc.firefly.packetArt;
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.ConfigStatus;
//import java.util.Arrays;
import jp.ac.kansai_u.kutc.firefly.packetArt.title.TitleForm;

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
		new ConfigStatus();  //Configurationの初期化
		//new Form("Title", EntryForm.class).show();
		Form form = new Form("Title", TitleForm.class);
		//if(Arrays.asList(args).contains("-verbose"))
		form.setVisible(true);
		//frame.show();
		System.out.println("Closed.");
	}
}
