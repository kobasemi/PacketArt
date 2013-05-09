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
		panelViewLog.setOpaque(false);
		panelMino = new MinoPanel(ConfigStatus.getMino());
		panelMino.setOpaque(false);
		panelMusic = new MusicVolumePanel(ConfigStatus.getVolMusic());
		panelMusic.setOpaque(false);
		panelSound = new SoundVolumePanel(ConfigStatus.getVolSound());
		panelSound.setOpaque(false);
		panelDifficulty = new DifficultyPanel(ConfigStatus.getDifficulty());
		panelDifficulty.setOpaque(false);
		
		add(panelViewLog);
		add(panelMino);
		add(panelMusic);
		add(panelSound);
		add(panelDifficulty);
	}
}
