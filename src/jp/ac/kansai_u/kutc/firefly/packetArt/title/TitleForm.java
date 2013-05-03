package jp.ac.kansai_u.kutc.firefly.packetArt.title;

import java.awt.AWTKeyStroke;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.KeyStroke;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.playing.PlayForm;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.ReadDumpForm;
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.SettingForm;

/**
 * タイトルフォーム
 */
public class TitleForm extends FormBase implements FocusListener {
	MainPanel panel;
	// TODO:実装時にコメントアウトをはずす。
	// Thread thread;
	
	// コンストラクタ
	public TitleForm() {
		PcapManager.getInstance().start();
	}
	
	public void initialize() {
		// パネルが存在しなければ生成する
		if (panel == null ) {
			panel = new MainPanel(getSize().width, getSize().height);
		}
		
		// ボタンにイベントリスナーを追加する
		for (JButton b:panel.getButtonArray()) {
			b.addFocusListener(this);
			b.addKeyListener(this);
			b.addMouseListener(this);
		}
		
		// パネルを追加する
		getContentPane().add(panel, 0);
		
		// TODO: オプションから戻ってきた時にボタンがフォーカスを失うのをなんとかする
		// panel.getButton(0).requestFocusInWindow();
		
		// TODO:実装時にコメントアウトをはずす。
		// はずしたらタイトルで音楽が流れるようになります。
		// PostScript by Lisa
		// thread = new TitleMusic(75);
		// thread.start();
		
		// 遷移先のフォームが存在しなければ生成する
		if(!FormUtil.getInstance().getForm().isExistForm("Playing")) {
			FormUtil.getInstance().createForm("Playing", PlayForm.class);
		}
		if(!FormUtil.getInstance().getForm().isExistForm("ReadDump")) {
			FormUtil.getInstance().createForm("ReadDump", ReadDumpForm.class);
		}
		if(!FormUtil.getInstance().getForm().isExistForm("Option")) {
			FormUtil.getInstance().createForm("Option", SettingForm.class);
		}
		
		// カーソルキーとスペースキーでフォーカスを変えられるようにする
		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		// 順送り
		Set<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>(focusManager.getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		forwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, 0));
		forwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK));
		forwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE, 0));
		focusManager.setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
		// 逆送り
		Set<AWTKeyStroke> backwardKeys = new HashSet<AWTKeyStroke>(focusManager.getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		backwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, 0));
		backwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK));
		backwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_MASK));
		focusManager.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
	}
	
	public void paint(Graphics g) {}
	
	public void update() {}
	
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
		
		// クリックされたボタンに従って画面を変化させる
		if (obj instanceof JButton) {
			JButton b = (JButton) obj;
			
			System.out.println("Mouse Clicked : [" + b.getName() + "] Button");
			buttonPressed(b);
		}
	}
    public void mouseEntered(MouseEvent e) {
    	Object obj = e.getSource();
    	
    	// マウスカーソルが乗っているボタンにフォーカスを取得させる
    	if (obj instanceof JButton) {
    		((JButton) obj).requestFocusInWindow();
    	}
    }
    public void mouseDragged(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    
    public void keyPressed(KeyEvent e) {
    	Object obj = e.getSource();
		
		// エンターキーが押下されたボタンに従って画面を変化させる
		if (obj instanceof JButton) {
			JButton b = (JButton) obj;
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("[ENTER] Key Pressed : [" + b.getName() + "] Button");
				buttonPressed(b);
			}
		}
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public void focusGained(FocusEvent e) {
    	panel.repaint();
    	Object obj = e.getSource();
    	
    	// フォーカスを得たボタンの横にカーソルを移動させる
		if (obj instanceof JButton) {
			JButton b = (JButton) e.getSource();
			
			for (int i = 0; i < panel.getButtonArray().length; i++) {
				if (b == panel.getButton(i)) {
					panel.moveCursor(i);
					break;
				}
			}
		}
    }
    public void focusLost(FocusEvent e) {}
    
    public void onFormChanged() {
    	for(JButton b : panel.getButtonArray()){
    		b.removeKeyListener(this);
    		b.removeMouseListener(this);
    		b.removeFocusListener(this);
    	}
    	
		// TODO:実装時にthreadのコメントアウトをはずす。	
		// thread.stop();
    }
    
    public void onClose() {}
    
    // ボタンをクリックするか、ボタン上でエンターキーを押下した時の動作
    private void buttonPressed(JButton button) {
    	panel.repaint();
    	
		switch (panel.getButtonIndex(button)) {
		case 0: // Start
			FormUtil.getInstance().changeForm("Playing");
			break;
		case 1: // Load
			FormUtil.getInstance().changeForm("ReadDump");
			break;
		case 2: // Option
			FormUtil.getInstance().changeForm("Option");
			break;
		case 3: // Exit
			panel.changeButton();
			panel.getButton(4).requestFocusInWindow();
			break;
		case 4: // Exit - Yes
			PcapManager.getInstance().kill();
			System.exit(0);
			break;
		case 5: // Exit - No
			panel.changeButton();
			panel.getButton(0).requestFocusInWindow();
			break;
		}
    }
}
