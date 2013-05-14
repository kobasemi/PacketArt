package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.Container;
import java.awt.Point;

import javax.swing.JButton;

class PanelManager {
	private static final int BUTTON_MARGIN = 350;
	private static final int BUTTON_PANEL_WIDTH = 300;
	private static final int BUTTON_PANEL_HEIGHT = 300;
	private MainPanel mainPanel;
	private ButtonPanel buttonPanel;
	
	// コンストラクタ
	PanelManager(final int width, final int height) {
		mainPanel = new MainPanel(width, height);
		buttonPanel = new ButtonPanel(BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT);
		
		buttonPanel.setLocation((width - BUTTON_PANEL_WIDTH) / 2, BUTTON_MARGIN);
		configureCursor();
		
		mainPanel.add(buttonPanel, 0);
	}
	
	// 引数で渡されたコンテナにパネルを追加する
	void addToParent(Container parent) {
		parent.add(mainPanel, 0);
	}
	
	// インデックスで指定されたボタンの横にカーソルを移動させる
	void moveCursor(final int index) {
		mainPanel.moveCursor(index);
	}
	
	// インデックスで指定されたボタンを返す
	JButton getButton(final int index) {
		return buttonPanel.getButton(index);
	}
	
	// ボタンの配列を返す
	JButton[] getButtonArray() {
		return buttonPanel.getButtonArray();
	}
	
	// ボタンの添え字を返す
	int getButtonIndex(JButton source) {
		JButton[] button = buttonPanel.getButtonArray();
		
		for (int i = 0; i < button.length; i++) {
			if (source == button[i]) {
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
	
	// カーソルの位置を設定する
	private void configureCursor() {
		JButton[] button = buttonPanel.getButtonArray();
		Point[] point = new Point[button.length];
		
		for (int i = 0; i < point.length; i++) {
			final int x = buttonPanel.getX();
			final int y = buttonPanel.getY() + button[i].getY() + button[i].getHeight() / 2;
			point[i] = new Point(x, y);
		}
		
		mainPanel.createCursor(point);
	}
}
