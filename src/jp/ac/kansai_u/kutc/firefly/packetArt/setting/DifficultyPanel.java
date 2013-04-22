package edu.self.Config;
import javax.swing.*;

public class DifficultyPanel extends JPanel{
	final public String IMGPATH = new String("./Resources/image/");
	
	private JRadioButton btnDifficultyStatic, btnDifficultyDynamic, btnDifficultyAuto;
	DifficultyPanel(byte b){
		ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		
		JLabel labelDifficulty = new JLabel("ìÔà’ìx");//icon);
		btnDifficultyStatic = new JRadioButton("ê√ìI");
	    btnDifficultyDynamic = new JRadioButton("ìÆìI");
	    btnDifficultyAuto = new JRadioButton("é©ìÆ");
	    ButtonGroup difficultyGroup = new ButtonGroup();
	    difficultyGroup.add(btnDifficultyStatic); difficultyGroup.add(btnDifficultyDynamic);
	    difficultyGroup.add(btnDifficultyAuto);
		add(labelDifficulty);
		add(btnDifficultyStatic);
		add(btnDifficultyDynamic);
		add(btnDifficultyAuto);
		
		if(b ==0)       btnDifficultyStatic.setSelected(true);
		else if(b == 1) btnDifficultyDynamic.setSelected(true);
		else            btnDifficultyAuto.setSelected(true);
	}
	
	public byte getStatus(){
		if(btnDifficultyStatic.isSelected()) return (byte)0;
		else if(btnDifficultyDynamic.isSelected()) return (byte)1;
		else return (byte)2;
	}
}
