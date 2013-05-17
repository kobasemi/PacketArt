package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MelodyPanel extends JPanel{
	private JRadioButton btnLight, btnDark;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のメロディ設定
	 */
	MelodyPanel(boolean f){
		setLayout(new FlowLayout(FlowLayout.LEFT, ConfigInfo.HGAP, 0));
		setOpaque(false);
		
		JLabel labelViewLog = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "labelMelody.png")));
		btnLight = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnLight.png")));
		btnDark  = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnDark.png")));
		
		ButtonGroup melodyGroup = new ButtonGroup();
		melodyGroup.add(btnLight); melodyGroup.add(btnDark);
		
		btnLight.setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnLightSelected.png")));
		btnDark .setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnDarkSelected.png")));
		
		btnLight.setContentAreaFilled(false);
		btnDark .setContentAreaFilled(false);
		
		add(labelViewLog);
		add(btnLight);
		add(btnDark);
		
		if(f) btnLight.setSelected(true);
		else  btnDark .setSelected(true);
	}
	
	/**
	 * メロディ調の設定を取得する
	 * @return メロディ調（明るめ，暗め）
	 */
	public boolean getStatus(){ return btnLight.isSelected(); }
}
