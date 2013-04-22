package edu.self.Config;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyConfigPanel extends JPanel {//implements KeyListener{
	//final public String IMGPATH = new String("./Resources/image/");
	JButton btnUp, btnDown, btnLeft, btnRight, btnLeftSpin, btnRightSpin;
	
	KeyConfigPanel(char [] key){
		
		btnUp = new JButton("UP");
		btnDown = new JButton("Down");
		btnLeft = new JButton("Left");
		btnRight = new JButton("Right");
		btnLeftSpin = new JButton("LeftSpin");
		btnRightSpin = new JButton("RightSpin");
		
		btnUp.setPreferredSize(new Dimension(200, 50));
		
		JLabel labelUp = new JLabel(String.valueOf(key[0]));
		JLabel labelDown = new JLabel(String.valueOf(key[1]));
		JLabel labelLeft = new JLabel(String.valueOf(key[2]));
		JLabel labelRight = new JLabel(String.valueOf(key[3]));
		JLabel labelLeftSpin = new JLabel(String.valueOf(key[4]));
		JLabel labelRightSpin = new JLabel(String.valueOf(key[5]));
		
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
}
