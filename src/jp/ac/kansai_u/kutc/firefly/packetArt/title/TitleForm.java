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
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.SettingForm;

/* このファイルがクラスの基本的な構造と使い方 
 * テンプレートをコピー→メソッドを編集
 */

/**
 * タイトルフォームです.
 */
public class TitleForm extends FormBase implements FocusListener {
	TitlePanel panel;

	// TODO:実装時にコメントアウトをはずす。
	// Thread thread;
	
	// コンストラクタ
	public TitleForm() {
		// 遷移先のフォームを生成する
		FormUtil.getInstance().createForm("Playing", PlayForm.class);
		FormUtil.getInstance().createForm("Option", SettingForm.class);
		//FormUtil.getInstance().createForm("", ); // TODO: SoundTestの実装を検討する
	}
	
	// あらゆるオブジェクトの初期化はここから(jnetpcap関連クラスなど)
	// あくまでフォームなのでフォームを使ってなんでもやらないこと推奨
	public void initialize() {
		panel = new TitlePanel(getSize().width, getSize().height);
		
		// イベントリスナーを追加する
		for (JButton b:panel.getButton()) {
			b.addFocusListener(this);
			b.addKeyListener(this);
			b.addMouseListener(this);
		}
		
		// パネルを配置する
		getContentPane().add(panel, 0);
		
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
		
		// TODO:実装時にコメントアウトをはずす。
		// はずしたらタイトルで音楽が流れるようになります。
		// PostScript by Lisa
		// thread = new TitleMusic(75);
		// thread.start();
	}
	
	// 描画関連のコードはここに
	public void paint(Graphics g) {
		
	}

	// viewとlogicの分離を考えるときはcommandパターンのようなものでも使ってください
	// パケット解析などはこのメソッドからどうぞ
	public void update() {
		// TODO: if select button is pressed then show forms[selected item]
	}

	// 使いたい入力イベントを実装、記述してください
	// Eventを切り離すときれいに見えますがめんどくさくなります
	// MouseListener
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
		
		// クリックされたボタンに従って画面を変化させる
		if (obj instanceof JButton) {
			JButton b = (JButton) obj;
			
			System.out.println("Mouse Clicked : [" + b.getName() + "] Button");

			// TODO:実装時にthreadのコメントアウトをはずす。	
			// thread.stop();
			if (b == panel.getButton(0)) {
				FormUtil.getInstance().changeForm("Playing");
			} else if (b == panel.getButton(1)) {
    			FormUtil.getInstance().changeForm("Option");
			} else if (b == panel.getButton(2)) {
				//FormUtil.getInstance().changeForm("");
			}
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
    // KeyListener
    public void keyPressed(KeyEvent e) {
		Object obj = e.getSource();
		
		// エンターキーが押下されたボタンに従って画面を変化させる
		if (obj instanceof JButton) {
			JButton b = (JButton) obj;
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("[ENTER] Key Pressed : [" + b.getName() + "] Button");
				if (b == panel.getButton(0)) {
					FormUtil.getInstance().changeForm("Playing");
				} else if (b == panel.getButton(1)) {
	    			FormUtil.getInstance().changeForm("Option");
				} else if (b == panel.getButton(2)) {
					//FormUtil.getInstance().changeForm("");
				}
			}
		}
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    // FocusListener
    public void focusGained(FocusEvent e) {
    	Object obj = e.getSource();
    	
    	// フォーカスを得たボタンの横にカーソルを移動させる
		if (obj instanceof JButton) {
			JButton b = (JButton) e.getSource();
			
			if (b == panel.getButton(0)) {
				panel.moveCursor(0);
			} else if (b == panel.getButton(1)) {
				panel.moveCursor(1);
			} else if (b == panel.getButton(2)) {
				panel.moveCursor(2);
			}
		}
    }
    public void focusLost(FocusEvent e) {}
    
    public void onFormChanged() {
    	for(JButton b : panel.getButton()){
    		b.removeKeyListener(this);
    		b.removeMouseListener(this);
    		b.removeFocusListener(this);
    	}
    }
    
    public void onClose() {}
}
