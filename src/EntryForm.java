import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// このファイルがクラスの基本的な構造と使い方
/**
 * 最初に表示されるフォームです.
 */
public class EntryForm extends FormBase {
	long tick;
	int limit;
	Point[] cursor;
	int[] time;
	int count;
	String fileName;
	JButton loadButton;

	// あらゆるオブジェクトの初期化はここから(jnetpcap関連クラスなど)
	// あくまでフォームなのでフォームを使ってなんでもやらないこと推奨
	public void initialize() {
		tick = 0;
		count = 0;
		limit = 50;
		cursor = new Point[50];
		time = new int[50];

		addMouseListener(this);
		setBackground(Color.white);

		loadButton = new JButton("ファイルを開く");
		loadButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser();
				if((int)chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION)
					fileName = chooser.getSelectedFile().getAbsolutePath();

				System.out.println(fileName);
			}
		});
		loadButton.setBounds((getSize().width / 3) , (getSize().height / 5) * 3, getSize().width / 3, getSize().height / 5);
		getContentPane().add(loadButton, 0);

	}

	// 描画関連のコードはここに
	public void paint(Graphics g) {
		for (int i = 0; i < 50 ; i++) {
			if(cursor[i] != null) {
				g.setColor(Color.getHSBColor(360.0f / (time[i] % 360.0f), 0.8f, 0.8f));
				g.fillOval((int)cursor[i].getX() - 25, (int)cursor[i].getY() - 25, 50, 50);
			}
		}
	}

	// viewとlogicの分離を考えるときはcommandパターンのようなものでも使ってください
	// パケット解析などはこのメソッドからどうぞ
	public void update() {
		tick++;
	}

	// 使いたい入力イベントを実装、記述してください
	// Eventを切り離すときれいに見えますがめんどくさくなります
	// TODO:謎のダブルクリック問題を解消する(シングルクリックが2回反応する)
	public void mouseClicked(MouseEvent e) {
		System.out.println(count);

		time[count] = (int)tick;
		cursor[count] = e.getPoint();
		if(count >= limit)
			count = 0;
		else
			count++;
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e){

    }
    public void mouseDragged(MouseEvent e){
    }

    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
}
