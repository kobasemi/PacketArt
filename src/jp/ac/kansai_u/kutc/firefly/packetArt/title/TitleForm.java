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
import jp.ac.kansai_u.kutc.firefly.packetArt.PlaySE;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.MidiPlayer;
import jp.ac.kansai_u.kutc.firefly.packetArt.playing.PlayForm;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.ReadDumpForm;
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.ConfigStatus;
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.SettingForm;

/**
 * タイトル画面を管理するクラスです。
 * @author hiyoko
 */
public class TitleForm extends FormBase implements FocusListener {
	private PanelManager panelManager;
	private Thread titlemusic; // by Lisa
	
	/**
	 * タイトル画面のコンストラクタです。
	 */
	public TitleForm() {
		PcapManager.getInstance().start();
		PlaySE.getInstance().play(PlaySE.OPEN);
		new ConfigStatus();
	}
	
	@Override
	public void initialize() {
		// パネルが存在しなければ生成する
		if (panelManager == null) {
			panelManager = new PanelManager(getSize().width, getSize().height);
		}
		
		panelManager.addToParent(getContentPane());
		enableButton();
		enableKeyFocus();
		
		// TODO: オプションから戻ってきた時にボタンがフォーカスを失うのをなんとかする
		// panelManager.getButton(0).requestFocusInWindow();
		
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
		
		// タイトルBGMを鳴らす。 by Lisa
		titlemusic = new MidiPlayer(ConfigStatus.getVolMusic(), "TitleMusic.mid");
		titlemusic.start();
	}
	
	@Override
	public void paint(Graphics g) {}
	
	@Override
	public void update() {}
	
	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource();
		
		// クリックされたボタンに従って画面を変化させる
		if (obj instanceof JButton) {
			JButton source = (JButton) obj;
			
			System.out.println("Mouse Clicked : [" + source.getName() + "] Button");
			buttonPressed(source);
		}
	}
    public void mouseEntered(MouseEvent e) {
    	Object obj = e.getSource();
    	
    	// マウスカーソルが乗っているボタンにフォーカスを取得させる
    	if (obj instanceof JButton) {
    		JButton source = (JButton) obj;
    		
    		source.requestFocusInWindow();
    	}
    }
    public void mouseDragged(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
    	Object obj = e.getSource();
    	
    	if (obj instanceof JButton) {
    		panelManager.repaint();
    	}
    }
    public void mouseReleased(MouseEvent e) {
    	Object obj = e.getSource();
    	
    	if (obj instanceof JButton) {
    		panelManager.repaint();
    	}
    }
    
    public void keyPressed(KeyEvent e) {
    	Object obj = e.getSource();
		
		// エンターキーが押下されたボタンに従って画面を変化させる
		if (obj instanceof JButton) {
			JButton source = (JButton) obj;
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("[ENTER] Key Pressed : [" + source.getName() + "] Button");
				buttonPressed(source);
			}
		}
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    
    public void focusGained(FocusEvent e) {
    	Object obj = e.getSource();
    	
    	// フォーカスを得たボタンの横にカーソルを移動させる
		if (obj instanceof JButton) {
			JButton source = (JButton) e.getSource();
			JButton[] button = panelManager.getButtonArray();
			
			for (int i = 0; i < button.length; i++) {
				if (source == button[i]) {
					PlaySE.getInstance().play(PlaySE.SELECT2);
					panelManager.moveCursor(i);
					break;
				}
			}
		}
    }
    public void focusLost(FocusEvent e) {}
    
    @Override
    public void onFormChanged() {
    	// フォームチェンジ時にはタイトルBGMを止める。 by Lisa
		 ((MidiPlayer) titlemusic).stopMidi();
    }
    
    @Override
    public void onClose() {}
    
    // ボタンをクリックするか、ボタン上でエンターキーを押下した時の動作
    private void buttonPressed(JButton source) {
    	panelManager.repaint();
		PlaySE.getInstance().play(PlaySE.SELECT);
    	
		switch (panelManager.getButtonIndex(source)) {
		case 0: // Start
			disableButton();
			FormUtil.getInstance().changeForm("Playing");
			break;
		case 1: // Load
			disableButton();
			FormUtil.getInstance().changeForm("ReadDump");
			break;
		case 2: // Option
			disableButton();
			FormUtil.getInstance().changeForm("Option");
			break;
		case 3: // Exit
			panelManager.changeButton();
			panelManager.getButton(4).requestFocusInWindow();
			break;
		case 4: // Exit - Yes
			PcapManager.getInstance().kill();
			
			System.exit(0);
			break;
		case 5: // Exit - No
			panelManager.changeButton();
			panelManager.getButton(0).requestFocusInWindow();
			break;
		}
    }
    
    // ボタンを無効化する
    private void disableButton() {
		for (JButton button : panelManager.getButtonArray()) {
			button.setEnabled(false);
    		button.removeKeyListener(this);
    		button.removeMouseListener(this);
    		button.removeFocusListener(this);
    		panelManager.repaint();
		}
    }
    
    // ボタンを有効化する
    private void enableButton() {
		for (JButton button : panelManager.getButtonArray()) {
			button.setEnabled(true);
    		button.addKeyListener(this);
    		button.addMouseListener(this);
    		button.addFocusListener(this);
		}
    }
    
	// カーソルキーとスペースキーでフォーカスを変えられるようにする
    private void enableKeyFocus() {
		KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		
		// 順送り
		Set<AWTKeyStroke> forwardKeys = new HashSet<AWTKeyStroke>(focusManager.getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		forwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, 0));
		forwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_MASK));
		forwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE, 0));
		getContentPane().setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
		
		// 逆送り
		Set<AWTKeyStroke> backwardKeys = new HashSet<AWTKeyStroke>(focusManager.getDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		backwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_UP, 0));
		backwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_MASK));
		backwardKeys.add(KeyStroke.getAWTKeyStroke(KeyEvent.VK_SPACE, InputEvent.SHIFT_MASK));
		getContentPane().setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
    }
}
