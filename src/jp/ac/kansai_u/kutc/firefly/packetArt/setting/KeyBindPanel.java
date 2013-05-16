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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * キーコンフィグに関するパネル
 * @author akasaka
 */
public class KeyBindPanel extends JPanel implements ActionListener{
	private JRadioButton btnKeyDefault, btnKeyGamer, btnKeyVim;
	private JLabel labelLeftImg, labelUpImg, labelRightImg, labelDownImg, labelLSpinImg, labelRSpinImg;
	private JLabel labelLeftKey, labelUpKey, labelRightKey, labelDownKey, labelLSpinKey, labelRSpinKey;
	private JLabel[] labelImageArray;
	private JLabel[] labelKeyCodeArray;
	
	/**
	 * デフォルトコンストラクタ
	 * @param b（キーバインドの設定）
	 */
	KeyBindPanel(byte b){
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel sectionPanel = new JPanel();
		sectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, ConfigInfo.HGAP, 0));
		sectionPanel.setOpaque(false);
		JLabel labelSection = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.IMGPATH + "labelKeyBind.png")));
		sectionPanel.add(labelSection);
		
		JPanel btnKeyBindPanel = new JPanel();
		btnKeyBindPanel.setLayout(new FlowLayout(FlowLayout.CENTER, ConfigInfo.HGAP, 0));
		btnKeyBindPanel.setOpaque(false);
		btnKeyDefault = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "bindDefault.png")));
		btnKeyGamer   = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "bindGamer.png")));
		btnKeyVim     = new JRadioButton(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "bindVim.png")));
		ButtonGroup bindGroup = new ButtonGroup();
		bindGroup.add(btnKeyDefault); bindGroup.add(btnKeyGamer); bindGroup.add(btnKeyVim);
		
		btnKeyDefault.setContentAreaFilled(false);
		btnKeyGamer  .setContentAreaFilled(false);
		btnKeyVim    .setContentAreaFilled(false);
		btnKeyDefault.setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "bindDefaultSelected.png")));
		btnKeyGamer  .setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "bindGamerSelected.png")));
		btnKeyVim    .setSelectedIcon(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "bindVimSelected.png")));
		btnKeyDefault.addActionListener(this);
		btnKeyGamer  .addActionListener(this);
		btnKeyVim    .addActionListener(this);
		
		btnKeyBindPanel.add(btnKeyDefault);
		btnKeyBindPanel.add(btnKeyGamer);
		btnKeyBindPanel.add(btnKeyVim);
		
		JPanel arrowImagePanel = new JPanel();
		arrowImagePanel.setOpaque(false);
		labelLeftImg  = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "arrLeft.png")));
		labelUpImg    = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "arrUp.png")));
		labelRightImg = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "arrRight.png")));
		labelDownImg  = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "arrDown.png")));
		labelLSpinImg = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "arrLSpin.png")));
		labelRSpinImg = new JLabel(new ImageIcon(this.getClass().getResource(ConfigInfo.KEYPATH + "arrRSpin.png")));
		labelImageArray = new JLabel[]{labelLeftImg, labelUpImg, labelRightImg, labelDownImg,
				labelLSpinImg, labelRSpinImg};
		for(int i=0; i<labelImageArray.length; i++){
			labelImageArray[i].setPreferredSize(new Dimension(80, 40));
			labelImageArray[i].setHorizontalAlignment(JLabel.CENTER);
			arrowImagePanel.add(labelImageArray[i]);
		}
		
		JPanel labelKeyPanel = new JPanel();
		labelKeyPanel.setOpaque(false);
		labelLeftKey  = new JLabel(KeyEvent.getKeyText(ConfigInfo.KEYBIND[b][ConfigInfo.LEFT]));
		labelUpKey    = new JLabel(KeyEvent.getKeyText(ConfigInfo.KEYBIND[b][ConfigInfo.UP]));
		labelRightKey = new JLabel(KeyEvent.getKeyText(ConfigInfo.KEYBIND[b][ConfigInfo.RIGHT]));
		labelDownKey  = new JLabel(KeyEvent.getKeyText(ConfigInfo.KEYBIND[b][ConfigInfo.DOWN]));
		labelLSpinKey = new JLabel(KeyEvent.getKeyText(ConfigInfo.KEYBIND[b][ConfigInfo.LSPIN]));
		labelRSpinKey = new JLabel(KeyEvent.getKeyText(ConfigInfo.KEYBIND[b][ConfigInfo.RSPIN]));
		labelKeyCodeArray = new JLabel[]{labelLeftKey, labelUpKey, labelRightKey, labelDownKey,
				labelLSpinKey, labelRSpinKey};
		for(int i=0; i<labelKeyCodeArray.length; i++){
			labelKeyCodeArray[i].setPreferredSize(new Dimension(80, 30));
			labelKeyCodeArray[i].setHorizontalAlignment(JLabel.CENTER);
			labelKeyCodeArray[i].setFont(new Font("Self", Font.PLAIN, 18));
			labelKeyCodeArray[i].setForeground(Color.white);
			labelKeyPanel.add(labelKeyCodeArray[i]);
		}
		
		add(sectionPanel);
		add(btnKeyBindPanel);
		add(arrowImagePanel);
		add(labelKeyPanel);
		
		if     (b == ConfigInfo.KEYDEFAULT) btnKeyDefault.setSelected(true);
		else if(b == ConfigInfo.KEYGAMER)   btnKeyGamer  .setSelected(true);
		else if(b == ConfigInfo.KEYVIM)     btnKeyVim    .setSelected(true);
	}

	/**
	 * キーバインドの設定状態を取得する
	 * @return キーバインドの種類（default, gamer, vim）
	 */
	public byte getStatus(){
		if     (btnKeyDefault.isSelected()) return ConfigInfo.KEYDEFAULT;
		else if(btnKeyGamer  .isSelected()) return ConfigInfo.KEYGAMER;
		else if(btnKeyVim    .isSelected()) return ConfigInfo.KEYVIM;
		return -1;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		byte idx = getStatus();  // キーバインドのインデックスを取得
		for(int i=0; i<labelKeyCodeArray.length; i++)
				labelKeyCodeArray[i].setText(KeyEvent.getKeyText(ConfigInfo.KEYBIND[idx][i]));
	}
}
