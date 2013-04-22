package edu.self.Config;

public class TempMainForm {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		ConfigStatus madoka = new ConfigStatus();
		ConfigFrame homura = new ConfigFrame(madoka);
		
		homura.setVisible(true);
		madoka.printStatus();
	}

}
