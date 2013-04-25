package jp.ac.kansai_u.kutc.firefly.packetArt.setting;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class ConfigFrame extends JFrame implements ActionListener{
	ConfigStatus stat;
	ConfigStatusMainPanel madoka;
	JButton mami1, mami2;
	/**
	 * @param args
	 */
	ConfigFrame(ConfigStatus stat){
		
		this.stat = stat;
		setTitle("Configuration");
		setSize(600, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		
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
	    KeyConfigPanel qbee = new KeyConfigPanel(stat.getKey());
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
	    
	    homura.add(sayaka);
	    homura.add(madoka);
	    homura.add(kusojo);
	    homura.add(qbee);
	    homura.add(kyoko);
	    
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == mami1){
			stat.setViewLog(madoka.panelViewLog.getStatus());
			stat.setMino(madoka.panelMino.getStatus());
			stat.setVolMusic(madoka.panelMusic.getStatus());
			stat.setVolSound(madoka.panelSound.getStatus());
			stat.setDifficulty(madoka.panelDifficulty.getStatus());
			stat.printStatus();
		}else if(e.getSource() == mami2)
			System.out.println("CANCEL pushed");
	}
}
