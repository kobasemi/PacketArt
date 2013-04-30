package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import jp.ac.kansai_u.kutc.firefly.packetArt.Form;
import jp.ac.kansai_u.kutc.firefly.packetArt.title.TitleForm;

public class TempMainForm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ConfigStatus madoka = new ConfigStatus();
//		ConfigFrame homura = new ConfigFrame(madoka);
//		
//		homura.setVisible(true);
//		madoka.printStatus();
		Form form = new Form("Configuration", SettingForm.class);
		form.setVisible(true);
		System.out.println("Closed.");
	}

}
