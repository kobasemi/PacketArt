package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

/**
 * 設定画面のメイン
 * @author akasaka
 */
public class SettingForm extends FormBase {
	SettingPanel mainPanel;
	
	public void initialize() {
		
		mainPanel = new SettingPanel();
		getContentPane().add(mainPanel, 0);
	}
	
	// 描画関連のコードはここに
	public void paint(Graphics g) {}
	// viewとlogicの分離を考えるときはcommandパターンのようなものでも使ってください
	// パケット解析などはこのメソッドからどうぞ
	public void update() {}
	public void onFormChanged(){}
	public void onClose(){}
	// 使いたい入力イベントを実装、記述してください
	// Eventを切り離すときれいに見えますがめんどくさくなります
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e){}
	public void mouseDragged(MouseEvent e){}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
