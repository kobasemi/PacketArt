package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormUtil;


/**
 * 設定画面のメイン
 * @author akasaka
 */
public class SettingForm extends FormBase implements ActionListener {
	private ConfigStatusMainPanel madoka;
	private JButton mami1, mami2;
	private KeyBindPanel qbee;
	
	private BufferedImage image;
	
	public void initialize() {
		try{
			image = ImageIO.read(new File(ConfigInfo.IMGPATH + "background3.png"));
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		
		
		Container homura = getContentPane();
		homura.setBackground(Color.black);
//		homura.setLayout(new BoxLayout(homura, BoxLayout.Y_AXIS));
		JLabel walpurgisNight = new JLabel(new ImageIcon(image));
		walpurgisNight.setBounds(0, 0, image.getWidth(), image.getHeight());
		
		JLabel sayaka = new JLabel("設定画面");
		sayaka.setFont(new Font("Self", Font.PLAIN, 48));
		sayaka.setBounds(30, 30, 540, 100);
		sayaka.setForeground(Color.white);
		sayaka.setOpaque(false);
		sayaka.setHorizontalAlignment(JLabel.CENTER);
		
		madoka= new ConfigStatusMainPanel();
	    
	    qbee = new KeyBindPanel(ConfigStatus.getKeyBind());
	    
	    JPanel kyoko = new JPanel();
	    kyoko.setBounds(30, 680, 540, 100);
	    kyoko.setOpaque(false);
	    mami1 = new JButton(new ImageIcon(ConfigInfo.IMGPATH + "btnSetting.png"));
	    mami2 = new JButton(new ImageIcon(ConfigInfo.IMGPATH + "btnCancel.png"));
	    mami1.setContentAreaFilled(false);
	    mami2.setContentAreaFilled(false);
	    mami1.addActionListener(this);
	    mami2.addActionListener(this);
	    kyoko.add(mami1);
	    kyoko.add(mami2);
	    
	    homura.add(walpurgisNight, 0);
	    homura.add(sayaka, 0);
	    homura.add(madoka, 0);
//	    homura.add(kusojo, 0);
	    homura.add(qbee  , 0);
	    homura.add(kyoko , 0);
	}
	
	
	public void actionPerformed(ActionEvent e){
		madoka.panelMusicVolume.thread.stop();
		if(e.getSource() == mami1){
			ConfigStatus.setViewLog(madoka.panelViewLog.getStatus());
			ConfigStatus.setMino(madoka.panelMino.getStatus());
			ConfigStatus.setVolMusic(madoka.panelMusicVolume.getStatus());
			ConfigStatus.setVolSound(madoka.panelSoundVolume.getStatus());
			ConfigStatus.setDifficulty(madoka.panelDifficulty.getStatus());
			ConfigStatus.setKeyBind(qbee.getStatus());
			ConfigStatus.setKeyUp(ConfigStatus.KEYBIND[qbee.getStatus()][0]);
			ConfigStatus.setKeyDown(ConfigStatus.KEYBIND[qbee.getStatus()][1]);
			ConfigStatus.setKeyLeft(ConfigStatus.KEYBIND[qbee.getStatus()][2]);
			ConfigStatus.setKeyRight(ConfigStatus.KEYBIND[qbee.getStatus()][3]);
			ConfigStatus.setKeyLeftSpin(ConfigStatus.KEYBIND[qbee.getStatus()][4]);
			ConfigStatus.setKeyRightSpin(ConfigStatus.KEYBIND[qbee.getStatus()][5]);
			ConfigStatus.printStatus();
			FormUtil.getInstance().changeForm("Title");
		}else if(e.getSource() == mami2){
			System.out.println("CANCEL pushed");
			FormUtil.getInstance().changeForm("Title");
		}
	}

	// 描画関連のコードはここに
	public void paint(Graphics g) {
//		Graphics2D g2D = (Graphics2D) g;
//
//        double imageWidth = image.getWidth();
//        double imageHeight = image.getHeight();
//        double panelWidth = this.getWidth();
//        double panelHeight = this.getHeight();
//
//        // 画像がコンポーネントの何倍の大きさか計算
//        double sx = (panelWidth / imageWidth);
//        double sy = (panelHeight / imageHeight);
//
//        // スケーリング
//        AffineTransform af = AffineTransform.getScaleInstance(sx, sy);
//        g2D.drawImage(image, af, this);
	}

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