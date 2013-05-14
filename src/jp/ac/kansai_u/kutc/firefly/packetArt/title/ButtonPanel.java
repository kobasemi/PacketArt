package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.CardLayout;
import java.awt.image.BufferedImage;
import java.io.File;
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
	private JButton[] button;
	private CardLayout cardLayout;
	
	// コンストラクタ
	ButtonPanel(final int width, final int height) {
		button = new JButton[FIRST_NUM + SECOND_NUM];
		cardLayout = new CardLayout();
		
		setLayout(cardLayout);
		setOpaque(false);
		setSize(width, height);
		add(createCardPanel(width, height, FIRST_NUM), "first");
		add(createCardPanel(width, height, SECOND_NUM), "second");
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
	
	// カードパネルを生成する
	private JPanel createCardPanel(final int width, final int height, int number) {
		int interval = 0;
		int margin = 0;
		int plusIndex = 0;
		JPanel panel = new JPanel(null);
		BufferedImage[] image = createImageArray(number);
		
		switch (number) {
		case FIRST_NUM:
			interval = (height - plusHeights(image)) / (FIRST_NUM + 1);
			margin = interval;
			break;
		case SECOND_NUM:
			JLabel labelMessage = createLabel();
			
			interval = (height - plusHeights(image) - labelMessage.getHeight()) / (SECOND_NUM + 1);
			labelMessage.setLocation((width - labelMessage.getWidth()) / 2, interval / 2);
			plusIndex = FIRST_NUM;
			margin = labelMessage.getY() + (int) (interval * 1.5);
			
			panel.add(labelMessage);
			break;
		}
		
		for (int i = 0; i < number; i++) {
			final int index = i + plusIndex;
			
			configureButton(index, image[i]);
			
			if (i == 0) {
					button[index].setLocation((width - image[i].getWidth()) / 2, margin);
			} else {
				button[index].setLocation((width - image[i].getWidth()) / 2, button[index - 1].getY() + button[index - 1].getHeight() + interval);
			}
			
			panel.add(button[index]);
		}
		
		panel.setOpaque(false);
		panel.setSize(width, height);
		
		return panel;
	}
	
	// ボタンに関する設定をする
	private void configureButton(final int index, BufferedImage image) {
		ImageIcon icon = new ImageIcon(image);
		button[index] = new JButton(icon);
		
		button[index].setContentAreaFilled(false);
		button[index].setDisabledIcon(icon);
		button[index].setFocusPainted(false);
		button[index].setName(BUTTON_NAME[index]);
		button[index].setSize(image.getWidth(), image.getHeight());
	}
	
	// ボタンの画像を読み込む
	private BufferedImage[] createImageArray(final int num) {
		BufferedImage[] image = new BufferedImage[num];
		
		try {
			switch (num) {
			case FIRST_NUM:
				for (int i = 0; i < num; i++) {
					image[i] = ImageIO.read(new File(IMAGE_PATH + FILE_NAME[i]));
				}
				break;
			case SECOND_NUM:
				for (int i = 0; i < num; i++) {
					image[i] = ImageIO.read(new File(IMAGE_PATH + FILE_NAME[FIRST_NUM + i]));
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
			image = ImageIO.read(new File(IMAGE_PATH + MESSAGE));
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
