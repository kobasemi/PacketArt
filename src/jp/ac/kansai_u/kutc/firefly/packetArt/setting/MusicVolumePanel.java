package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.BGMExperimenter;

/**
 * ボリューム設定に関するパネル
 * @author akasaka
 */
public class MusicVolumePanel extends JPanel implements ActionListener{
	final String IMGPATH = new String("./Resources/image/");
	private JRadioButton btnVolumeMute, btnVolumeLow, btnVolumeMed, btnVolumeHigh;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のボリューム設定
	 */
	MusicVolumePanel(byte b){
		//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		//ImageIcon icon4 = new ImageIcon(IMGPATH + "img4.png");
		
		JLabel labelVolume = new JLabel("音楽の音量");//icon);
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
	    
	    btnVolumeMute.addActionListener(this);
	    btnVolumeLow.addActionListener(this);
	    btnVolumeMed.addActionListener(this);
	    btnVolumeHigh.addActionListener(this);
	    
	    if(b == 0)      btnVolumeMute.setSelected(true);
	    else if(b == 1) btnVolumeLow.setSelected(true);
	    else if(b == 2) btnVolumeMed.setSelected(true);
	    else btnVolumeHigh.setSelected(true);
	}
	
	/**
	 * ボリューム設定を取得する
	 * @return ボリューム設定（0_Mute, 1_Low, 2_Medium, 3_High）
	 */
	public byte getStatus(){
		if(btnVolumeMute.isSelected()) return (byte)0;
	    else if(btnVolumeLow.isSelected()) return (byte)1; 
	    else if(btnVolumeMed.isSelected()) return (byte)2;
	    else return (byte)3;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnVolumeMute)
			try {
				BGMExperimenter.playChangedBGM(0);
			} catch (Exception e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		else if(e.getSource() == btnVolumeLow)
			try {
				BGMExperimenter.playChangedBGM(1);
			} catch (Exception e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		else if(e.getSource() == btnVolumeMed)
			try {
				BGMExperimenter.playChangedBGM(2);
			} catch (Exception e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		else if(e.getSource() == btnVolumeHigh)
			try {
				BGMExperimenter.playChangedBGM(3);
			} catch (Exception e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
	}
}
