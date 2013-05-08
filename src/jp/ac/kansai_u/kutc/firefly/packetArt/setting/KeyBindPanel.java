package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

// TODO Layoutを整えなきゃ

/**
 * キーコンフィグに関するパネル
 * @author akasaka
 */
public class KeyBindPanel extends JPanel implements ActionListener{
	final public String KEYPATH = new String("./resource/image/config/key/");
	private JRadioButton btnKeyDefault, btnKeyGamer, btnKeyVim;
	private JLabel labelUpImg, labelDownImg, labelLeftImg, labelRightImg, labelLSpinImg, labelRSpinImg;
	private JLabel labelUpKey, labelDownKey, labelLeftKey, labelRightKey, labelLSpinKey, labelRSpinKey;
	private JLabel[] labelImageArray;
	private JLabel[] labelKeyCodeArray;
	
	/**
	 * デフォルトコンストラクタ
	 * @param key（各キーの配列）
	 */
	KeyBindPanel(byte b){
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
		p.setOpaque(false);
		btnKeyDefault = new JRadioButton(new ImageIcon(KEYPATH + "bindDefault.png"));
		btnKeyGamer = new JRadioButton(new ImageIcon(KEYPATH + "bindGamer.png"));
		btnKeyVim = new JRadioButton(new ImageIcon(KEYPATH + "bindVim.png"));
		ButtonGroup bindGroup = new ButtonGroup();
		bindGroup.add(btnKeyDefault); bindGroup.add(btnKeyGamer); bindGroup.add(btnKeyVim);
		
		btnKeyDefault.setContentAreaFilled(false);
		btnKeyGamer.setContentAreaFilled(false);
		btnKeyVim.setContentAreaFilled(false);
		btnKeyDefault.setSelectedIcon(new ImageIcon(KEYPATH + "bindDefaultSelected.png"));
		btnKeyGamer.setSelectedIcon(new ImageIcon(KEYPATH + "bindGamerSelected.png"));
		btnKeyVim.setSelectedIcon(new ImageIcon(KEYPATH + "bindVimSelected.png"));
		
		btnKeyDefault.addActionListener(this);
		btnKeyGamer.addActionListener(this);
		btnKeyVim.addActionListener(this);
		
		p.add(btnKeyDefault);
		p.add(btnKeyGamer);
		p.add(btnKeyVim);
		
		JPanel p2 = new JPanel();
		p2.setOpaque(false);
		labelUpImg = new JLabel(new ImageIcon(KEYPATH + "arrUp.png"));
		labelDownImg = new JLabel(new ImageIcon(KEYPATH + "arrDown.png"));
		labelLeftImg = new JLabel(new ImageIcon(KEYPATH + "arrLeft.png"));
		labelRightImg = new JLabel(new ImageIcon(KEYPATH + "arrRight.png"));
		labelLSpinImg = new JLabel(new ImageIcon(KEYPATH + "arrLSpin.png"));
		labelRSpinImg = new JLabel(new ImageIcon(KEYPATH + "arrRSpin.png"));
		labelImageArray = new JLabel[]{labelUpImg, labelDownImg, labelLeftImg, labelRightImg,
				labelLSpinImg, labelRSpinImg};
		for(int i=0; i<labelImageArray.length; i++){
			labelImageArray[i].setPreferredSize(new Dimension(80, 50));
			labelImageArray[i].setHorizontalAlignment(JLabel.CENTER);
			p2.add(labelImageArray[i]);
		}
		
		JPanel p3 = new JPanel();
		p3.setOpaque(false);
		labelUpKey = new JLabel(KeyEvent.getKeyText(ConfigStatus.KEYBIND[b][0]));
		labelDownKey = new JLabel(KeyEvent.getKeyText(ConfigStatus.KEYBIND[b][1]));
		labelLeftKey = new JLabel(KeyEvent.getKeyText(ConfigStatus.KEYBIND[b][2]));
		labelRightKey = new JLabel(KeyEvent.getKeyText(ConfigStatus.KEYBIND[b][3]));
		labelLSpinKey = new JLabel(KeyEvent.getKeyText(ConfigStatus.KEYBIND[b][4]));
		labelRSpinKey = new JLabel(KeyEvent.getKeyText(ConfigStatus.KEYBIND[b][5]));
		labelKeyCodeArray = new JLabel[]{labelUpKey, labelDownKey, labelLeftKey, labelRightKey,
				labelLSpinKey, labelRSpinKey};
		for(int i=0; i<labelKeyCodeArray.length; i++){
			labelKeyCodeArray[i].setPreferredSize(new Dimension(80, 30));
			labelKeyCodeArray[i].setHorizontalAlignment(JLabel.CENTER);
			labelKeyCodeArray[i].setFont(new Font("Self", Font.PLAIN, 20));
			labelKeyCodeArray[i].setForeground(Color.white);
			p3.add(labelKeyCodeArray[i]);
		}
		
		add(p);
		add(p2);
		add(p3);
		
		if     (b == ConfigStatus.KEYDEFAULT) btnKeyDefault.setSelected(true);
		else if(b == ConfigStatus.KEYGAMER)   btnKeyGamer.setSelected(true);
		else if(b == ConfigStatus.KEYVIM)     btnKeyVim.setSelected(true);
	}

	/**
	 * キーバインドの設定状態を取得する
	 * @return キーバインドの種類（default, gamer, vim）
	 */
	public byte getStatus(){
		if     (btnKeyDefault.isSelected()) return ConfigStatus.KEYDEFAULT;
		else if(btnKeyGamer.isSelected())   return ConfigStatus.KEYGAMER;
		else if(btnKeyVim.isSelected())     return ConfigStatus.KEYVIM;
		return -1;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnKeyDefault)
			for(int i=0; i<labelKeyCodeArray.length; i++)
				labelKeyCodeArray[i].setText(KeyEvent.getKeyText(ConfigStatus.KEYBIND[ConfigStatus.KEYDEFAULT][i]));
		else if(e.getSource() == btnKeyGamer)
			for(int i=0; i<labelKeyCodeArray.length; i++)
				labelKeyCodeArray[i].setText(KeyEvent.getKeyText(ConfigStatus.KEYBIND[ConfigStatus.KEYGAMER][i]));
		else if(e.getSource() == btnKeyVim)
			for(int i=0; i<labelKeyCodeArray.length; i++)
				labelKeyCodeArray[i].setText(KeyEvent.getKeyText(ConfigStatus.KEYBIND[ConfigStatus.KEYVIM][i]));
	}
}
