package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

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
		keyQueue = new LinkedList<Integer>();
		keyPressedTime = new HashMap<Integer, Long>();
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
		model.initialize();
		addKeyListener(this);
	}

	@Override
	public void paint(Graphics g) {
		// TODO: backgrownd
		
		// TODO: painting PacketBlocks using packet data.
		for(PacketBlock item : model.currentMinos) {
			g.setColor(Color.getHSBColor(model.parentLocation.getX() % 360.0f, 0.7f, 0.7f));
			g.drawRect(
					((model.parentLocation.getX() + item.location.getX()) * 16), 
					((model.parentLocation.getY() + item.location.getY()) * 16), 14, 14);
			
		}
		for (PacketBlock[] column : model.getBoard()) {
			for(PacketBlock item : column){
				g.setColor(Color.blue);
				g.drawRect(item.location.getX() * 16, item.location.getY() * 16, 14, 14);
			}
		}
		if(model.isGameOverd()){
			g.setColor(Color.getHSBColor(0.0f, 0.75f, 0.5f));
			g.setFont(new Font(null, Font.PLAIN, 30));
			g.drawString("Game Over", 100, 100);
			
		}

	}

	@Override
	public void update() {
		// 入力されたキーを配列へ
		List<Integer> keys = new ArrayList<Integer>();
		while (keyQueue.size() != 0) {
			// 未来の話なら抜ける(そんなことあり得るのか)
			if (keyPressedTime.get(keyQueue.get(0)) > tick)
				break;
			//if (keyPressedTime.get(keyQueue.get(0)) < tick)
			keys.add(keyQueue.pop());
		}
		
		// ゲームオーバー判定
		if(model.isGameOverd())
			return;
		
		// if pressedkey is configured key then try to operation
		if (false/* */)
			model.rotate(Direction.Left);
		if (false/* */)
			falldownTimer = falldownLimit + 1;
		if (keys.contains(KeyEvent.VK_ENTER)) {
			while (model.fallDown()) {
			}
			falldownTimer = falldownLimit + 1;
		}

		// もし指定のタイミングになったらfalldown
		if (falldownTimer > falldownLimit) {
			System.out.println("falling - " + model.parentLocation.toString());
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
		
		// TODO:1回だけ押せるようにする(長押し(何ms?)で連続反応するようにする)
		if (!keyPressedTime.containsKey(key)) {
			keyPressedTime.put(key, time);
			keyQueue.push(key);
		}

		// TODO:入力キューに入力を入れる(長押し時に入れすぎても爆発するだけ)
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
	}

	@Override
	public void onFormChanged() {
	}

}
