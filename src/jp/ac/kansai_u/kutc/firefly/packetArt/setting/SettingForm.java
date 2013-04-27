package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
	ConfigStatus stat;
	ConfigStatusMainPanel madoka;
	JButton mami1, mami2;
	KeyConfigPanel qbee;
	
	public void initialize() {
		stat = new ConfigStatus();
		Container homura = getContentPane();
		
		JLabel sayaka = new JLabel("設定画面");
		sayaka.setFont(new Font("Self", Font.PLAIN, 48));
		sayaka.setBounds(30, 30, 540, 100);
		sayaka.setBackground(Color.red);
		sayaka.setOpaque(true);
		sayaka.setHorizontalAlignment(JLabel.CENTER);
		
		madoka= new ConfigStatusMainPanel(stat);
	    madoka.setBounds(30, 130, 540, 250);
	    
	    JLabel kusojo = new JLabel("キーコンフィグ");
	    kusojo.setBounds(30, 380, 540, 50);
	    qbee = new KeyConfigPanel(stat.getKey());
	    qbee.setBounds(30, 430, 540, 250);
	    
	    JPanel kyoko = new JPanel();
	    kyoko.setBounds(30, 680, 540, 70);
	    kyoko.setBackground(Color.green);
	    mami1 = new JButton("設定");
	    mami2 = new JButton("キャンセル");
	    mami1.addActionListener(this);
	    mami2.addActionListener(this);
	    kyoko.add(mami1);
	    kyoko.add(mami2);
	    
	    homura.add(sayaka, 0);
	    homura.add(madoka, 0);
	    homura.add(kusojo, 0);
	    homura.add(qbee, 0);
	    homura.add(kyoko, 0);
	}
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == mami1){
			stat.setViewLog(madoka.panelViewLog.getStatus());
			stat.setMino(madoka.panelMino.getStatus());
			stat.setVolMusic(madoka.panelMusic.getStatus());
			stat.setVolSound(madoka.panelSound.getStatus());
			stat.setDifficulty(madoka.panelDifficulty.getStatus());
			stat.setUp(qbee.labelUp.getText().charAt(0));
			stat.setDown(qbee.labelDown.getText().charAt(0));
			stat.setLeft(qbee.labelLeft.getText().charAt(0));
			stat.setRight(qbee.labelRight.getText().charAt(0));
			stat.setLeftSpin(qbee.labelLeftSpin.getText().charAt(0));
			stat.setRightSpin(qbee.labelRightSpin.getText().charAt(0));
			stat.setKey();
			stat.printStatus();
		}else if(e.getSource() == mami2){
			System.out.println("CANCEL pushed");
			FormUtil.getInstance().changeForm("Title");
		}
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