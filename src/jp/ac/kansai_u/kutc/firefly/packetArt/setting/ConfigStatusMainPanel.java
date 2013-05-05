package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.Color;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * キーコンフィグを除く各設定項目のパネル
 * @author akasaka
 */
public class ConfigStatusMainPanel extends JPanel{
	final public String IMGPATH = new String("./resource/image/config/");
	ImageIcon icon;
	ViewLogPanel panelViewLog;
	MinoPanel panelMino;
	MusicVolumePanel panelMusic;
	SoundVolumePanel panelSound;
	DifficultyPanel panelDifficulty;
	
	/**
	 * コンストラクタ
	 */
	ConfigStatusMainPanel(){
		this.setOpaque(false);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panelViewLog = new ViewLogPanel(ConfigStatus.isViewLog());
		panelViewLog.setBackground(Color.yellow);
		panelViewLog.setOpaque(false);
		panelMino = new MinoPanel(ConfigStatus.getMino());
		panelMino.setBackground(Color.cyan);
		panelMusic = new MusicVolumePanel(ConfigStatus.getVolMusic());
		panelMusic.setBackground(Color.magenta);
		panelSound = new SoundVolumePanel(ConfigStatus.getVolSound());
		panelSound.setBackground(Color.gray);
		panelDifficulty = new DifficultyPanel(ConfigStatus.getDifficulty());
		panelDifficulty.setBackground(Color.blue);
		
		add(panelViewLog);
		add(panelMino);
		add(panelMusic);
		add(panelSound);
		add(panelDifficulty);
	}
}
