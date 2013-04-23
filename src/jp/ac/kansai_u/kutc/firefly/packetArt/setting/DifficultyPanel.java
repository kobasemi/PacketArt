package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class DifficultyPanel extends JPanel{
	final public String IMGPATH = new String("./Resources/image/");
	
	private JRadioButton btnDifficultyStatic, btnDifficultyDynamic, btnDifficultyAuto;
	DifficultyPanel(byte b){
		ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		
		JLabel labelDifficulty = new JLabel("��Փx");//icon);
		btnDifficultyStatic = new JRadioButton("�ÓI");
	    btnDifficultyDynamic = new JRadioButton("���I");
	    btnDifficultyAuto = new JRadioButton("����");
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
