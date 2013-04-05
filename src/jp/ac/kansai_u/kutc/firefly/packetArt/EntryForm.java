import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jnetpcap.packet.PcapPacket;

/**
 * 最初に表示されるフォームです.
 * @author midolin
 */
public class EntryForm extends FormBase {
	long tick;
	int limit;
	Point[] cursor;
	int[] time;
	int count;
	String fileName;
	JButton loadButton;
	JButton loadButton2;//TEST
    PcapManager pcapManager;//TEST
   // TcpHandler tcpHandler;
    Liner liner;
	boolean isChanging;

	// あらゆるオブジェクトの初期化はここから(jnetpcap関連クラスなど)
	// あくまでフォームなのでフォームを使ってなんでもやらないこと推奨
	public void initialize() {
		tick = 0;
		count = 0;
		limit = 50;
		cursor = new Point[50];
		time = new int[50];
        pcapManager = new PcapManager();//TEST
     //   tcpHandler = new TcpHandler();//TEST
        liner = new Liner(getSize().width,getSize().height);

		setBackground(Color.white);

		// ファイルを開くボタンを指定する部分
		loadButton = new JButton("ファイルを開く");
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				if((int)chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION)
					fileName = chooser.getSelectedFile().getAbsolutePath();
                    if (fileName != null) {
                        if( pcapManager.isReadyRun() ) {
                            loadButton.setText("すでにロードされています。");
                        } else {
                            pcapManager.openFile(fileName);
                            fileName = null;
                            if (pcapManager.isReadyRun() == true) {
                                //loadButton.setText("Pcapファイルが正しくロードされました。");
                                loadButton2.setVisible(false);
                                loadButton.setVisible(false);
                            }
                        }
                    }
			}
		});
		loadButton.setBounds((getSize().width / 3) , (getSize().height / 5) * 3, getSize().width / 3, getSize().height / 5);
		getContentPane().add(loadButton, 0);

        loadButton2 = new JButton("デバイスのIPアドレスから開く");//TEST
        loadButton2.addActionListener(new ActionListener(){//TEST
            public void actionPerformed(ActionEvent e){//TEST
                    String ipAddress = JOptionPane.showInputDialog("IPアドレスを入力してください", "IPv4もしｋはIPv6");//TEST
                    if (ipAddress != null) {
                        if( pcapManager.isReadyRun() ) {
                            loadButton2.setText("すでにロードされています。");
                        } else {
                            pcapManager.openString(ipAddress);
                            ipAddress = null;
                            if (pcapManager.isReadyRun() == true) {
                                //loadButton2.setText("Pcapファイルが正しくロードされました。");
                                loadButton2.setVisible(false);
                                loadButton.setVisible(false);
                            }
                        }
                    }
            }
        });
        loadButton2.setBounds((getSize().width / 4) , (getSize().height / 5) * 1, getSize().width / 2, getSize().height / 5);//TST
        getContentPane().add(loadButton2, 1);

		// ファイルを開くボタンを配置する 0はレイヤー番号
		getContentPane().add(loadButton, 0);

	}

	// 描画関連のコードはここに
	public void paint(Graphics g) {
		// アンチエイリアス
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			getAntiAlias() ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);

		g.setColor(Color.getHSBColor(360.0f / (tick % 360.0f), 0.5f, 1.0f));
		g.fillRect(0, 0, 25, 25);

		for (int i = 0; i < limit ; i++) {
			if(cursor[i] != null) {
				g.setColor(Color.getHSBColor(360.0f / (time[i] % 360.0f), 0.5f, 0.9f));
				g.fillOval((int)cursor[i].getX() - 25, (int)cursor[i].getY() - 25, 50, 50);
			}
		}
        liner.paint(g,cursor);
       // tcpHandler.paint(g,cursor,getSize().width,getSize().height);
	}

	// viewとlogicの分離を考えるときはcommandパターンのようなものでも使ってください
	// パケット解析などはこのメソッドからどうぞ
	public void update() {
        if ( pcapManager.isReadyRun() ) {
            if (tick % 1000 == 0) {
            PcapPacket pkt = pcapManager.nextPacket();
         //   tcpHandler.inspect(pkt);
            liner.inspect(pkt);
            }
        } else {
            loadButton2.setVisible(true);
            loadButton.setVisible(true);
        //    System.err.println("GIVE ME MORE PCAP..");
            //再度pcapファイルを開くように促す
        }
		tick++;
		// 5秒のはず 正確に時間を取りたい場合は別スレッドで動かすこと
		//if(tick > 3000 && !isChanging){
		/*
		if(false && tick > 3000 && !isChanging){
			isChanging = true;
			FormUtil.getInstance().createForm("TemplateForm", TemplateForm.class);
			FormUtil.getInstance().changeForm("TemplateForm");
		}
		*/
	}

	// 使いたい入力イベントを実装、記述してください
	// Eventを切り離すときれいに見えますがめんどくさくなります
	// MouseClickedとMousePressedの違い:
	// Clicked:押して、離してから反応、ダブルクリックにも対応
	// Pressed:押したら反応
	public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) {
		System.out.print(count);
		System.out.println(Thread.currentThread());

		if(count > limit)
			count = 0;
		else
			count++;

		synchronized(cursor){
			time[count] = (int)tick;
			cursor[count] = e.getPoint();
		}

    }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }

    public void mouseMoved(MouseEvent e){ }
    public void mouseDragged(MouseEvent e){ }

    public void keyPressed(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }

    public void onFormChanged(){ }
    public void onClose(){ }
}
