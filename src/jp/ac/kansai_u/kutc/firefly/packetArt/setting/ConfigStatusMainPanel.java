package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

/**
 * キーコンフィグを除く各設定項目のパネル
 * @author akasaka
 */
public class ConfigStatusMainPanel extends JPanel{
	//各設定項目のパネル
	ViewLogPanel     panelViewLog;
	MinoPanel        panelMino;
	MelodyPanel      panelMelody;
	MusicVolumePanel panelMusicVolume;
	SoundVolumePanel panelSoundVolume;
	DifficultyPanel  panelDifficulty;
	KeyBindPanel     panelKeyBind;
	
	/**
	 * コンストラクタ
	 */
	ConfigStatusMainPanel(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);
		
		panelViewLog     = new ViewLogPanel    (ConfigStatus.isViewLog());
		panelMino        = new MinoPanel       (ConfigStatus.getMino());
		panelMelody      = new MelodyPanel     (ConfigStatus.isMelody());
		panelMusicVolume = new MusicVolumePanel(ConfigStatus.getVolMusic());
		panelSoundVolume = new SoundVolumePanel(ConfigStatus.getVolSound());
		panelDifficulty  = new DifficultyPanel (ConfigStatus.getDifficulty());
		panelKeyBind     = new KeyBindPanel    (ConfigStatus.getKeyBind());
		
		add(panelViewLog);
		add(panelMino);
		add(panelMelody);
		add(panelMusicVolume);
		add(panelSoundVolume);
		add(panelDifficulty);
		add(panelKeyBind);
	}
	
	/**
	 * 各パネルの設定状態を，ConfigStatusにセットする
	 */
	public void setStatus(){
		ConfigStatus.setViewLog     (panelViewLog    .getStatus());
		ConfigStatus.setMino        (panelMino       .getStatus());
		ConfigStatus.setMelody      (panelMelody     .getStatus());
		ConfigStatus.setVolMusic    (panelMusicVolume.getStatus());
		ConfigStatus.setVolSound    (panelSoundVolume.getStatus());
		ConfigStatus.setDifficulty  (panelDifficulty .getStatus());
		ConfigStatus.setKeyBind     (panelKeyBind    .getStatus());
		ConfigStatus.setKeyLeft     (ConfigStatus.KEYBIND[panelKeyBind.getStatus()][ConfigInfo.LEFT]);
		ConfigStatus.setKeyUp       (ConfigStatus.KEYBIND[panelKeyBind.getStatus()][ConfigInfo.UP]);
		ConfigStatus.setKeyRight    (ConfigStatus.KEYBIND[panelKeyBind.getStatus()][ConfigInfo.RIGHT]);
		ConfigStatus.setKeyDown     (ConfigStatus.KEYBIND[panelKeyBind.getStatus()][ConfigInfo.DOWN]);
		ConfigStatus.setKeyLeftSpin (ConfigStatus.KEYBIND[panelKeyBind.getStatus()][ConfigInfo.LSPIN]);
		ConfigStatus.setKeyRightSpin(ConfigStatus.KEYBIND[panelKeyBind.getStatus()][ConfigInfo.RSPIN]);
	}
}
