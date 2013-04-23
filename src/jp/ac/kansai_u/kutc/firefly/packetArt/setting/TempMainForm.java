package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

public class TempMainForm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		ConfigStatus madoka = new ConfigStatus();
		ConfigFrame homura = new ConfigFrame(madoka);
		
		homura.setVisible(true);
		madoka.printStatus();
	}

}
