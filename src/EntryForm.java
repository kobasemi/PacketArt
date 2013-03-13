import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
	boolean isChanging;

	// あらゆるオブジェクトの初期化はここから(jnetpcap関連クラスなど)
	// あくまでフォームなのでフォームを使ってなんでもやらないこと推奨
	public void initialize() {
		tick = 0;
		count = 0;
		limit = 50;
		cursor = new Point[50];
		time = new int[50];

		setBackground(Color.white);

		// ファイルを開くボタンを指定する部分
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
	}

	// viewとlogicの分離を考えるときはcommandパターンのようなものでも使ってください
	// パケット解析などはこのメソッドからどうぞ
	public void update() {
		tick++;
		// 5秒のはず 正確に時間を取りたい場合は別スレッドで動かすこと
		if(tick > 3000 && !isChanging){
			isChanging = true;
			FormUtil.getInstance().createForm("TemplateForm", TemplateForm.class);
			FormUtil.getInstance().changeForm("TemplateForm");
		}
	}

	// 使いたい入力イベントを実装、記述してください
	// Eventを切り離すときれいに見えますがめんどくさくなります
	// TODO:謎のダブルクリック問題を解消する(シングルクリックが2回反応する)
	public void mouseClicked(MouseEvent e) {

    }
    public void mousePressed(MouseEvent e) {
		System.out.print(count);
		System.out.println(Thread.currentThread());

		synchronized(cursor){
			time[count] = (int)tick;
			cursor[count] = e.getPoint();
		}
		if(count > limit)
			count = 0;
		else
			count++;
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

    public void onFormChanged(){
    }
    public void onClose(){
    }
}
