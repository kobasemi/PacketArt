package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * キーコンフィグを除く各設定項目のパネル
 * @author akasaka
 */
public class ConfigStatusMainPanel extends JPanel{
	//final public String IMGPATH = new String("./Resources/image/");
	//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
	
	ViewLogPanel panelViewLog;
	MinoPanel panelMino;
	MusicVolumePanel panelMusic;
	SoundVolumePanel panelSound;
	DifficultyPanel panelDifficulty;
	
	/**
	 * コンストラクタ
	 */
	ConfigStatusMainPanel(){
		this.setBackground(Color.blue);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panelViewLog = new ViewLogPanel(ConfigStatus.isViewLog());
		panelViewLog.setBackground(Color.yellow);
		panelMino = new MinoPanel(ConfigStatus.getMino());
		panelMino.setBackground(Color.cyan);
		panelMusic = new MusicVolumePanel(ConfigStatus.getVolMusic());
		panelMusic.setBackground(Color.magenta);
		panelSound = new SoundVolumePanel(ConfigStatus.getVolSound());
		panelSound.setBackground(Color.gray);
		panelDifficulty = new DifficultyPanel(ConfigStatus.getDifficulty());
		panelDifficulty.setBackground(Color.white);
		
		add(panelViewLog);
		add(panelMino);
		add(panelMusic);
		add(panelSound);
		add(panelDifficulty);
	}
}
