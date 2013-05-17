package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ButtonPanel extends JPanel {
	private static final int FIRST_NUM = 4;  // 1枚目のパネルに配置するボタンの数
	private static final int SECOND_NUM = 2; // 2枚目のパネルに配置するボタンの数
	private static final String IMAGE_PATH = "resource/image/title/";
	private static final String MESSAGE = "message.png";
	private static final String[] BUTTON_NAME = {"Start", "Load", "Option", "Exit", "Yes", "No"};
	private static final String[] FILE_NAME = {"start.png", "load.png", "option.png", "exit.png", "yes.png", "no.png"};
	private JButton[] buttonArray;
	private CardLayout cardLayout;
	
	// コンストラクタ
	ButtonPanel(final int width, final int height) {
		buttonArray = new JButton[FIRST_NUM + SECOND_NUM];
		cardLayout = new CardLayout();
		
		setLayout(cardLayout);
		setOpaque(false);
		setSize(width, height);
		add(createCardPanel(width, height, new JButton[FIRST_NUM]), "first");
		add(createCardPanel(width, height, new JButton[SECOND_NUM]), "second");
	}
	
	// インデックスで指定されたボタンを返す
	JButton getButton(final int index) {
		if (0 <= index && index < buttonArray.length) {
			return buttonArray[index];
		} else {
			return null;
		}
	}
	
	// ボタンの配列を返す
	JButton[] getButtonArray() {
		if (buttonArray != null) {
			return buttonArray;
		} else {
			return new JButton[0];
		}
	}
	
	// カードを切り替える
	void changeCard() {
		cardLayout.next(this);
	}
	
	// カードパネルを生成する
	private JPanel createCardPanel(final int width, final int height, JButton[] button) {
		int buttonIndex = 0;
		int index = 0;
		int interval = 0;
		int margin = 0;
		JPanel panel = new JPanel(null);
		BufferedImage[] image = createImageArray(button.length);
		
		switch (button.length) {
		case FIRST_NUM:
			interval = (height - plusHeights(image)) / (FIRST_NUM + 1);
			margin = interval;
			break;
		case SECOND_NUM:
			buttonIndex = FIRST_NUM;
			index = FIRST_NUM;
			JLabel labelMessage = createLabel();
			
			interval = (height - plusHeights(image) - labelMessage.getHeight()) / (SECOND_NUM + 1);
			labelMessage.setLocation((width - labelMessage.getWidth()) / 2, interval / 2);
			margin = labelMessage.getY() + (int) (interval * 1.5);
			
			panel.add(labelMessage);
			break;
		}
		
		for (int i = 0; i < button.length; i++) {
			button[i] = new JButton();
			configureButton(i + buttonIndex, button[i], image[i]);
			
			if (i == 0) {
					button[i].setLocation((width - image[i].getWidth()) / 2, margin);
			} else {
				button[i].setLocation((width - image[i].getWidth()) / 2, button[i - 1].getY() + button[i - 1].getHeight() + interval);
			}
			
			panel.add(button[i]);
		}
		System.arraycopy(button, 0, buttonArray, index, button.length);
		
		panel.setOpaque(false);
		panel.setSize(width, height);
		
		return panel;
	}
	
	// ボタンに関する設定をする
	private void configureButton(final int index, JButton button, BufferedImage image) {
		ImageIcon icon = new ImageIcon(image);
		
		button.setContentAreaFilled(false);
		button.setDisabledIcon(icon);
		button.setIcon(icon);
		button.setFocusPainted(false);
		button.setName(BUTTON_NAME[index]);
		button.setSize(image.getWidth(), image.getHeight());
	}
	
	// ボタンの画像を読み込む
	private BufferedImage[] createImageArray(final int num) {
		BufferedImage[] image = new BufferedImage[num];
		
		try {
			switch (num) {
			case FIRST_NUM:
				for (int i = 0; i < num; i++) {
					image[i] = ImageIO.read(this.getClass().getResourceAsStream("/" + IMAGE_PATH + FILE_NAME[i]));
				}
				break;
			case SECOND_NUM:
				for (int i = 0; i < num; i++) {
					image[i] = ImageIO.read(this.getClass().getResourceAsStream("/" + IMAGE_PATH + FILE_NAME[FIRST_NUM + i]));
				}
				break;
			}
			
			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return new BufferedImage[0];
		}
	}
	
	// メッセージ用のラベルを生成する
	private JLabel createLabel() {
		BufferedImage image = null;
		
		try {
			
			image = ImageIO.read(this.getClass().getResourceAsStream("/" + IMAGE_PATH + MESSAGE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JLabel labelMessage = new JLabel(new ImageIcon(image));
		labelMessage.setSize(image.getWidth(), image.getHeight());
		
		return labelMessage;
	}
	
	// 画像の高さの合計を返す
	private int plusHeights(BufferedImage[] image) {
		int total = 0;
		
		for (BufferedImage img : image) {
			total += img.getHeight();
		}
		
		return total;
	}
}
