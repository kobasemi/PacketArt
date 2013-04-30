package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

/**
 * 設定項目のデータ構造
 * @author akasaka
 */
public class ConfigStatus {
	final static byte MUTE=0;
	final static byte MINO4=0, MINO5=1, MINOBOTH=2;
	final static byte BGMLOW=50, BGMMEDIUM=75, BGMHIGH=100;
	final static byte SELOW=1, SEMEDIUM=2, SEHIGH=3;
	final static byte STATIC=0, DYNAMIC=2, AUTO=3;
	private static boolean viewLog;		// ビューの表示のオン・オフ
	private static byte    mino;		// ミノの設定（4つ，5つ，両方）
	private static byte    volMusic;	// 音楽の音量の設定（Mute, Low, Medium, High）
	private static byte    volSound;	// 効果音の音量の設定（Mute, Low, Medium, High）
	private static byte    difficulty;	// 難易度（静的，動的，自動）
	private static char    up, down, left, right, leftSpin, rightSpin;	// キーコンフィグ
	private static char[]  key;			// キーコンフィグの配列（up, down, left, right, leftSpin, rightSpin)
	
	/**
	 * コンストラクタ
	 */
	public ConfigStatus(){
		viewLog = false;
		mino = MINO4;
		volMusic = MUTE; //TODO :実装時，MEDIUMに．
		volSound = SEMEDIUM;
		difficulty = AUTO;
		up = 'i'; down = 'k'; left = 'j'; right = 'l';
		leftSpin = 'z'; rightSpin = 'x';
		key = new char[]{up, down, left, right, leftSpin, rightSpin};
	}
	
	/**
	 * ログ表示のオン・オフを設定する
	 * @param viewLog
	 */
	public static void setViewLog(boolean b){ viewLog = b; }
	/**
	 * ミノの出現個数を設定する
	 * @param mino
	 */
	public static void setMino(byte b){ mino = b; }
	/**
	 * 音楽の音量を設定する
	 * @param volMusic
	 */
	public static void setVolMusic(byte b){ volMusic = b; }
	/**
	 * 効果音の音量を設定する
	 * @param volSound
	 */
	public static void setVolSound(byte b){ volSound = b; }
	/**
	 * 難易度を設定する
	 * @param difficulty
	 */
	public static void setDifficulty(byte b){ difficulty = b; }
	/**
	 * 上キーを設定する
	 * @param up
	 */
	public static void setKeyUp(char c){ up = key[0] = c;}
	/**
	 * 下キーを設定する
	 * @param down
	 */
	public static void setKeyDown(char c){ down = key[1] = c; }
	/**
	 * 左キーを設定する
	 * @param left
	 */
	public static void setKeyLeft(char c){ left = key[2] = c; }
	/**
	 * 右キーを設定する
	 * @param right
	 */
	public static void setKeyRight(char c){ right = key[3] = c; }
	/**
	 * 左回転キーを設定する
	 * @param leftSpin
	 */
	public static void setKeyLeftSpin(char c){ leftSpin = key[4] = c; }
	/**
	 * 右回転キーを設定する
	 * @param rightSpin
	 */
	public static void setKeyRightSpin(char c){ rightSpin = key[5] = c; }
	
	/**
	 * ログの表示設定を取得する
	 * @return ログの表示のオン・オフ（true, false）
	 */
	public static boolean isViewLog(){ return viewLog; }
	/**
	 * ミノの出現個数設定を取得する
	 * @return ミノの個数（4つ, 5つ, 両方）
	 */
	public static byte getMino(){ return mino; }
	/**
	 * 音楽の音量設定を取得する
	 * @return 音楽の音量（Mute, Low, Medium, High）
	 */
	public static byte getVolMusic(){ return volMusic; }
	/**
	 * 効果音の音量設定を取得する
	 * @return 効果音の音量（Mute, Low, Medium, High）
	 */
	public static byte getVolSound(){ return volSound; }
	/**
	 * 難易度の設定を取得する
	 * @return 難易度（Static, Dynamic, Auto）
	 */
	public static byte getDifficulty(){ return difficulty; }
	/**
	 * 上キーの設定を取得する
	 * @return 上キー
	 */
	public static char getKeyUp(){ return up; }
	/**
	 * 下キーの設定を取得する
	 * @return 下キー
	 */
	public static char getKeyDown(){ return down; }
	/**
	 * 左キーの設定を取得する
	 * @return 左キー
	 */
	public static char getKeyLeft(){ return left; }
	/**
	 * 右キーの設定を取得する
	 * @return 右キー
	 */
	public static char getKeyRight(){ return right; }
	/**
	 * 左回転キーの設定を取得する
	 * @return 左回転キー
	 */
	public static char getKeyLeftSpin(){ return leftSpin; }
	/**
	 * 右回転キーの設定を取得する
	 * @return 右回転キー
	 */
	public static char getKeyRightSpin(){ return rightSpin; }
	/**
	 * 各キーの配列を取得する
	 * @return 各キーの配列
	 */
	public static char[] getKey(){ return key; }
	
//	最終的には消すけど，今は変数確認用に使う
	/**
	 * 各項目の設定を標準出力する
	 */
	public static void printStatus(){
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
