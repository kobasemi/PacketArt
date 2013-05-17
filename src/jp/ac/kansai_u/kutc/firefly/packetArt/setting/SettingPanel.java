package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.PlaySE;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.MidiPlayer;

public class SettingPanel extends JPanel implements ActionListener{
	private ConfigStatusMainPanel setStatusPanel;
	private JButton btnSetting, btnCancel;
	private BufferedImage image;
	
	SettingPanel(){
		setBounds(0, 0, ConfigInfo.WIDTH, ConfigInfo.HEIGHT);
		setLayout(null);
		
		try{ image = ImageIO.read(this.getClass().getResourceAsStream(ConfigInfo.IMGPATH + "background.png")); }
		catch(IOException e){ System.err.println(e.getMessage()); }
		
		JLabel formTitle = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "formTitle.png")));
		formTitle.setBounds(ConfigInfo.WMARGIN, ConfigInfo.HMARGIN, 540, 100);
		formTitle.setOpaque(false);
		
		JLabel frame = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "frame.png")));
		frame.setBounds(17, 30, 566, 740);
		
		setStatusPanel= new ConfigStatusMainPanel();
		setStatusPanel.setBounds(ConfigInfo.WMARGIN, 140, 540, 450);
		
		btnSetting = new JButton(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "btnSetting.png")));
		btnCancel  = new JButton(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "btnCancel.png")));
		btnSetting.setContentAreaFilled(false);
		btnCancel .setContentAreaFilled(false);
		btnSetting.addActionListener(this);
		btnCancel .addActionListener(this);
		
		JPanel btnCombPanel = new JPanel();
		btnCombPanel.setBounds(ConfigInfo.WMARGIN, 660, 540, 100);
		btnCombPanel.setOpaque(false);
		btnCombPanel.add(btnSetting);
		btnCombPanel.add(btnCancel);
		
		add(frame, 0);
		add(formTitle, 0);
		add(setStatusPanel, 0);
		add(btnCombPanel, 0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(setStatusPanel.panelMusicVolume.testBGM != null)
			//スレッドが動いているかも．動いていればストップ
			((MidiPlayer) setStatusPanel.panelMusicVolume.testBGM).stopMidi();
		if(e.getSource() == btnSetting){
			PlaySE.getInstance().play(PlaySE.SELECT);
			setStatusPanel.setStatus();  // 各項目の状態をセットする
			PlaySE.getInstance().setVolumeAll((double)ConfigStatus.getVolSound());  //SEのボリュームを設定
		} else {
			PlaySE.getInstance().play(PlaySE.CANCEL);
		}
		FormUtil.getInstance().changeForm("Title");
	}
	
	@Override
	public void paintComponent(Graphics g) { g.drawImage(image, 0, 0, this); }
}
