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
	VolumePanel panelMusic, panelSound;
	DifficultyPanel panelDifficulty;
	
	/**
	 * コンストラクタ
	 * @param stat（ConfigStatusデータ構造）
	 */
	ConfigStatusMainPanel(ConfigStatus stat){
		this.setBackground(Color.blue);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			
		panelViewLog = new ViewLogPanel(stat.isViewLog());
		panelViewLog.setBackground(Color.yellow);
		panelMino = new MinoPanel(stat.getMino());
		panelMino.setBackground(Color.cyan);
		panelMusic = new VolumePanel(stat.getVolMusic(), "音楽の音量");
		panelMusic.setBackground(Color.magenta);
		panelSound = new VolumePanel(stat.getVolSound(), "効果音の音量");
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
