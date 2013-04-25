package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MinoPanel extends JPanel{
	final String IMGPATH = new String("./Resources/image/");
	private JRadioButton btnMino4, btnMino5, btnMinoBoth;
	
	MinoPanel(byte b){
		//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		//ImageIcon icon3 = new ImageIcon(IMGPATH + "img3.png");
		
		JLabel labelMino = new JLabel("ミノの出現設定");//icon);
		btnMino4 = new JRadioButton("4");//icon3);
	    btnMino5 = new JRadioButton("5");//icon3);
	    btnMinoBoth = new JRadioButton("Both");//icon3);
	    ButtonGroup minoGroup = new ButtonGroup();
	    minoGroup.add(btnMino4); minoGroup.add(btnMino5); minoGroup.add(btnMinoBoth);
		add(labelMino);
		add(btnMino4);
		add(btnMino5);
		add(btnMinoBoth);
		
		if(b == 0) btnMino4.setSelected(true);
		else if(b == 1) btnMino5.setSelected(true);
		else btnMinoBoth.setSelected(true);
	}
	
	public byte getStatus(){
		if(btnMino4.isSelected()) return (byte)0;
		else if(btnMino5.isSelected()) return (byte)1;
		else return (byte)2;
	}
}
