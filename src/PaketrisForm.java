import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * パケットを利用したテトリスを表示、処理するフォームです。
 * @author midolin
 */
public class PaketrisForm extends FormBase {
	PacketrisPacketHandler pktHandler;
	LinkedList<Integer> keyQueue;
	HashMap<Integer, Long> keyPressedTime;
	int keySensitivity = 10;
	long tick;

	// Tetrisは10*20、外枠を含めて12*22
	int height = 22,width = 12 ;
    int[][] board;
	
	public int getKeySensitivity() {
		return keySensitivity;
	}

	/**
	 * キー入力の敏感さを設定します。<br>
	 * キー入力の敏感さとは、キーが押しっぱなしにされていた時に、どのくらいの時間押しっぱなしにされていた場合、
	 * 連続入力とみなすかを設定する値です。<br>
	 * 0から60までの値を取り、それ以外の値が入力されると、その範囲に収まるように補正されます。
	 * @param keySensitivity 敏感さ。0から60(フレーム)。デフォルトで10f;
	 */
	public void setKeySensitivity(int keySensitivity) {
		// 範囲外なら補正する
		if(keySensitivity < 0)
			keySensitivity = 0;
		else if(keySensitivity > 60)
			keySensitivity = 60;
		
		this.keySensitivity = keySensitivity;
	}

    public PaketrisForm() {
    	keyQueue = new LinkedList<Integer>();
    }

    @Override
    public void initialize() {
        // TODO 自動生成されたメソッド・スタブ
        board = new int[height][width];
    }

    @Override
    public void paint(Graphics g) {
        // TODO 自動生成されたメソッド・スタブ

    }

	/**
	 * 指定された座標を左上として、ブロックを描画します。
	 * @param g
	 * @param x
	 * @param y
	 */
	void drawBlock(Graphics g, int x, int y){

	}

    @Override
    public void update() {
    	// 入力されたキーを配列へ
    	List<Integer> keys = new ArrayList<Integer>();
    	while(keyQueue.size() != 0){
    		// 未来の話なら抜ける(そんなことあり得るのか)
    		if(keyPressedTime.get(keyQueue.get(0)) > tick)
    			break;
	    	if(keyPressedTime.get(keyQueue.get(0)) < tick);
		    	keys.add(keyQueue.pop());
    	}
    	
    	tick++;
    }

    /**
     * 行を削除
     */
    void elase(){

    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();
    	long time = tick;
    	;
    	// TODO:1回だけ押せるようにする(長押し(何ms?)で連続反応するようにする)
    	if(!keyPressedTime.containsKey(key)) {
			keyPressedTime.put(key, time);
			keyQueue.push(key);
    	}		
    	// TODO:入力キューに入力を入れる(長押し時に入れすぎても爆発するだけ)
    	if(keyPressedTime.get(key) - time > keySensitivity)
	    	keyQueue.push(key);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	keyPressedTime.remove(e.getKeyCode());
    }

    @Override
    public void mouseDragged(MouseEvent e) { }
    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void onClose() { }

    @Override
    public void onFormChanged() { }

}
