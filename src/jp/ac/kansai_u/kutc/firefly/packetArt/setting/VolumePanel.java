package edu.self.Config;
import java.awt.FlowLayout;

import javax.swing.*;

public class VolumePanel extends JPanel{
	final String IMGPATH = new String("./Resources/image/");
	private JRadioButton btnVolumeMute, btnVolumeLow, btnVolumeMed, btnVolumeHigh;
	
	VolumePanel(byte b, String label){
		//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		//ImageIcon icon4 = new ImageIcon(IMGPATH + "img4.png");
		
		JLabel labelVolume = new JLabel(label);//icon);
		btnVolumeMute = new JRadioButton("Mute");//icon4);
	    btnVolumeLow = new JRadioButton("Low");//icon4);
	    btnVolumeMed = new JRadioButton("Medium");//icon4);
	    btnVolumeHigh = new JRadioButton("High");//icon4);
	    ButtonGroup volumeGroup = new ButtonGroup();
	    volumeGroup.add(btnVolumeMute); volumeGroup.add(btnVolumeLow);
	    volumeGroup.add(btnVolumeMed); volumeGroup.add(btnVolumeHigh);
	    add(labelVolume);
	    add(btnVolumeMute);
	    add(btnVolumeLow);
	    add(btnVolumeMed);
	    add(btnVolumeHigh);
	    
	    if(b == 0)      btnVolumeMute.setSelected(true);
	    else if(b == 1) btnVolumeLow.setSelected(true);
	    else if(b == 2) btnVolumeMed.setSelected(true);
	    else btnVolumeHigh.setSelected(true);
	}
	
	public byte getStatus(){
		if(btnVolumeMute.isSelected()) return (byte)0;
	    else if(btnVolumeLow.isSelected()) return (byte)1; 
	    else if(btnVolumeMed.isSelected()) return (byte)2;
	    else return (byte)3;
	}
}
