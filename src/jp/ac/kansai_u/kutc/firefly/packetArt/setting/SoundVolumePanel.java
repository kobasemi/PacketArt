package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

//import jp.ac.kansai_u.kutc.firefly.packetArt.playSE;

/**
 * SEボリューム設定に関するパネル
 * @author akasaka
 */
public class SoundVolumePanel extends JPanel implements ActionListener{
	final String IMGPATH = new String("./Resources/image/");
	private JRadioButton btnVolumeMute, btnVolumeLow, btnVolumeMed, btnVolumeHigh;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のボリューム設定
	 */
	SoundVolumePanel(byte b){
		//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		//ImageIcon icon4 = new ImageIcon(IMGPATH + "img4.png");
		
		JLabel labelVolume = new JLabel("効果音の音量");//icon);
		btnVolumeMute = new JRadioButton("Mute");//icon4);
	    btnVolumeLow = new JRadioButton("Low");//icon4);
	    btnVolumeMed = new JRadioButton("Medium");//icon4);
	    btnVolumeHigh = new JRadioButton("High");//icon4);
	    ButtonGroup volumeGroup = new ButtonGroup();
	    volumeGroup.add(btnVolumeMute); volumeGroup.add(btnVolumeLow);
	    volumeGroup.add(btnVolumeMed); volumeGroup.add(btnVolumeHigh);
	    add(labelVolume);
	    add(btnVolumeMute);
	    add(btnVolumeLow);
	    add(btnVolumeMed);
	    add(btnVolumeHigh);
	    btnVolumeLow.addActionListener(this);
	    btnVolumeMed.addActionListener(this);
	    btnVolumeHigh.addActionListener(this);
	    
	    if     (b == ConfigStatus.MUTE)     btnVolumeMute.setSelected(true);
	    else if(b == ConfigStatus.SELOW)    btnVolumeLow .setSelected(true);
	    else if(b == ConfigStatus.SEMEDIUM) btnVolumeMed .setSelected(true);
	    else if(b == ConfigStatus.SEHIGH)   btnVolumeHigh.setSelected(true);
	}
	
	/**
	 * ボリューム設定を取得する
	 * @return ボリューム設定（Mute, Low, Medium, High）
	 */
	public byte getStatus(){
		if     (btnVolumeMute.isSelected()) return ConfigStatus.MUTE;
	    else if(btnVolumeLow.isSelected())  return ConfigStatus.SELOW;
	    else if(btnVolumeMed.isSelected())  return ConfigStatus.SEMEDIUM;
	    else if(btnVolumeHigh.isSelected()) return ConfigStatus.SEHIGH;
		return -1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*if(e.getSource() == btnVolumeLow)
			playSE.volume = playSE.Volume.LOW;
		else if(e.getSource() == btnVolumeMed)
			playSE.volume = playSE.Volume.MEDIUM;
		else if(e.getSource() == btnVolumeHigh)
			playSE.volume = playSE.Volume.HIGH;
		playSE.SELECT.play();*/
	}
}
