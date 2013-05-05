package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.BGMExperimenter;

/**
 * BGMボリューム設定に関するパネル
 * @author akasaka
 */
public class MusicVolumePanel extends JPanel implements ActionListener{
	final String IMGPATH = new String("./resource/image/config/");
	final String VOLPATH = new String(IMGPATH + "volume/");
	 
	private JRadioButton btnVolumeMute, btnVolumeLow, btnVolumeMed, btnVolumeHigh;
	Thread thread;
	
	/**
	 * コンストラクタ
	 * @param 初期化前のボリューム設定
	 */
	MusicVolumePanel(byte b){
		setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
		JLabel labelVolume = new JLabel(new ImageIcon(IMGPATH + "labelBGM.png"));
		btnVolumeMute = new JRadioButton(new ImageIcon(VOLPATH + "volMute.png"));
	    btnVolumeLow  = new JRadioButton(new ImageIcon(VOLPATH + "volLow.png"));
	    btnVolumeMed  = new JRadioButton(new ImageIcon(VOLPATH + "volMedium.png"));
	    btnVolumeHigh = new JRadioButton(new ImageIcon(VOLPATH + "volHigh.png"));
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
	    
	    if     (b == ConfigStatus.MUTE)      btnVolumeMute.setSelected(true);
	    else if(b == ConfigStatus.BGMLOW)    btnVolumeLow .setSelected(true);
	    else if(b == ConfigStatus.BGMMEDIUM) btnVolumeMed .setSelected(true);
	    else if(b == ConfigStatus.BGMHIGH)   btnVolumeHigh.setSelected(true);
	    
	    //スレッドに何もしない処理を一応登録する．
	    //じゃないとstop()でエラーが出るのだもの
	    thread = new BGMExperimenter(ConfigStatus.MUTE);
	}
	
	/**
	 * ボリューム設定を取得する
	 * @return ボリューム設定（Mute, Low, Medium, High）
	 */
	public byte getStatus(){
		if     (btnVolumeMute.isSelected()) return ConfigStatus.MUTE;
	    else if(btnVolumeLow .isSelected()) return ConfigStatus.BGMLOW; 
	    else if(btnVolumeMed .isSelected()) return ConfigStatus.BGMMEDIUM;
	    else if(btnVolumeHigh.isSelected()) return ConfigStatus.BGMHIGH;
		return -1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		thread.stop();
		if(e.getSource() == btnVolumeLow)
			thread = new BGMExperimenter(ConfigStatus.BGMLOW);
		else if(e.getSource() == btnVolumeMed)
			thread = new BGMExperimenter(ConfigStatus.BGMMEDIUM);
		else if(e.getSource() == btnVolumeHigh)
			thread = new BGMExperimenter(ConfigStatus.BGMHIGH);
		thread.start();
	}
}
