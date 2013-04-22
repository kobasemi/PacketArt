package edu.self.Config;
import java.awt.*;
import javax.swing.*;

public class ConfigStatusMainPanel extends JPanel{
	//final public String IMGPATH = new String("./Resources/image/");
	//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
	
	ViewLogPanel panelViewLog;
	MinoPanel panelMino;
	VolumePanel panelMusic, panelSound;
	DifficultyPanel panelDifficulty;
	
	ConfigStatusMainPanel(ConfigStatus stat){
		this.setBackground(Color.blue);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
		panelViewLog = new ViewLogPanel(stat.isViewLog());
		panelViewLog.setBackground(Color.yellow);
		panelMino = new MinoPanel(stat.getMino());
		panelMino.setBackground(Color.cyan);
		panelMusic = new VolumePanel(stat.getVolMusic(), "‰¹Šy‚Ì‰¹—Ê");
		panelMusic.setBackground(Color.magenta);
		panelSound = new VolumePanel(stat.getVolSound(), "Œø‰Ê‰¹‚Ì‰¹—Ê");
		panelSound.setBackground(Color.gray);
		panelDifficulty = new DifficultyPanel(stat.getDifficulty());
		panelDifficulty.setBackground(Color.white);
		
		add(panelViewLog);
		add(panelMino);
		add(panelMusic);
		add(panelSound);
		add(panelDifficulty);
	}	
}
