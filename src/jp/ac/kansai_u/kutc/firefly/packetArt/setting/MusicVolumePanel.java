package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.BGMExperimenter;

/**
 * BGMボリューム設定に関するパネル
 * @author akasaka
 */
public class MusicVolumePanel extends JPanel implements ActionListener{
	final byte MUTE = 0, LOW = 50, MED = 75, HIGH = 100;
	final String IMGPATH = new String("./Resources/image/");
	
	private JRadioButton btnVolumeMute, btnVolumeLow, btnVolumeMed, btnVolumeHigh;
	Thread thread;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のボリューム設定
	 */
	MusicVolumePanel(byte b){
		//ImageIcon icon = new ImageIcon(IMGPATH + "temp.png");
		//ImageIcon icon4 = new ImageIcon(IMGPATH + "img4.png");
		
		JLabel labelVolume = new JLabel("音楽の音量");//icon);
		btnVolumeMute = new JRadioButton("Mute");//icon4);
	    btnVolumeLow  = new JRadioButton("Low");//icon4);
	    btnVolumeMed  = new JRadioButton("Medium");//icon4);
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
	    
	    if     (b == 0) btnVolumeMute.setSelected(true);
	    else if(b == 1) btnVolumeLow .setSelected(true);
	    else if(b == 2) btnVolumeMed .setSelected(true);
	    else            btnVolumeHigh.setSelected(true);
	    
	    //スレッドに何もしない処理を一応登録する．
	    //じゃないとstop()でエラーが出るのだもの
	    thread = new BGMExperimenter(MUTE);
	}
	
	/**
	 * ボリューム設定を取得する
	 * @return ボリューム設定（Mute, Low, Medium, High）
	 */
	public byte getStatus(){
		if     (btnVolumeMute.isSelected()) return MUTE;
	    else if(btnVolumeLow .isSelected()) return LOW; 
	    else if(btnVolumeMed .isSelected()) return MED;
	    else                                return HIGH;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		thread.stop();
		if(e.getSource() == btnVolumeLow)
			thread = new BGMExperimenter(LOW);
		else if(e.getSource() == btnVolumeMed)
			thread = new BGMExperimenter(MED);
		else if(e.getSource() == btnVolumeHigh)
			thread = new BGMExperimenter(HIGH);
		thread.start();
	}
}
