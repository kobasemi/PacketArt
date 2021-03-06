package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * ミノの出現設定に関するパネル
 * @author akasaka
 */
public class MinoPanel extends JPanel{
	private JRadioButton btnMino4, btnMino5, btnMinoBoth;	// 各コンポーネント
	
	/**
	 * コンストラクタ
	 * @param 初期化前のミノ個数の設定
	 */
	MinoPanel(MinoType m){
		setLayout(new FlowLayout(FlowLayout.LEFT, ConfigInfo.HGAP, 0));
		setOpaque(false);
		
		JLabel labelMino = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "labelMino.png")));
		btnMino4    = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnMino4.png")));
		btnMino5    = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnMino5.png")));
		btnMinoBoth = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnMinoBoth.png")));
		
		ButtonGroup minoGroup = new ButtonGroup();
		minoGroup.add(btnMino4); minoGroup.add(btnMino5); minoGroup.add(btnMinoBoth);
		
		btnMino4   .setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnMino4Selected.png")));
		btnMino5   .setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnMino5Selected.png")));
		btnMinoBoth.setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.BTNPATH + "btnMinoBothSelected.png")));
		
		btnMino4   .setContentAreaFilled(false);
		btnMino5   .setContentAreaFilled(false);
		btnMinoBoth.setContentAreaFilled(false);
		
		add(labelMino);
		add(btnMino4);
		add(btnMino5);
		add(btnMinoBoth);
		
		if     (m == MinoType.values()[ConfigInfo.MINO4]   ) btnMino4   .setSelected(true);
		else if(m == MinoType.values()[ConfigInfo.MINO5]   ) btnMino5   .setSelected(true);
		else if(m == MinoType.values()[ConfigInfo.MINOBOTH]) btnMinoBoth.setSelected(true);
	}
	
	/**
	 * ミノの出現個数の設定を取得する
	 * @return ミノの出現個数（4つ, 5つ, 両方）
	 */
	public MinoType getStatus(){
		if     (btnMino4   .isSelected()) return MinoType.values()[ConfigInfo.MINO4];
		else if(btnMino5   .isSelected()) return MinoType.values()[ConfigInfo.MINO5];
		else if(btnMinoBoth.isSelected()) return MinoType.values()[ConfigInfo.MINOBOTH];
		return null;
	}
}
