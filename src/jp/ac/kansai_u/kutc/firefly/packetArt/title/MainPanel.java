package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MainPanel extends JPanel {
	private static final int CREDIT_MARGIN = 700;
	private static final int TITLE_MARGIN = 50;
	private static final String IMAGE_PATH = "resource/image/title/";
	private static final String FILE_NAME_CURSOR = "cursor.png";
	private static final String[] FILE_NAME = {"background.png", "title.png", "credit.png"}; // カーソル画像以外のファイル名
	private JLabel labelCursor;
	private Point[] posCursor;
	
	// コンストラクタ
	MainPanel(final int width, final int height) {
		setLayout(null);
		setBounds(0, 0, width, height);
		configureMainPanel(width / 2);
	}
	
	// カーソルの描画に関する設定をする
	void createCursor(Point point[]) {
		// 引数がヌルの場合はカーソルを描画しない
		if (point == null) {
			System.err.println("argument is null : Method [createCusor] : MainPanel.java");
			posCursor = new Point[0];
			return;
		}
		
		posCursor = new Point[point.length];
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File(IMAGE_PATH + FILE_NAME_CURSOR));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < posCursor.length; i++) {
			// カーソルの中央とボタンの中央を合わせる
			final int x = (int) (point[i].getX() - image.getWidth() * 1.5);
			final int y = (int) (point[i].getY() - image.getHeight() / 2);
			posCursor[i] = new Point(x, y);
		}
		labelCursor = new JLabel(new ImageIcon(image));
		labelCursor.setBounds((int) posCursor[0].getX(), (int) posCursor[0].getY(), image.getWidth(), image.getHeight());
		
		add(labelCursor, 0);
	}
	
	// インデックスで指定されたボタンの横にカーソルを移動させる
	void moveCursor(final int index) {
		if (0 <= index && index < posCursor.length) {
			labelCursor.setLocation(posCursor[index]);
		}
	}
	
	// カーソル以外の描画に関する設定をする
	private void configureMainPanel(final int center) {
		BufferedImage[] image = new BufferedImage[FILE_NAME.length];
		
		try {
			for (int i = 0; i < image.length; i++) {
				image[i] = ImageIO.read(new File(IMAGE_PATH + FILE_NAME[i]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JLabel labelBackground = new JLabel(new ImageIcon(image[0]));
		labelBackground.setBounds(0, 0, image[0].getWidth(), image[0].getHeight());
		
		JLabel labelTitle = new JLabel(new ImageIcon(image[1]));
		labelTitle.setBounds(center - image[1].getWidth() / 2, TITLE_MARGIN, image[1].getWidth(), image[1].getHeight());
		
		JLabel labelCredit = new JLabel(new ImageIcon(image[2]));
		labelCredit.setBounds(center - image[2].getWidth() / 2, CREDIT_MARGIN, image[2].getWidth(), image[2].getHeight());
		
		add(labelBackground, 0);
		add(labelTitle, 0);
		add(labelCredit, 0);
	}
}
