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
		buttonPanel = new ButtonPanel(250, 250);
		posCursor = new Point[buttonPanel.getButtonArray().length];
		
		buttonPanel.setLocation((windowWidth - buttonPanel.getWidth()) / 2, buttonMargin);
		
		setLayout(null);
		setBounds(0, 0, windowWidth, windowHeight);
		setMainPanel(windowWidth / 2);
		
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
	
	// カーソルを対応するボタンの横に移動させる
	void moveCursor(int index) {
		if (0 <= index && index < posCursor.length) {
			labelCursor.setLocation(posCursor[index]);
		}
	}
	
	// メインパネルの描画に関する設定をする
	private void setMainPanel(final int center) {
		final int creditMargin = 100;
		final int titleMargin = 50;
		final String imagePath = "resource/image/title/";
		BufferedImage imgBackground = null;
		BufferedImage imgCredit = null;
		BufferedImage imgCursor = null;
		BufferedImage imgTitle = null;
		JLabel labelBackground = null;
		JLabel labelCredit = null;
		JLabel labelTitle = null;
		
		try {
			imgBackground = ImageIO.read(new File(imagePath + "background.png"));
			imgTitle = ImageIO.read(new File(imagePath + "title.png"));
			imgCursor = ImageIO.read(new File(imagePath + "cursor.png"));
			imgCredit = ImageIO.read(new File(imagePath + "credit.png"));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		labelBackground = new JLabel(new ImageIcon(imgBackground));
		labelBackground.setBounds(0, 0, imgBackground.getWidth(), imgBackground.getHeight());
		
		labelTitle = new JLabel(new ImageIcon(imgTitle));
		labelTitle.setBounds(center - imgTitle.getWidth() / 2, titleMargin, imgTitle.getWidth(), imgTitle.getHeight());
		
		labelCredit = new JLabel(new ImageIcon(imgCredit));
		labelCredit.setBounds(center - imgCredit.getWidth() / 2, buttonPanel.getY() + buttonPanel.getHeight() + creditMargin, imgCredit.getWidth(), imgCredit.getHeight());
		
		for (int i = 0; i < posCursor.length; i++) {
			// カーソルの中央とボタンの中央を合わせる
			posCursor[i] = new Point((int) (buttonPanel.getX() - imgCursor.getWidth() * 1.5),
											buttonPanel.getY() + buttonPanel.getButton(i).getY() + (buttonPanel.getButton(i).getHeight() - imgCursor.getHeight()) / 2);
		}
		labelCursor = new JLabel(new ImageIcon(imgCursor));
		labelCursor.setBounds((int) posCursor[0].getX(), (int) posCursor[0].getY(), imgCursor.getWidth(), imgCursor.getHeight());
		
		add(labelBackground, 0);
		add(labelTitle, 0);
		add(labelCredit, 0);
		add(labelCursor, 0);
	}
}
