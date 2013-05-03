package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

class ButtonPanel extends JPanel {
	private JButton[] button;
	private JPanel firstCardPanel, secondCardPanel;
	private CardLayout cardLayout;
	
	// コンストラクタ
	ButtonPanel(final int panelWidth, final int panelHeight) {
		final int division = 3; // 1枚目のパネルに配置するボタンの数
		button = new JButton[5];
		cardLayout = new CardLayout();
		
		// 1枚目のパネル
		firstCardPanel = new JPanel(null);
		firstCardPanel.setSize(panelWidth, panelHeight);
		setCardPanel(firstCardPanel, 0, division - 1);
		
		// 2枚目のパネル
		secondCardPanel = new JPanel(null);
		secondCardPanel.setSize(panelWidth, panelHeight);
		setCardPanel(secondCardPanel, division, button.length - 1);
		
		// カード切り替え用のパネル
		setLayout(cardLayout);
		setBackground(new java.awt.Color(255, 255, 255, 128));
		setSize(panelWidth, panelHeight);
		add(firstCardPanel);
		add(secondCardPanel);
	}
	
	// ボタンの配列を返す
	JButton[] getButtonArray() {
		return button;
	}
	
	// 指定されたボタンを返す
	JButton getButton(final int index) {
		if (0 <= index && index < button.length) {
			return button[index];
		} else {
			return null;
		}
	}
	
	// カードを切り替える
	void changeCard() {
		cardLayout.next(this);
	}
	
	// カードパネルを設定する
	private void setCardPanel(JPanel panel, final int firstIndex, final int lastIndex) {
		int totalImageHeight = 0;
		final int buttonNumber = lastIndex - firstIndex + 1;
		final int center = panel.getWidth() / 2;
		final String imagePath = "resource/image/title/";
		final String[] buttonName = {"Start", "Option", "Exit", "Yes", "No"};
		final String[] fileName = {"start.png", "option.png", "exit.png", "yes.png", "no.png"};
		BufferedImage image[] = new BufferedImage[buttonNumber];
		
		// 画像ファイルを読み込む
		try {
			for (int i = 0; i < buttonNumber; i++) {
				image[i] = ImageIO.read(new File(imagePath + fileName[i + firstIndex]));
				
				// ボタンの間隔を求めるために記憶しておく
				totalImageHeight += image[i].getHeight();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// ボタンが等間隔になる間隔を求める
		int interval = (panel.getHeight() - totalImageHeight) / (buttonNumber + 1);
		
		// ボタンを設定する
		for (int i = firstIndex; i <= lastIndex; i++) {
			int imageIndex = i - firstIndex;
			button[i] = new JButton(new ImageIcon(image[imageIndex]));
			setButton(i);
			button[i].setName(buttonName[imageIndex]);
			
			if (i == firstIndex) {
				button[i].setBounds(center - image[imageIndex].getWidth() / 2, interval, image[imageIndex].getWidth(), image[imageIndex].getHeight());
			} else {
				button[i].setBounds(center - image[imageIndex].getWidth() / 2, button[i - 1].getY() + button[i - 1].getHeight() + interval, image[imageIndex].getWidth(), image[i - firstIndex].getHeight());
			}
			
			panel.add(button[i]);
		}
		
		panel.setOpaque(false);
	}
	
	// ボタンの描画に関する設定をする
	private void setButton(final int index) {
		button[index].setContentAreaFilled(false);
		button[index].setFocusPainted(false);
		button[index].setRolloverEnabled(false);
	}
}
