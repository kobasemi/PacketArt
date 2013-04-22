package edu.self.Config;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

public class ViewLogPanel extends JPanel{
	final String IMGPATH = new String("./Resources/image/");
	private JRadioButton btnOn, btnOff;
	
	ViewLogPanel(boolean f){
		//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		//ImageIcon icon2 = new ImageIcon(IMGPATH + "img2.png");
		setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
		JLabel labelViewLog = new JLabel("ÉçÉOÇÃï\é¶");//icon);
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
	
	public boolean getStatus(){
		return btnOn.isSelected();
	}
}
