package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

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
		panelMusic = new VolumePanel(stat.getVolMusic(), "���y�̉���");
		panelMusic.setBackground(Color.magenta);
		panelSound = new VolumePanel(stat.getVolSound(), "��ʉ��̉���");
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
