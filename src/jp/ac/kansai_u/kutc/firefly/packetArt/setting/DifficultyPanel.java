package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.FlowLayout;

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
	private JRadioButton btnDifficultyStatic, btnDifficultyDynamic, btnDifficultyAuto;
	
	/**
	 * コンストラクタ
	 * @param 初期化前の難易度の設定
	 */
	DifficultyPanel(byte b){
		setLayout(new FlowLayout(FlowLayout.LEFT, ConfigInfo.HGAP, 0));
		setOpaque(false);
		
		JLabel labelDifficulty = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "labelDifficulty.png")));
		btnDifficultyStatic  = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnStatic.png")));
	    btnDifficultyDynamic = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnDynamic.png")));
	    btnDifficultyAuto    = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnAuto.png")));
	    
	    ButtonGroup difficultyGroup = new ButtonGroup();
	    difficultyGroup.add(btnDifficultyStatic); difficultyGroup.add(btnDifficultyDynamic);
	    difficultyGroup.add(btnDifficultyAuto);
	    
	    btnDifficultyStatic .setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnStaticSelected.png")));
	    btnDifficultyDynamic.setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnDynamicSelected.png")));
	    btnDifficultyAuto   .setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnAutoSelected.png")));
	    
	    btnDifficultyStatic .setContentAreaFilled(false);
	    btnDifficultyDynamic.setContentAreaFilled(false);
	    btnDifficultyAuto   .setContentAreaFilled(false);
	    
	    add(labelDifficulty);
		add(btnDifficultyStatic);
		add(btnDifficultyDynamic);
		add(btnDifficultyAuto);
		
		if     (b == ConfigInfo.STATIC)  btnDifficultyStatic .setSelected(true);
		else if(b == ConfigInfo.DYNAMIC) btnDifficultyDynamic.setSelected(true);
		else if(b == ConfigInfo.AUTO)    btnDifficultyAuto   .setSelected(true);
	}
	
	/**
	 * 難易度の設定を取得する
	 * @return 難易度（静的, 動的, 自動）
	 */
	public byte getStatus(){
		if     (btnDifficultyStatic .isSelected()) return ConfigInfo.STATIC;
		else if(btnDifficultyDynamic.isSelected()) return ConfigInfo.DYNAMIC;
		else if(btnDifficultyAuto   .isSelected()) return ConfigInfo.AUTO;
		return -1;
	}
}
