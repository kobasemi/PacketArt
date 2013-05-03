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

class MainPanel extends JPanel {
	private ButtonPanel buttonPanel;
	private JLabel labelCursor;
	private Point[] posCursor;
	
	// コンストラクタ
	MainPanel(final int windowWidth, final int windowHeight) {
		final int buttonMargin = 350;
		final int center = windowWidth / 2;
		final int creditMargin = 100;
		final int titleMargin = 50;
		final String imagePath = "resource/image/title/";
		buttonPanel = new ButtonPanel(250, 250);
		posCursor = new Point[buttonPanel.getButtonArray().length];
		BufferedImage imgBackground = null;
		BufferedImage imgCredit = null;
		BufferedImage imgCursor = null;
		BufferedImage imgTitle = null;
		JLabel labelBackground = null;
		JLabel labelCredit = null;
		JLabel labelTitle = null;
		
		// ボタンパネルに関する設定をする
		buttonPanel.setLocation(center - buttonPanel.getWidth() / 2, buttonMargin);
		
		// 画像ファイルを読み込む
		try {
			imgBackground = ImageIO.read(new File(imagePath + "background.png"));
			imgTitle = ImageIO.read(new File(imagePath + "title.png"));
			imgCursor = ImageIO.read(new File(imagePath + "cursor.png"));
			imgCredit = ImageIO.read(new File(imagePath + "credit.png"));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// 背景を設定する
		labelBackground = new JLabel(new ImageIcon(imgBackground));
		labelBackground.setBounds(0, 0, imgBackground.getWidth(), imgBackground.getHeight());
		
		// タイトルを設定する
		labelTitle = new JLabel(new ImageIcon(imgTitle));
		labelTitle.setBounds(center - imgTitle.getWidth() / 2, titleMargin, imgTitle.getWidth(), imgTitle.getHeight());
		
		// クレジットを設定する
		labelCredit = new JLabel(new ImageIcon(imgCredit));
		labelCredit.setBounds(center - imgCredit.getWidth() / 2, buttonPanel.getY() + buttonPanel.getHeight() + creditMargin, imgCredit.getWidth(), imgCredit.getHeight());
		
		// カーソルを設定する
		for (int i = 0; i < posCursor.length; i++) {
			// カーソルの中央とボタンの中央を合わせる
			posCursor[i] = new Point((int) (
					buttonPanel.getX() - imgCursor.getWidth() * 1.5),
					buttonPanel.getY() + buttonPanel.getButton(i).getY() + (buttonPanel.getButton(i).getHeight() / 2) - (imgCursor.getHeight() / 2)
					);
		}
		labelCursor = new JLabel(new ImageIcon(imgCursor));
		labelCursor.setBounds((int) posCursor[0].getX(), (int) posCursor[0].getY(), imgCursor.getWidth(), imgCursor.getHeight());
		
		// メインパネルに関する設定をする
		setLayout(null);
		setBounds(0, 0, windowWidth, windowHeight);
		
		// メインパネルにコンポーネントを追加する
		add(labelBackground, 0);
		add(labelTitle, 0);
		add(labelCredit, 0);
		add(labelCursor, 0);
		add(buttonPanel, 0);
	}
	
	// ボタンの配列を返す
	JButton[] getButtonArray() {
		return buttonPanel.getButtonArray();
	}
	
	// 指定されたボタンを返す
	JButton getButton(final int index) {
		return buttonPanel.getButton(index);
	}

	// ボタン配列の添え字を返す
	int getButtonIndex(JButton button) {
		for (int i = 0; i < buttonPanel.getButtonArray().length; i++) {
			if (button == buttonPanel.getButton(i)) {
				return i;
			}
		}
		
		// 一致するボタンが見つからなかった場合
		return -1;
	}
	
	// ボタンを入れ替える
	void changeButton() {
		buttonPanel.changeCard();
	}
	
	// カーソルをフォーカス中のボタンの横に移動させる
	void moveCursor(int index) {
		if (0 <= index && index < posCursor.length) {
			labelCursor.setLocation(posCursor[index]);
		}
	}
}
