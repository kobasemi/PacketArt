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
	final String IMGPATH = new String("./resource/image/config/");
	final String BTNPATH = new String(IMGPATH + "button/");
	private JRadioButton btnMino4, btnMino5, btnMinoBoth;	// 各コンポーネント
	
	/**
	 * コンストラクタ
	 * @param 初期化前のミノ個数の設定
	 */
	MinoPanel(byte b){
		setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
		JLabel labelMino = new JLabel(new ImageIcon(IMGPATH + "labelMino.png"));
		btnMino4 = new JRadioButton(new ImageIcon(BTNPATH + "btnMino4.png"));
		btnMino5 = new JRadioButton(new ImageIcon(BTNPATH + "btnMino5.png"));
		btnMinoBoth = new JRadioButton(new ImageIcon(BTNPATH + "btnMinoBoth.png"));
		ButtonGroup minoGroup = new ButtonGroup();
		minoGroup.add(btnMino4); minoGroup.add(btnMino5); minoGroup.add(btnMinoBoth);
		
		btnMino4.setSelectedIcon(new ImageIcon(BTNPATH + "btnMino4Selected.png"));
		btnMino5.setSelectedIcon(new ImageIcon(BTNPATH + "btnMino5Selected.png"));
		btnMinoBoth.setSelectedIcon(new ImageIcon(BTNPATH + "btnMinoBothSelected.png"));
		
		add(labelMino);
		add(btnMino4);
		add(btnMino5);
		add(btnMinoBoth);
		
		if     (b == ConfigStatus.MINO4)    btnMino4.setSelected(true);
		else if(b == ConfigStatus.MINO5)    btnMino5.setSelected(true);
		else if(b == ConfigStatus.MINOBOTH) btnMinoBoth.setSelected(true);
	}
	
	/**
	 * ミノの出現個数の設定を取得する
	 * @return ミノの出現個数（4つ, 5つ, 両方）
	 */
	public byte getStatus(){
		if     (btnMino4.isSelected())    return ConfigStatus.MINO4;
		else if(btnMino5.isSelected())    return ConfigStatus.MINO5;
		else if(btnMinoBoth.isSelected()) return ConfigStatus.MINOBOTH;
		return -1;
	}
}