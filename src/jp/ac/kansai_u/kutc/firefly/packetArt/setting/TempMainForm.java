package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import jp.ac.kansai_u.kutc.firefly.packetArt.Form;

public class TempMainForm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ConfigStatus();
		Form form = new Form("Configuration", SettingForm.class);
		form.setVisible(true);
		System.out.println("Closed.");
	}

}
