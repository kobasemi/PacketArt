package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MelodyPanel extends JPanel{
	final String BTNPATH = new String(ConfigInfo.IMGPATH + "button/");
	private JRadioButton btnLight, btnDark;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のメロディ設定
	 */
	MelodyPanel(boolean f){
		setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
		setOpaque(false);
		
		JLabel labelViewLog = new JLabel(new ImageIcon(ConfigInfo.IMGPATH + "labelMelody.png"));
		btnLight = new JRadioButton(new ImageIcon(BTNPATH + "btnLight.png"));
		btnDark  = new JRadioButton(new ImageIcon(BTNPATH + "btnDark.png"));
		
		btnLight.setSelectedIcon(new ImageIcon(BTNPATH + "btnLightSelected.png"));
		btnDark .setSelectedIcon(new ImageIcon(BTNPATH + "btnDarkSelected.png"));
		btnLight.setContentAreaFilled(false);
		btnDark .setContentAreaFilled(false);
		
		ButtonGroup melodyGroup = new ButtonGroup();
		melodyGroup.add(btnLight); melodyGroup.add(btnDark);
		add(labelViewLog);
		add(btnLight);
		add(btnDark);
		
		if(f) btnLight.setSelected(true);
		else  btnDark.setSelected(true);
	}
	
	/**
	 * メロディ調の設定を取得する
	 * @return メロディ調（明るめ，暗め）
	 */
	public boolean getStatus(){ return btnLight.isSelected(); }
}
