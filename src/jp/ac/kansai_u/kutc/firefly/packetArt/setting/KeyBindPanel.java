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
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// TODO Layoutを整えなきゃ

/**
 * キーコンフィグに関するパネル
 * @author akasaka
 */
public class KeyBindPanel extends JPanel implements KeyListener, ActionListener{
	final public String ARRPATH = new String("./resource/image/config/arrow/");
	private JButton btnKeyDefault, btnKeyGamer, btnKeyVim;
	JLabel labelUp, labelDown, labelLeft, labelRight, labelLSpin, labelRSpin;
	JLabel labelUpCode, labelDownCode, labelLeftCode, labelRightCode,
			labelLSpinCode, labelRSpinCode;
	private JLabel[] labelArray;
	private String cmd;
	
	final private int DEFAULTKEYCODE[] = new int[]{KeyEvent.VK_LEFT, 38, 39, 40, 32, 0};
	final private int GAMERKEYCODE[] = new int[]{65, 87, 68, 83, 74, 75};
	final private int VIMKEYCODE[] = new int[]{72, 75, 76, 74, 65, 83};
	
	/**
	 * デフォルトコンストラクタ
	 * @param key（各キーの配列）
	 */
	KeyBindPanel(int [] key){
		btnKeyDefault = new JButton("Defalut");
		btnKeyGamer = new JButton("Gamer");
		btnKeyVim = new JButton("Vim");
		
		labelUp = new JLabel(new ImageIcon(ARRPATH + "arrUp.png"));
		labelDown = new JLabel(new ImageIcon(ARRPATH + "arrDown.png"));
		labelLeft = new JLabel(new ImageIcon(ARRPATH + "arrLeft.png"));
		labelRight = new JLabel(new ImageIcon(ARRPATH + "arrRight.png"));
		labelLSpin = new JLabel(new ImageIcon(ARRPATH + "arrLSpin.png"));
		labelRSpin = new JLabel(new ImageIcon(ARRPATH + "arrRSpin.png"));
		labelArray = new JLabel[]{labelUp, labelDown, labelLeft, labelRight,
				labelLSpin, labelRSpin};
		
		labelUpCode = new JLabel(KeyEvent.getKeyText(DEFAULTKEYCODE[0]));
		labelDownCode = new JLabel(KeyEvent.getKeyText(DEFAULTKEYCODE[1]));
		labelLeftCode = new JLabel(KeyEvent.getKeyText(DEFAULTKEYCODE[2]));
		labelRightCode = new JLabel(KeyEvent.getKeyText(DEFAULTKEYCODE[3]));
		labelLSpinCode = new JLabel(KeyEvent.getKeyText(DEFAULTKEYCODE[4]));
		labelRSpinCode = new JLabel(KeyEvent.getKeyText(DEFAULTKEYCODE[5]));
		
		add(btnKeyDefault);
		add(btnKeyGamer);
		add(btnKeyVim);
		add(labelUp);
		add(labelDown);
		add(labelLeft);
		add(labelRight);
		add(labelLSpin);
		add(labelRSpin);
		add(labelUpCode);
		add(labelDownCode);
		add(labelLeftCode);
		add(labelRightCode);
		add(labelLSpinCode);
		add(labelRSpinCode);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		cmd = e.getActionCommand();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char key = e.getKeyChar();
		int code = e.getKeyCode();
		
		// 各ラベルに文字をセットする
		if(cmd == "Up")
			labelUp.setText(String.valueOf(key));
		else if(cmd == "Down")
			labelDown.setText(String.valueOf(key));
		else if(cmd == "Left")
			labelLeft.setText(String.valueOf(key));
		else if(cmd == "Right")
			labelRight.setText(String.valueOf(key));
		else if(cmd == "LSpin")
			labelLSpin.setText(String.valueOf(key));
		else if(cmd == "RSpin")
			labelRSpin.setText(String.valueOf(key));
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
