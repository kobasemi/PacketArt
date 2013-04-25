package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * 難易度に関するパネル
 * @author akasaka
 */
public class DifficultyPanel extends JPanel{
	final public String IMGPATH = new String("./Resources/image/");
	private JRadioButton btnDifficultyStatic, btnDifficultyDynamic, btnDifficultyAuto;
	
	/**
	 * コンストラクタ
	 * @param 初期化前の難易度の設定
	 */
	DifficultyPanel(byte b){
		ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		
		JLabel labelDifficulty = new JLabel("難易度");//icon);
		btnDifficultyStatic = new JRadioButton("静的");
	    btnDifficultyDynamic = new JRadioButton("動的");
	    btnDifficultyAuto = new JRadioButton("自動");
	    ButtonGroup difficultyGroup = new ButtonGroup();
	    difficultyGroup.add(btnDifficultyStatic); difficultyGroup.add(btnDifficultyDynamic);
	    difficultyGroup.add(btnDifficultyAuto);
		add(labelDifficulty);
		add(btnDifficultyStatic);
		add(btnDifficultyDynamic);
		add(btnDifficultyAuto);
		
		if(b ==0)       btnDifficultyStatic.setSelected(true);
		else if(b == 1) btnDifficultyDynamic.setSelected(true);
		else            btnDifficultyAuto.setSelected(true);
	}
	
	/**
	 * 難易度の設定を取得する
	 * @return 難易度（0_静的, 1_動的, 2_自動）
	 */
	public byte getStatus(){
		if(btnDifficultyStatic.isSelected()) return (byte)0;
		else if(btnDifficultyDynamic.isSelected()) return (byte)1;
		else return (byte)2;
	}
}
