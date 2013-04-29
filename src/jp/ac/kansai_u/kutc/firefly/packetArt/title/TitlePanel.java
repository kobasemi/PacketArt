package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class TitlePanel extends JPanel {
	JLabel labelCursor;
	JButton[] button;
	Point[] posCursor;
	
	// コンストラクタ
	TitlePanel(final int panelWidth, final int panelHeight) {
		int min = Integer.MAX_VALUE;
        final int buttonInterval = 20;
        final int buttonMargin = 250;
        final int buttonNumber = 3;
    	final int center = panelWidth / 2;
        final int creditMargin = 100;
        final int titleMargin = 50;
        final String imagePath = "resource/image/title/";
        final String[] buttonName = {"Playing", "Option", "Music"};
		BufferedImage imgBackground = null;
		BufferedImage imgCursor = null;
		BufferedImage imgCredit = null;
		BufferedImage imgTitle = null;
		BufferedImage[] imgButton = new BufferedImage[buttonNumber];
		JLabel labelBackground = null;
		JLabel labelCredit = null;
		JLabel labelTitle = null;
		button = new JButton[buttonNumber];
		posCursor = new Point[buttonNumber];
		
		// 画像ファイルを読み込む
		try {
			imgBackground = ImageIO.read(new File(imagePath + "background.png"));
			imgTitle = ImageIO.read(new File(imagePath + "title.png"));
			imgCursor = ImageIO.read(new File(imagePath + "cursor.png"));
			imgCredit = ImageIO.read(new File(imagePath + "credit.png"));
			imgButton[0] = ImageIO.read(new File(imagePath + "start.png"));
			imgButton[1] = ImageIO.read(new File(imagePath + "option.png"));
			imgButton[2] = ImageIO.read(new File(imagePath + "music.png"));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// 背景を設定する
		labelBackground = new JLabel(new ImageIcon(imgBackground));
		labelBackground.setBounds(0, 0, imgBackground.getWidth(), imgBackground.getHeight());
		
		// タイトルを設定する
		labelTitle = new JLabel(new ImageIcon(imgTitle));
		labelTitle.setBounds(center - imgTitle.getWidth() / 2, titleMargin, imgTitle.getWidth(), imgTitle.getHeight());
		
		// ボタンを設定する
		for (int i = 0; i < buttonNumber; i++) {
			button[i] = new JButton(new ImageIcon(imgButton[i]));
			button[i].setContentAreaFilled(false);
			button[i].setFocusPainted(false);
			button[i].setName(buttonName[i]);

			if (i == 0) {
				button[i].setBounds(center - imgButton[i].getWidth() / 2, labelTitle.getY() + labelTitle.getHeight() + buttonMargin, imgButton[i].getWidth(), imgButton[i].getHeight());
			} else {
				button[i].setBounds(center - imgButton[i].getWidth() / 2, button[i - 1].getY() + button[i - 1].getHeight() + buttonInterval, imgButton[i].getWidth(), imgButton[i].getHeight());
			}
			
			// カーソル位置のために記憶しておく
			if (min > button[i].getX()) {
				min = button[i].getX();
			}
		}
		
		// カーソルを設定する
		for (int i = 0; i < buttonNumber; i++) {
			// カーソルの上端とボタンの上端を合わせる
			//posCursor[i] = new Point((int) (min - imgCursor.getWidth() * 1.5), button[i].getY());
			// カーソルの中央とボタンの中央を合わせる
			posCursor[i] = new Point((int) (min - imgCursor.getWidth() * 1.5), button[i].getY() + (button[i].getHeight() / 2) - (imgCursor.getHeight() / 2));
			// カーソルの下端とボタンの下端を合わせる
			//posCursor[i] = new Point((int) (min - imgCursor.getWidth() * 1.5), button[i].getY() + button[i].getHeight() - imgCursor.getHeight());
		}
		labelCursor = new JLabel(new ImageIcon(imgCursor));
		labelCursor.setBounds((int) posCursor[0].getX(), (int) posCursor[0].getY(), imgCursor.getWidth(), imgCursor.getHeight());
		
		// クレジットを設定する
		labelCredit = new JLabel(new ImageIcon(imgCredit));
		labelCredit.setBounds(center - imgCredit.getWidth() / 2, button[buttonNumber - 1].getY() + button[buttonNumber - 1].getHeight() + creditMargin, imgCredit.getWidth(), imgCredit.getHeight());
		
		// パネルを設定する
		setBounds(0, 0, panelWidth, panelHeight);
		setLayout(null);
		
		// パネルにコンポーネントを配置する
		add(labelBackground, 0);
		add(labelTitle, 0);
		add(button[0], 0);
		add(button[1], 0);
		add(button[2], 0);
		add(labelCursor, 0);
		add(labelCredit, 0);
	}
}
