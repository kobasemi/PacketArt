package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * キーコンフィグに関するパネル
 * @author akasaka
 */
public class KeyConfigPanel extends JPanel implements KeyListener, ActionListener{
	//final public String IMGPATH = new String("./Resources/image/");
	JButton btnUp, btnDown, btnLeft, btnRight, btnLeftSpin, btnRightSpin;
	JLabel labelUp, labelDown, labelLeft, labelRight, labelLeftSpin, labelRightSpin;
	String cmd;
	
	/**
	 * デフォルトコンストラクタ
	 * @param key（各キーの配列）
	 */
	KeyConfigPanel(char [] key){
		
		btnUp = new JButton("Up");
		btnDown = new JButton("Down");
		btnLeft = new JButton("Left");
		btnRight = new JButton("Right");
		btnLeftSpin = new JButton("LeftSpin");
		btnRightSpin = new JButton("RightSpin");
		
		labelUp = new JLabel(String.valueOf(key[0]));
		labelDown = new JLabel(String.valueOf(key[1]));
		labelLeft = new JLabel(String.valueOf(key[2]));
		labelRight = new JLabel(String.valueOf(key[3]));
		labelLeftSpin = new JLabel(String.valueOf(key[4]));
		labelRightSpin = new JLabel(String.valueOf(key[5]));
		
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
		
		btnUp.setActionCommand("Up");
		btnUp.addActionListener(this);
	    btnUp.addKeyListener(this);
	    btnDown.setActionCommand("Down");
		btnDown.addActionListener(this);
	    btnDown.addKeyListener(this);
	    btnLeft.setActionCommand("Left");
		btnLeft.addActionListener(this);
	    btnLeft.addKeyListener(this);
	    btnRight.setActionCommand("Right");
		btnRight.addActionListener(this);
	    btnRight.addKeyListener(this);
	    btnLeftSpin.setActionCommand("LeftSpin");
		btnLeftSpin.addActionListener(this);
	    btnLeftSpin.addKeyListener(this);
	    btnRightSpin.setActionCommand("RightSpin");
		btnRightSpin.addActionListener(this);
	    btnRightSpin.addKeyListener(this);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(cmd == "Up")
			labelUp.setText(String.valueOf(e.getKeyCode()));
		else if(cmd == "Down")
			labelDown.setText(String.valueOf(e.getKeyCode()));
		else if(cmd == "Left")
			labelLeft.setText(String.valueOf(e.getKeyCode()));
		else if(cmd == "Right")
			labelRight.setText(String.valueOf(e.getKeyCode()));
		else if(cmd == "LeftSpin")
			labelLeftSpin.setText(String.valueOf(e.getKeyCode()));
		else if(cmd == "RightSpin")
			labelRightSpin.setText(String.valueOf(e.getKeyCode()));
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void actionPerformed(ActionEvent e) {
		cmd = e.getActionCommand();
	}
}
