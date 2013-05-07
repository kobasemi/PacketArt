package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// TODO Layoutを整えなきゃ

/**
 * キーコンフィグに関するパネル
 * @author akasaka
 */
public class KeyConfigPanel extends JPanel implements KeyListener, ActionListener{
	final public String ARRPATH = new String("./resource/image/config/arrow/");
	private JRadioButton btnUp, btnDown, btnLeft, btnRight, btnLeftSpin, btnRightSpin;
	JLabel labelUp, labelDown, labelLeft, labelRight, labelLeftSpin, labelRightSpin;
	private JLabel[] labelArray;
	private JRadioButton[] btnArray;
	private String cmd;
	
	/**
	 * デフォルトコンストラクタ
	 * @param key（各キーの配列）
	 */
	KeyConfigPanel(char [] key){
		
		btnUp = new JRadioButton(new ImageIcon(ARRPATH + "arrUp.png"));
		btnDown = new JRadioButton(new ImageIcon(ARRPATH + "arrDown.png"));
		btnLeft = new JRadioButton(new ImageIcon(ARRPATH + "arrLeft.png"));
		btnRight = new JRadioButton(new ImageIcon(ARRPATH + "arrRight.png"));
		btnLeftSpin = new JRadioButton(new ImageIcon(ARRPATH + "arrLSpin.png"));
		btnRightSpin = new JRadioButton(new ImageIcon(ARRPATH + "arrRSpin.png"));
		btnArray = new JRadioButton[]{btnUp, btnDown, btnLeft, btnRight,
				btnLeftSpin, btnRightSpin};
		ButtonGroup btnArrow = new ButtonGroup();
		btnArrow.add(btnUp); btnArrow.add(btnDown); btnArrow.add(btnLeft);
		btnArrow.add(btnRight); btnArrow.add(btnLeftSpin); btnArrow.add(btnRightSpin);
		
		labelUp = new JLabel(String.valueOf(key[0]));
		labelDown = new JLabel(String.valueOf(key[1]));
		labelLeft = new JLabel(String.valueOf(key[2]));
		labelRight = new JLabel(String.valueOf(key[3]));
		labelLeftSpin = new JLabel(String.valueOf(key[4]));
		labelRightSpin = new JLabel(String.valueOf(key[5]));
		labelArray = new JLabel[]{labelUp, labelDown, labelLeft, labelRight,
				labelLeftSpin, labelRightSpin};
		
		btnUp.setActionCommand("Up");
		btnDown.setActionCommand("Down");
		btnLeft.setActionCommand("Left");
		btnRight.setActionCommand("Right");
		btnLeftSpin.setActionCommand("LeftSpin");
		btnRightSpin.setActionCommand("RightSpin");
		
		btnUp.setSelectedIcon(new ImageIcon(ARRPATH + "arrUpSelected.png"));
		btnDown.setSelectedIcon(new ImageIcon(ARRPATH + "arrDownSelected.png"));
		btnLeft.setSelectedIcon(new ImageIcon(ARRPATH + "arrLeftSelected.png"));
		btnRight.setSelectedIcon(new ImageIcon(ARRPATH + "arrRightSelected.png"));
		btnLeftSpin.setSelectedIcon(new ImageIcon(ARRPATH + "arrLSpinSelected.png"));
		btnRightSpin.setSelectedIcon(new ImageIcon(ARRPATH + "arrRSpinSelected.png"));
		for(int i=0; i<btnArray.length; i++){
			btnArray[i].setContentAreaFilled(true);
			btnArray[i].addActionListener(this);
			btnArray[i].addKeyListener(this);
		}
		for(int i=0; i<labelArray.length; i++){
			labelArray[i].setPreferredSize(new Dimension(50, 50));
			labelArray[i].setFont(new Font("Self", Font.PLAIN, 30));
			labelArray[i].setHorizontalAlignment(JLabel.CENTER);
			labelArray[i].setForeground(Color.white);
		}
		
		add(btnUp);
		add(labelUp);
		add(btnDown);
		add(labelDown);
		add(btnLeft);
		add(labelLeft);
		add(btnRight);
		add(labelRight);
		add(btnLeftSpin);
		add(labelLeftSpin);
		add(btnRightSpin);
		add(labelRightSpin);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		cmd = e.getActionCommand();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		int code = e.getKeyCode();
		
		// キー入力のうち，Space, Enter, Escape, BackSpace, Controlのキーコード，
		// そして未定義の文字を読み飛ばそう
		///////////////////////////////////////////////////////////////////
		//TODO 後々，シフト，コントロールなどをマスクする方に変えようか
		////////////////////////////////////////////////////////////////////
		if( code == KeyEvent.VK_SPACE ||
			code == KeyEvent.VK_ENTER ||
			code == KeyEvent.VK_ESCAPE ||
			code == KeyEvent.VK_BACK_SPACE ||
			code == KeyEvent.VK_CONTROL ||
			key == KeyEvent.CHAR_UNDEFINED) return;
		
		// キーの重複を探索
		int idx = KeyInputLinerSearch(key);
		if(idx != -1)
			// キーが衝突したら，今からインプットするラベルの文字を
			// キーが衝突したラベルに挿入．
			InsertKeyConflictLabel(idx);
		
		// 各ラベルに文字をセットする
		if(cmd == "Up")
			labelUp.setText(String.valueOf(key));
		else if(cmd == "Down")
			labelDown.setText(String.valueOf(key));
		else if(cmd == "Left")
			labelLeft.setText(String.valueOf(key));
		else if(cmd == "Right")
			labelRight.setText(String.valueOf(key));
		else if(cmd == "LeftSpin")
			labelLeftSpin.setText(String.valueOf(key));
		else if(cmd == "RightSpin")
			labelRightSpin.setText(String.valueOf(key));
	}
	
	/**
	 * 入力キーの重複を探査する
	 * @param 入力キー
	 * @return 衝突したラベルのインデックス，-1の場合衝突なし
	 */
	private int KeyInputLinerSearch(char key){
		for(int i=0; i<labelArray.length; i++)
			if(labelArray[i].getText().equals(String.valueOf(key)))
				return i;
		return -1;
	}
	
	/**
	 * 衝突があったラベルに，今からインプットするラベルのキーを挿入する
	 * @param 衝突したインデックスナンバー
	 */
	private void InsertKeyConflictLabel(int idx){
		if(cmd == "Up")
			labelArray[idx].setText(labelUp.getText());
		else if(cmd == "Down")
			labelArray[idx].setText(labelDown.getText());
		else if(cmd == "Left")
			labelArray[idx].setText(labelLeft.getText());
		else if(cmd == "Right")
			labelArray[idx].setText(labelRight.getText());
		else if(cmd == "LeftSpin")
			labelArray[idx].setText(labelLeftSpin.getText());
		else if(cmd == "RightSpin")
			labelArray[idx].setText(labelRightSpin.getText());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
