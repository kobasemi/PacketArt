package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.setting.ConfigStatus;

/**
 * パケットを利用したテトリスを表示、処理するフォームです。
 * 
 * @author midolin
 */
public class PlayForm extends FormBase {
	PacketrisModel model;

	LinkedList<Integer> keyQueue;
	HashMap<Integer, Long> keyPressedTime;
	int keySensitivity = 10;
	long tick;
	int falldownLimit;
	long falldownTimer;
	int minoSize;
	boolean isPaused = false;
	
	Point topLeft;

	/**
	 * キー入力に対する敏感さを取得します。この値は0から60までの値をとります。
	 * 
	 * @return キー入力に対する敏感さ
	 */
	public int getKeySensitivity() {
		return keySensitivity;
	}

	/**
	 * キー入力の敏感さを設定します。<br>
	 * キー入力の敏感さとは、キーが押しっぱなしにされていた時に、どのくらいの時間押しっぱなしにされていた場合、 連続入力とみなすかを設定する値です。<br>
	 * 0から60までの値を取り、それ以外の値が入力されると、その範囲に収まるように補正されます。
	 * 
	 * @param keySensitivity
	 *            敏感さ。0から60(フレーム)。デフォルトで10f;
	 */
	public void setKeySensitivity(int keySensitivity) {
		// 範囲外なら補正する
		if (keySensitivity < 0)
			keySensitivity = 0;
		else if (keySensitivity > 60)
			keySensitivity = 60;

		this.keySensitivity = keySensitivity;
	}

	public PlayForm() {
		model = new PacketrisModel();
		falldownLimit = 60;
		addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {
				requestFocusInWindow();
			}
			public void componentResized(ComponentEvent e) { }
			public void componentMoved(ComponentEvent e) { }
			public void componentHidden(ComponentEvent e) { }
		});
	}

	@Override
	public void initialize() {
		keyQueue = new LinkedList<Integer>();
		keyPressedTime = new HashMap<Integer, Long>();
		model.initialize();
		addKeyListener(this);
		minoSize = (int)(Math.min(getPreferredSize().width / model.column, getPreferredSize().height / model.row) * 0.9);
		topLeft = new Point(
				(getSize().width - (minoSize * model.column)) / 2, 
				(getSize().height - (minoSize * model.row)) / 2);
	}

	@Override
	public void paint(Graphics g) {
		// TODO: backgrownd
		
		if(!isPaused){
			for(PacketBlock item : model.currentMinos) {
				paintMino(g, item,
						topLeft.x + ((model.parentLocation.getX() + item.location.getX()) * minoSize), 
						topLeft.y + ((model.parentLocation.getY() + item.location.getY()) * minoSize));
			}
			for (PacketBlock[] column : model.getBoard()) {
				for(PacketBlock item : column){
					paintMino(g, item,
							topLeft.x + item.location.getX() * minoSize, 
							topLeft.y + item.location.getY() * minoSize);
				}
			}
		}
		if(model.isGameOverd()){
			String over = "Game Over";
			
			g.setFont(new Font(null, Font.PLAIN, 45));
			int x = getSize().width / 2 - g.getFontMetrics().stringWidth(over) / 2;
			int y = getSize().height / 4;
			
			// ドロップシャドウのような効果
			g.setColor(Color.BLACK);

			g.drawString(over, x - 3, y);
			g.drawString(over, x + 3, y);
			g.drawString(over, x, y - 3);
			g.drawString(over, x, y + 3);
			
			g.setColor(Color.getHSBColor(0.0f, 0.85f, 0.7f));
			g.drawString(over, x, y);
		}
	}
	
	// TODO: painting PacketBlocks using packet data.
	void paintMino(Graphics g, PacketBlock block, int x, int y){
		if(block.blockType == BlockType.Wall)
			g.setColor(Color.blue);
		else if(block.blockType == BlockType.Mino)
			g.setColor(Color.getHSBColor(model.parentLocation.getX() % 360.0f, 0.7f, 0.7f));
		else
			g.setColor(new Color(0,0,0,0));
		
		g.drawRect(x, y, minoSize - 2, minoSize - 2);
	}

	@Override
	public void update() {
		// 入力されたキーを配列へ
		List<Integer> keys = new ArrayList<Integer>();
		while (keyQueue.size() != 0 && keyPressedTime.size() != 0) {
			// 未来の話なら抜ける(そんなことあり得るのか)
			int tgt = keyQueue.get(0);
			if (keyPressedTime.get(tgt) > tick)
				break;
			//if (keyPressedTime.get(keyQueue.get(0)) < tick)
			keys.add(keyQueue.pop());
		}
		
		// ゲームオーバー判定
		if(model.isGameOverd()){
			// JDK 8のラムダ式が利用できればこんなコードにはならなかった(はず)
			new Thread() {
				@Override
				public void run() {
					try {
						// ボタン表示待ち
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					final JButton[] buttons = new JButton[]{
						new JButton("リトライ"),
						new JButton("タイトルに戻る")
					};
					
					ActionListener actionListener = new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// JButtonの挙動を定義
							if(e.getSource() instanceof JButton){
								if(((JButton) (e.getSource())).getName() == "Retry"){
									// ボタンがRetryならボタンを消して再初期化
									for(JButton item : buttons){
										getContentPane().remove(item);
										item = null;
										getContentPane().validate();
										
									}
									initialize();
								} else {
									// タイトルに帰る
									FormUtil.getInstance().changeForm("Title");
								}
							}
								
						}
					};
					
					// 各種ボタンの設定
					for(int i = 0; i < buttons.length; i++){
						JButton item = buttons[i];
						item.setName(i == 0 ? "Retry" : "Quit");
						item.addActionListener(actionListener);
						item.setLocation(getSize().width / 3, (getSize().height / 4) * (i + 2));
						item.setSize(getSize().width / 3, getSize().height / 10);
						getContentPane().add(item, 0);
					}
				}
			}.run();
			return;
		}

		if (keys.contains(KeyEvent.VK_ESCAPE)){
			isPaused = !isPaused;
			System.out.print(isPaused);
		}
		if(isPaused)
			return;
		
		// キー入力の処理
		if (keys.contains(ConfigStatus.getKeyLeftSpin()))
			model.rotate(Direction.Left);
		if(keys.contains(ConfigStatus.getKeyRightSpin()))
			model.rotate(Direction.Right);
		if(keys.contains(ConfigStatus.getKeyLeft()))
			model.translate(Direction.Left);
		if(keys.contains(ConfigStatus.getKeyRight()))
			model.translate(Direction.Right);
		if (keys.contains(ConfigStatus.getKeyDown())){
			model.fallDown();
			falldownTimer = 0;
		}
		if (keys.contains(ConfigStatus.getKeyUp())) {
			while (model.fallDown()) { }
			falldownTimer = 0;
		}

		
		// もし指定のタイミングになったらfalldown
		if (falldownTimer > falldownLimit) {
			//System.out.println("falling - " + model.parentLocation.toString());
			falldownTimer = 0;
			// 落下に失敗したら、Next生成
			if (!model.fallDown()) {
				System.out.println("generate");
				model.fixMino();
				generateNextBlockFromPacket();
			}
		} else {
			falldownTimer++;
		}

		tick++;
	}

	private void generateNextBlockFromPacket() {
		// TODO ミノの生成方法を決定する
		model.generateMino(TetroMino.T, false, model.column / 2);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		long time = tick;
		
		System.out.println("input key:" + key);
		
		if (!keyPressedTime.containsKey(key)) {
			keyPressedTime.put(key, time);
			keyQueue.push(key);
		}
		
		if (keyPressedTime.get(key) - time > keySensitivity)
			keyQueue.push(key);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyPressedTime.remove(e.getKeyCode());
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void onClose() {
		onFormChanged();
	}

	@Override
	public void onFormChanged() {
		keyQueue = null;
		keyPressedTime = null;
		//model = null;
		removeKeyListener(this);
	}

}
