package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jp.ac.kansai_u.kutc.firefly.packetArt.PlaySE;

/**
 * SEボリューム設定に関するパネル
 * @author akasaka
 */
public class SoundVolumePanel extends JPanel implements ActionListener{
	private JRadioButton btnVolumeMute, btnVolumeLow, btnVolumeMed, btnVolumeHigh;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のボリューム設定
	 */
	SoundVolumePanel(byte b){
		setLayout(new FlowLayout(FlowLayout.LEFT, ConfigInfo.HGAP, 0));
		setOpaque(false);
		
		JLabel labelVolume = new JLabel(new ImageIcon(ConfigInfo.IMGPATH + "labelSE.png"));
		btnVolumeMute = new JRadioButton(new ImageIcon(ConfigInfo.VOLPATH + "volMute.png"));
		btnVolumeLow  = new JRadioButton(new ImageIcon(ConfigInfo.VOLPATH + "volLow.png"));
		btnVolumeMed  = new JRadioButton(new ImageIcon(ConfigInfo.VOLPATH + "volMedium.png"));
		btnVolumeHigh = new JRadioButton(new ImageIcon(ConfigInfo.VOLPATH + "volHigh.png"));

		ButtonGroup volumeGroup = new ButtonGroup();
		volumeGroup.add(btnVolumeMute); volumeGroup.add(btnVolumeLow);
		volumeGroup.add(btnVolumeMed); volumeGroup.add(btnVolumeHigh);
		
		btnVolumeMute.setSelectedIcon(new ImageIcon(ConfigInfo.VOLPATH + "volMuteSelected.png"));
		btnVolumeLow .setSelectedIcon(new ImageIcon(ConfigInfo.VOLPATH + "volLowSelected.png"));
		btnVolumeMed .setSelectedIcon(new ImageIcon(ConfigInfo.VOLPATH + "volMediumSelected.png"));
		btnVolumeHigh.setSelectedIcon(new ImageIcon(ConfigInfo.VOLPATH + "volHighSelected.png"));
		
		btnVolumeMute.setContentAreaFilled(false);
		btnVolumeLow .setContentAreaFilled(false);
		btnVolumeMed .setContentAreaFilled(false);
		btnVolumeHigh.setContentAreaFilled(false);
		
		btnVolumeMute.addActionListener(this);
		btnVolumeLow .addActionListener(this);
		btnVolumeMed .addActionListener(this);
		btnVolumeHigh.addActionListener(this);
		
		add(labelVolume);
		add(btnVolumeMute);
		add(btnVolumeLow);
		add(btnVolumeMed);
		add(btnVolumeHigh);
		
		if     (b == ConfigInfo.MUTE)      btnVolumeMute.setSelected(true);
		else if(b == ConfigInfo.VOLSELOW)    btnVolumeLow .setSelected(true);
		else if(b == ConfigInfo.VOLSEMEDIUM) btnVolumeMed .setSelected(true);
		else if(b == ConfigInfo.VOLSEHIGH)   btnVolumeHigh.setSelected(true);
	}
	
	/**
	 * ボリューム設定を取得する
	 * @return ボリューム設定（Mute, Low, Medium, High）
	 */
	public byte getStatus(){
		if     (btnVolumeMute.isSelected()) return ConfigInfo.MUTE;
	    else if(btnVolumeLow .isSelected()) return ConfigInfo.VOLSELOW;
	    else if(btnVolumeMed .isSelected()) return ConfigInfo.VOLSEMEDIUM;
	    else if(btnVolumeHigh.isSelected()) return ConfigInfo.VOLSEHIGH;
		return -1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnVolumeMute)
			return;
		PlaySE playSE = PlaySE.getInstance();
		playSE.play("select" ,getStatus());
	}
}
