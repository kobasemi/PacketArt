package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MainPanel extends JPanel {
	private static final int CREDIT_MARGIN = 700;
	private static final int FRAME_MARGIN = 350;
	private static final int TITLE_MARGIN = 100;
	private static final String IMAGE_PATH = "resource/image/title/";
	private static final String FILE_NAME_CURSOR = "cursor.png";
	private static final String[] FILE_NAME = {"background.png", "title.png", "frame.png", "credit.png"}; // カーソル画像以外のファイル名
	private JLabel labelCursor;
	private Point[] posCursor;
	
	// コンストラクタ
	MainPanel(final int width, final int height) {
		setLayout(null);
		setBounds(0, 0, width, height);
		configureMainPanel(width);
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
			image = ImageIO.read(this.getClass().getResourceAsStream("/" + IMAGE_PATH + FILE_NAME_CURSOR));
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
	private void configureMainPanel(final int width) {
		BufferedImage[] image = new BufferedImage[FILE_NAME.length];
		JLabel[] label = new JLabel[image.length];
		
		try {
			for (int i = 0; i < image.length; i++) {
				image[i] = ImageIO.read(this.getClass().getResourceAsStream("/" + IMAGE_PATH + FILE_NAME[i]));
				label[i] = new JLabel(new ImageIcon(image[i]));
				label[i].setSize(image[i].getWidth(), image[i].getHeight());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		label[0].setLocation(0, 0);
		label[1].setLocation((width - image[1].getWidth()) / 2, TITLE_MARGIN);
		label[2].setLocation((width - image[2].getWidth()) / 2, FRAME_MARGIN);
		label[3].setLocation((width - image[3].getWidth()) / 2, CREDIT_MARGIN);
		
		for (JLabel lbl : label) {
			add(lbl, 0);
		}
	}
}
