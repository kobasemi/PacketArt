package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.MidiPlayer;

/**
 * BGMボリューム設定に関するパネル
 * @author akasaka
 */
public class MusicVolumePanel extends JPanel implements ActionListener{
	final String VOLPATH = new String(ConfigInfo.IMGPATH + "volume/");
	 
	private JRadioButton btnVolumeMute, btnVolumeLow, btnVolumeMed, btnVolumeHigh;
	Thread thread = null;
	private String file = "BGMTestSound.mid";
	
	/**
	 * コンストラクタ
	 * @param 初期化前のボリューム設定
	 */
	MusicVolumePanel(byte b){
		setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
		setOpaque(false);
		
		JLabel labelVolume = new JLabel(new ImageIcon(ConfigInfo.IMGPATH + "labelBGM.png"));
		btnVolumeMute = new JRadioButton(new ImageIcon(VOLPATH + "volMute.png"));
		btnVolumeLow  = new JRadioButton(new ImageIcon(VOLPATH + "volLow.png"));
		btnVolumeMed  = new JRadioButton(new ImageIcon(VOLPATH + "volMedium.png"));
		btnVolumeHigh = new JRadioButton(new ImageIcon(VOLPATH + "volHigh.png"));
	
		btnVolumeMute.setSelectedIcon(new ImageIcon(VOLPATH + "volMuteSelected.png"));
		btnVolumeLow .setSelectedIcon(new ImageIcon(VOLPATH + "volLowSelected.png"));
		btnVolumeMed .setSelectedIcon(new ImageIcon(VOLPATH + "volMediumSelected.png"));
		btnVolumeHigh.setSelectedIcon(new ImageIcon(VOLPATH + "volHighSelected.png"));
		
		btnVolumeMute.setContentAreaFilled(false);
		btnVolumeLow .setContentAreaFilled(false);
		btnVolumeMed .setContentAreaFilled(false);
		btnVolumeHigh.setContentAreaFilled(false);
	    
		ButtonGroup volumeGroup = new ButtonGroup();
	    volumeGroup.add(btnVolumeMute); volumeGroup.add(btnVolumeLow );
	    volumeGroup.add(btnVolumeMed ); volumeGroup.add(btnVolumeHigh);
	    
	    add(labelVolume);
	    add(btnVolumeMute);
	    add(btnVolumeLow);
	    add(btnVolumeMed);
	    add(btnVolumeHigh);
	    
	    btnVolumeLow .addActionListener(this);
	    btnVolumeMed .addActionListener(this);
	    btnVolumeHigh.addActionListener(this);
	    
	    if     (b == ConfigInfo.MUTE)      btnVolumeMute.setSelected(true);
	    else if(b == ConfigInfo.VOLLOW)    btnVolumeLow .setSelected(true);
	    else if(b == ConfigInfo.VOLMEDIUM) btnVolumeMed .setSelected(true);
	    else if(b == ConfigInfo.VOLHIGH)   btnVolumeHigh.setSelected(true);
	}
	
	/**
	 * ボリューム設定を取得する
	 * @return ボリューム設定（Mute, Low, Medium, High）
	 */
	public byte getStatus(){
		if     (btnVolumeMute.isSelected()) return ConfigInfo.MUTE;
	    else if(btnVolumeLow .isSelected()) return ConfigInfo.VOLLOW; 
	    else if(btnVolumeMed .isSelected()) return ConfigInfo.VOLMEDIUM;
	    else if(btnVolumeHigh.isSelected()) return ConfigInfo.VOLHIGH;
		return -1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(thread != null)
			// スレッドに何か入っているときは，ストップする
//			TODO ストップをやめて，halt(), interrupt()を使用しよう
			((MidiPlayer) thread).stopMidi();
		thread = new MidiPlayer(getStatus(), file);
		
		thread.start();
	}
}
