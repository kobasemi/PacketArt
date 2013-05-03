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
		final String imagePath = "resource/image/title/";
		button = new JButton[5];
		cardLayout = new CardLayout();
		
		// 1枚目のパネル
		firstCardPanel = new JPanel(null);
		setFirstCardPanel(panelWidth, panelHeight, imagePath);
		
		// 2枚目のパネル
		secondCardPanel = new JPanel(null);
		setSecondCardPanel(panelWidth, panelHeight, imagePath);
		
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
	
	// 1枚目のパネルを設定する
	private void setFirstCardPanel(final int width, final int height, final String path) {
		final int center = width / 2;
		final String[] buttonName = {"Start", "Option", "Exit"};
		final String[] fileName = {"start.png", "option.png", "exit.png"};
		BufferedImage image[] = new BufferedImage[fileName.length];
		
		// 画像ファイルを読み込む
		try {
			for (int i = 0; i < fileName.length; i++) {
				image[i] = ImageIO.read(new File(path + fileName[i]));
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// ボタンが等間隔になる間隔を求める
		int interval = (height - image[0].getHeight() - image[1].getHeight() - image[2].getHeight()) / (fileName.length + 1);
		
		// ボタンを設定する
		for (int i = 0; i < fileName.length; i++) {
			button[i] = new JButton(new ImageIcon(image[i]));
			setButton(i);
			button[i].setName(buttonName[i]);
			
			if (i == 0) {
				button[i].setBounds(center - image[i].getWidth() / 2, interval, image[i].getWidth(), image[i].getHeight());
			} else {
				button[i].setBounds(center - image[i].getWidth() / 2, button[i - 1].getY() + button[i - 1].getHeight() + interval, image[i].getWidth(), image[i].getHeight());
			}
		}
		
		firstCardPanel.setOpaque(false);
		firstCardPanel.add(button[0]);
		firstCardPanel.add(button[1]);
		firstCardPanel.add(button[2]);
	}
	
	// 2枚目のパネルを設定する
	private void setSecondCardPanel(final int width, final int height, final String path) {
		final int center = width / 2;
		final String[] buttonName = {"Yes", "No"};
		final String[] fileName = {"yes.png", "no.png"};
		BufferedImage image[] = new BufferedImage[fileName.length];
		
		// 画像ファイルを読み込む
		try {
			for (int i = 0; i < fileName.length; i++) {
				image[i] = ImageIO.read(new File(path + fileName[i]));
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// ボタンが等間隔になる間隔を求める
		int interval = (height - image[0].getHeight() - image[1].getHeight()) / (fileName.length + 1);
		
		// ボタンを設定する
		for (int i = 0; i < fileName.length; i++) {
			int iPlus3 = i + 3;
			button[iPlus3] = new JButton(new ImageIcon(image[i]));;
			setButton(iPlus3);
			button[iPlus3].setName(buttonName[i]);
			
			if (i == 0) {
				button[iPlus3].setBounds(center - image[i].getWidth() / 2, interval, image[i].getWidth(), image[i].getHeight());
			} else {
				button[iPlus3].setBounds(center - image[i].getWidth() / 2, button[iPlus3 - 1].getY() + button[iPlus3 - 1].getHeight() + interval, image[i].getWidth(), image[i].getHeight());
			}
		}
		
		secondCardPanel.setOpaque(false);
		secondCardPanel.add(button[3]);
		secondCardPanel.add(button[4]);
	}
	
	// ボタンの描画に関する設定をする
	private void setButton(final int index) {
		button[index].setContentAreaFilled(false);
		button[index].setFocusPainted(false);
		button[index].setRolloverEnabled(false);
	}
}
