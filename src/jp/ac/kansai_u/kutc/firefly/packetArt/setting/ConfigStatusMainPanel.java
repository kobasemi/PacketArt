package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

/**
 * キーコンフィグを除く各設定項目のパネル
 * @author akasaka
 */
public class ConfigStatusMainPanel extends JPanel{
	ViewLogPanel     panelViewLog;
	MinoPanel        panelMino;
	MelodyPanel      panelMelody;
	MusicVolumePanel panelMusicVolume;
	SoundVolumePanel panelSoundVolume;
	DifficultyPanel  panelDifficulty;
	
	/**
	 * コンストラクタ
	 */
	ConfigStatusMainPanel(){
		setBounds(30, 130, 540, 250);
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		panelViewLog     = new ViewLogPanel(ConfigStatus.isViewLog());
		panelMino        = new MinoPanel(ConfigStatus.getMino());
//		panelMelody      = new MelodyPanel(ConfigStatus.isMelody());
		panelMusicVolume = new MusicVolumePanel(ConfigStatus.getVolMusic());
		panelSoundVolume = new SoundVolumePanel(ConfigStatus.getVolSound());
		panelDifficulty  = new DifficultyPanel(ConfigStatus.getDifficulty());
		
		add(panelViewLog);
		add(panelMino);
//		add(panelMelody);
		add(panelMusicVolume);
		add(panelSoundVolume);
		add(panelDifficulty);
	}
}
