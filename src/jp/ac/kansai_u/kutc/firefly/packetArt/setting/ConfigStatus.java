package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

/**
 * 設定項目のデータ構造
 * @author akasaka
 */
public class ConfigStatus {
	private boolean viewLog;	// ビューの表示のオン・オフ
	private byte    mino;		// ミノの設定（4つ，5つ，両方）
	private byte    volMusic;	// 音楽の音量の設定（Mute, Low, Medium, High）
	private byte    volSound;	// 効果音の音量の設定（Mute, Low, Medium, High）
	private byte    difficulty;	// 難易度（静的，動的，自動）
	private char    up, down, left, right, leftSpin, rightSpin;	// キーコンフィグ
	private char[]  key;		// キーコンフィグの配列（up, down, left, right, leftSpin, rightSpin)
	
	
	/**
	 * コンストラクタ
	 */
	public ConfigStatus(){
		viewLog = false;
		mino = 0;
		volMusic = 2;
		volSound = 2;
		difficulty = 2;
		up = 'i';
		down = 'k';
		left = 'j';
		right = 'l';
		leftSpin = 'z';
		rightSpin = 'x';
		key = new char[]{up, down, left, right, leftSpin, rightSpin};
	}
	
	/**
	 * ログ表示のオン・オフを設定する
	 * @param viewLog
	 */
	public void setViewLog(boolean viewLog){ this.viewLog = viewLog; }
	/**
	 * ミノの出現個数を設定する
	 * @param mino
	 */
	public void setMino(byte mino){ this.mino = mino; }
	/**
	 * 音楽の音量を設定する
	 * @param volMusic
	 */
	public void setVolMusic(byte volMusic){ this.volMusic = volMusic; }
	/**
	 * 効果音の音量を設定する
	 * @param volSound
	 */
	public void setVolSound(byte volSound){ this.volSound = volSound; }
	/**
	 * 難易度を設定する
	 * @param difficulty
	 */
	public void setDifficulty(byte difficulty){ this.difficulty = difficulty; }
	/**
	 * 上キーを設定する
	 * @param up
	 */
	public void setUp(char up){ this.up = this.key[0] = up;}
	/**
	 * 下キーを設定する
	 * @param down
	 */
	public void setDown(char down){ this.down = this.key[1] = down; }
	/**
	 * 左キーを設定する
	 * @param left
	 */
	public void setLeft(char left){ this.left = this.key[2] = left; }
	/**
	 * 右キーを設定する
	 * @param right
	 */
	public void setRight(char right){ this.right = this.key[3] = right; }
	/**
	 * 左回転キーを設定する
	 * @param leftSpin
	 */
	public void setLeftSpin(char leftSpin){	this.leftSpin = this.key[4] = leftSpin; }
	/**
	 * 右回転キーを設定する
	 * @param rightSpin
	 */
	public void setRightSpin(char rightSpin){ this.rightSpin = this.key[5] = rightSpin; }
	/**
	 * 各キーの設定をキー配列にセットする
	 */
	public void setKey(){
		this.key[0] = this.up;
		this.key[1] = this.down;
		this.key[2] = this.left;
		this.key[3] = this.right;
		this.key[4] = this.leftSpin;
		this.key[5] = this.rightSpin;
	}
	
	/**
	 * ログの表示設定を取得する
	 * @return ログの表示のオン・オフ（true, false）
	 */
	public boolean isViewLog(){ return viewLog; }
	/**
	 * ミノの出現個数設定を取得する
	 * @return ミノの個数（0_4つ, 1_5つ, 2_両方）
	 */
	public byte getMino(){ return mino; }
	/**
	 * 音楽の音量設定を取得する
	 * @return 音楽の音量（0_Mute, 1_Low, 2_Medium, 3_High）
	 */
	public byte getVolMusic(){ return volMusic; }
	/**
	 * 効果音の音量設定を取得する
	 * @return 効果音の音量（0_Mute, 1_Low, 2_Medium, 3_High）
	 */
	public byte getVolSound(){ return volSound; }
	/**
	 * 難易度の設定を取得する
	 * @return 難易度（0_Static, 1_Dynamic, 2_Auto）
	 */
	public byte getDifficulty(){ return difficulty; }
	/**
	 * 上キーの設定を取得する
	 * @return 上キー（char）
	 */
	public char getUp(){ return up; }
	/**
	 * 下キーの設定を取得する
	 * @return 下キー（char）
	 */
	public char getDown(){ return down; }
	/**
	 * 左キーの設定を取得する
	 * @return 左キー（char）
	 */
	public char getLeft(){ return left; }
	/**
	 * 右キーの設定を取得する
	 * @return 右キー（char）
	 */
	public char getRight(){ return right; }
	/**
	 * 右回転キーの設定を取得する
	 * @return 右回転キー（char）
	 */
	public char getRightSpin(){ return rightSpin; }
	/**
	 * 左回転キーの設定を取得する
	 * @return 左回転キー（char）
	 */
	public char getLeftSpin(){ return leftSpin; }
	/**
	 * 各キーの配列を取得する
	 * @return 各キーのの配列（char []）
	 */
	public char[] getKey(){ return key; }
	
	/**
	 * 各項目の設定状況を標準出力する
	 */
	public void printStatus(){
		System.out.println(viewLog);
		System.out.println(mino);
		System.out.println(volMusic);
		System.out.println(volSound);
		System.out.println(difficulty);
		System.out.println(up);
		System.out.println(down);
		System.out.println(left);
		System.out.println(right);
		System.out.println(leftSpin);
		System.out.println(rightSpin);
		for(int i=0; i<6; i++)
			System.out.print(key[i] + " ");
		System.out.println();
	}
}
