package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * ログの表示設定に関するパネル
 * @author akasaka
 */
public class ViewLogPanel extends JPanel{
	final String IMGPATH = new String("./Resources/image/");
	private JRadioButton btnOn, btnOff;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のログ表示設定
	 */
	ViewLogPanel(boolean f){
		//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		//ImageIcon icon2 = new ImageIcon(IMGPATH + "img2.png");
		setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		JLabel labelViewLog = new JLabel("ログの表示");//icon);
		btnOn = new JRadioButton("On");//icon2);
		btnOff = new JRadioButton("Off");//icon2);
		ButtonGroup logViewGroup = new ButtonGroup();
		logViewGroup.add(btnOn); logViewGroup.add(btnOff);
		add(labelViewLog);
		add(btnOn);
		add(btnOff);
		
		if(f) btnOn.setSelected(true);
		else btnOff.setSelected(true);
	}
	
	/**
	 * ログの表示設定を取得する
	 * @return ログ表示のオン・オフ
	 */
	public boolean getStatus(){
		return btnOn.isSelected();
	}
}
