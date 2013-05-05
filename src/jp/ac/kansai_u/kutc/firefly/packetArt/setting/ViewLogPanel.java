package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * ログの表示設定に関するパネル
 * @author akasaka
 */
public class ViewLogPanel extends JPanel{
	final String IMGPATH = new String("./resource/image/config/");
	final String BTNPATH = new String(IMGPATH + "button/");
	private JRadioButton btnOn, btnOff;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のログ表示設定
	 */
	ViewLogPanel(boolean f){
		setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
		JLabel labelViewLog = new JLabel(new ImageIcon(IMGPATH + "labelLog.png"));
		btnOn = new JRadioButton(new ImageIcon(BTNPATH + "btnOn.png"));
		btnOn.setSelectedIcon(new ImageIcon(BTNPATH + "btnOnSelected.png"));
		btnOff = new JRadioButton(new ImageIcon(BTNPATH + "btnOff.png"));
		btnOff.setSelectedIcon(new ImageIcon(BTNPATH + "btnOffSelected.png"));
		ButtonGroup logViewGroup = new ButtonGroup();
		logViewGroup.add(btnOn); logViewGroup.add(btnOff);
		add(labelViewLog);
		add(btnOn);
		add(btnOff);
		
		if(f) btnOn.setSelected(true);
		else  btnOff.setSelected(true);
	}
	
	/**
	 * ログの表示設定を取得する
	 * @return ログ表示のオン・オフ
	 */
	public boolean getStatus(){
		return btnOn.isSelected();
	}
}
