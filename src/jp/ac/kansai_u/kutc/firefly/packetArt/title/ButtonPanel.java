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
	private static final int BUTTON_NUMBER = 6;
	private static final int DIVISION = 4; // 1枚目のパネルに配置するボタンの数
	private static final String IMAGE_PATH = "resource/image/title/";
	private static final String[] BUTTON_NAME = {"Start", "Load", "Option", "Exit", "Yes", "No"};
	private static final String[] FILE_NAME = {"start.png", "load.png", "option.png", "exit.png", "yes.png", "no.png"};
	private JButton[] button;
	private CardLayout cardLayout;
	
	// コンストラクタ
	ButtonPanel(final int width, final int height) {
		button = new JButton[BUTTON_NUMBER];
		cardLayout = new CardLayout();
		
		JPanel firstCardPanel = new JPanel(null);
		firstCardPanel.setSize(width, height);
		setCardPanel(firstCardPanel, 0, DIVISION - 1);
		
		JPanel secondCardPanel = new JPanel(null);
		secondCardPanel.setSize(width, height);
		setCardPanel(secondCardPanel, DIVISION, button.length - 1);
		
		setLayout(cardLayout);
		setBackground(new java.awt.Color(255, 255, 255, 64));
		setSize(width, height);
		add(firstCardPanel,"first");
		add(secondCardPanel,"second");
	}
	
	// インデックスで指定されたボタンを返す
	JButton getButton(final int index) {
		if (0 <= index && index < button.length) {
			return button[index];
		} else {
			return null;
		}
	}
	
	// ボタンの配列を返す
	JButton[] getButtonArray() {
		if (button != null) {
			return button;
		} else {
			return new JButton[0];
		}
	}
	
	// カードを切り替える
	void changeCard() {
		cardLayout.next(this);
	}
	
	// カードパネルを設定する
	private void setCardPanel(JPanel panel, final int firstIndex, final int lastIndex) {
		final int buttonNumber = lastIndex - firstIndex + 1;
		final int center = panel.getWidth() / 2;
		int totalImageHeight = 0;
		BufferedImage image[] = new BufferedImage[buttonNumber];
		
		try {
			for (int i = 0; i < buttonNumber; i++) {
				image[i] = ImageIO.read(new File(IMAGE_PATH + FILE_NAME[i + firstIndex]));
				
				// ボタンの間隔を求めるために記憶しておく
				totalImageHeight += image[i].getHeight();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ボタンが等間隔になる間隔を求める
		final int interval = (panel.getHeight() - totalImageHeight) / (buttonNumber + 1);
		
		for (int i = firstIndex; i <= lastIndex; i++) {
			final int imageIndex = i - firstIndex;
			ImageIcon icon = new ImageIcon(image[imageIndex]);
			button[i] = new JButton(icon);
			
			button[i].setDisabledIcon(icon);
			setButtonView(i);
			button[i].setName(BUTTON_NAME[firstIndex + imageIndex]);
			
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
	private void setButtonView(final int index) {
		button[index].setContentAreaFilled(false);
		button[index].setFocusPainted(false);
		button[index].setRolloverEnabled(false);
	}
}
