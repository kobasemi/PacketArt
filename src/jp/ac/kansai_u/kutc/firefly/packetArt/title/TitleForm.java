package jp.ac.kansai_u.kutc.firefly.packetArt.title;
import java.awt.AWTKeyStroke;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
	int center;
	BufferedImage imgBackground, imgTitle, imgCursor, imgCredit;
	JLabel labelBackground, labelTitle, labelCursor, labelCredit;
	BufferedImage[] imgButton;
	JButton[] button;
	Point[] posCursor;
	
	// あらゆるオブジェクトの初期化はここから(jnetpcap関連クラスなど)
	// あくまでフォームなのでフォームを使ってなんでもやらないこと推奨
	public void initialize() {
		center = getSize().width / 2;
		imgButton = new BufferedImage[BUTTON_NUMBER];
		button = new JButton[BUTTON_NUMBER];
		posCursor = new Point[BUTTON_NUMBER];
		
		// 画像ファイルを読み込む
		try {
			imgBackground = ImageIO.read(new File(PATH + "background.png"));
			imgTitle = ImageIO.read(new File(PATH + "title.png"));
			imgCursor = ImageIO.read(new File(PATH + "cursor.png"));
			imgCredit = ImageIO.read(new File(PATH + "credit.png"));
			imgButton[0] = ImageIO.read(new File(PATH + "start.png"));
			imgButton[1] = ImageIO.read(new File(PATH + "option.png"));
			imgButton[2] = ImageIO.read(new File(PATH + "music.png"));
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		// 背景を配置する
		labelBackground = new JLabel(new ImageIcon(imgBackground));
		labelBackground.setBounds(0, 0, imgBackground.getWidth(), imgBackground.getHeight());
		getContentPane().add(labelBackground, 0);
		
		// タイトルを配置する
		labelTitle = new JLabel(new ImageIcon(imgTitle));
		labelTitle.setBounds(center - imgTitle.getWidth() / 2, TITLE_MARGIN, imgTitle.getWidth(), imgTitle.getHeight());
		getContentPane().add(labelTitle, 0);
		
		// ボタンを配置する
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < BUTTON_NUMBER; i++) {
			button[i] = new JButton(new ImageIcon(imgButton[i]));
			button[i].setContentAreaFilled(false);
			button[i].setFocusPainted(false);
			button[i].setName(BUTTON_NAME[i]);
			button[i].addFocusListener(this);
			button[i].addKeyListener(this);
			button[i].addMouseListener(this);
			if (i == 0) {
				button[i].setBounds(center - imgButton[i].getWidth() / 2, labelTitle.getY() + labelTitle.getHeight() + BUTTON_MARGIN, imgButton[i].getWidth(), imgButton[i].getHeight());
			} else {
				button[i].setBounds(center - imgButton[i].getWidth() / 2, button[i - 1].getY() + button[i - 1].getHeight() + BUTTON_INTERVAL, imgButton[i].getWidth(), imgButton[i].getHeight());
			}
			getContentPane().add(button[i], 0);
			
			// カーソル位置のために記憶しておく
			if (min > button[i].getX()) {
				min = button[i].getX();
			}
		}
		
		// カーソル位置を設定する
		for (int i = 0; i < BUTTON_NUMBER; i++) {
			// カーソルの上端とボタンの上端を合わせる
			//posCursor[i] = new Point((int) (min - imgCursor.getWidth() * 1.5), button[i].getY());
			// カーソルの中央とボタンの中央を合わせる
			posCursor[i] = new Point((int) (min - imgCursor.getWidth() * 1.5), button[i].getY() + (button[i].getHeight() / 2) - (imgCursor.getHeight() / 2));
			// カーソルの下端とボタンの下端を合わせる
			//posCursor[i] = new Point((int) (min - imgCursor.getWidth() * 1.5), button[i].getY() + button[i].getHeight() - imgCursor.getHeight());
		}
		
		// カーソルを配置する
		labelCursor = new JLabel(new ImageIcon(imgCursor));
		labelCursor.setBounds((int) posCursor[0].getX(), (int) posCursor[0].getY(), imgCursor.getWidth(), imgCursor.getHeight());
		getContentPane().add(labelCursor, 0);
		
		// クレジットを配置する
		labelCredit = new JLabel(new ImageIcon(imgCredit));
		labelCredit.setBounds(center - imgCredit.getWidth() / 2, button[BUTTON_NUMBER - 1].getY() + button[BUTTON_NUMBER - 1].getHeight() + CREDIT_MARGIN, imgCredit.getWidth(), imgCredit.getHeight());
		getContentPane().add(labelCredit, 0);
		
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
		if (obj instanceof JButton) {
			JButton b = (JButton) obj;
			
			System.out.println("Mouse Clicked : [" + b.getName() + "] Button");
			if (b == button[0]) {
				FormUtil.getInstance().createForm("Playing", PlayForm.class);
				FormUtil.getInstance().changeForm("Playing");
			} else if (b == button[1]) {
    			FormUtil.getInstance().createForm("Option", SettingForm.class);
    			FormUtil.getInstance().changeForm("Option");
			} else if (b == button[2]) {
				//FormUtil.getInstance().createForm("", .class);
				//FormUtil.getInstance().changeForm("");
			}
		}
	}
    public void mouseEntered(MouseEvent e) {
    	Object obj = e.getSource();
    	if (obj instanceof JButton) {
    		((JButton) obj).requestFocusInWindow();
    	}
    }
    public void mouseDragged(MouseEvent e){}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e){}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    // KeyListener
    public void keyPressed(KeyEvent e) {
		Object obj = e.getSource();
		if (obj instanceof JButton) {
			JButton b = (JButton) obj;
			
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("[ENTER] Key Pressed : [" + b.getName() + "] Button");
				if (b == button[0]) {
					FormUtil.getInstance().createForm("Playing", PlayForm.class);
					FormUtil.getInstance().changeForm("Playing");
				} else if (b == button[1]) {
					FormUtil.getInstance().createForm("Option", SettingForm.class);
					FormUtil.getInstance().changeForm("Option");
				} else if (b == button[2]) {
					// TODO: SoundTestの実装を検討する
					//FormUtil.getInstance().createForm("", .class);
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
		if (obj instanceof JButton) {
			JButton b = (JButton) e.getSource();
			
			if (b == button[0]) {
				labelCursor.setLocation(posCursor[0]);
			} else if (b == button[1]) {
				labelCursor.setLocation(posCursor[1]);
			} else if (b == button[2]) {
				labelCursor.setLocation(posCursor[2]);
			}
		}
    }
    public void focusLost(FocusEvent e) {}
    
    public void onFormChanged(){
    	for(JButton item : button){
    		item.removeKeyListener(this);
    		item.removeMouseListener(this);
    		item.removeFocusListener(this);
    	}
    }
    
    public void onClose(){}
    
    private static final int TITLE_MARGIN = 50;
    private static final int BUTTON_INTERVAL = 20;
    private static final int BUTTON_MARGIN = 250;
    private static final int BUTTON_NUMBER = 3;
    private static final int CREDIT_MARGIN = 100;
    private static final String PATH = "resource/image/title/";
    private static final String[] BUTTON_NAME = {"Playing", "Option", "Music"};
}
