package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.event.KeyEvent;

/**
 * 設定項目のデータ構造
 * @author akasaka
 */
public class ConfigStatus {
	final static byte MINO4=0, MINO5=1, MINOBOTH=2;
	final static byte MUTE=0;
	final static byte BGMLOW=50, BGMMEDIUM=75, BGMHIGH=100;
	final static byte SELOW=1, SEMEDIUM=2, SEHIGH=3;
	final static byte STATIC=0, DYNAMIC=1, AUTO=2;
	final static byte KEYDEFAULT=0, KEYGAMER=1, KEYVIM=2;
	private static boolean viewLog;		// ビューの表示のオン・オフ
	private static byte    mino;		// ミノの設定（4つ，5つ，両方）
	private static byte    volMusic;	// 音楽の音量の設定（Mute, Low, Medium, High）
	private static byte    volSound;	// 効果音の音量の設定（Mute, Low, Medium, High）
	private static byte    difficulty;	// 難易度（静的，動的，自動）
	private static byte    keybind;		// キーバインド（Default, Gamer, Vim）
	private static int    up, down, left, right, leftSpin, rightSpin;	// キー
	private static int[]  key;			// キーコンフィグの配列（up, down, left, right, leftSpin, rightSpin)
	
	final static int DEFAULTKEYCODE[] =
			new int[]{KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ALT, KeyEvent.VK_SPACE};
	final static int GAMERKEYCODE[] =
			new int[]{KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_J, KeyEvent.VK_K};
	final static int VIMKEYCODE[] =
			new int[]{KeyEvent.VK_K, KeyEvent.VK_J, KeyEvent.VK_H, KeyEvent.VK_L, KeyEvent.VK_A, KeyEvent.VK_S};
	final static int[][] KEYBIND= new int[][]{DEFAULTKEYCODE, GAMERKEYCODE, VIMKEYCODE};
	
	/**
	 * コンストラクタ
	 */
	public ConfigStatus(){
		viewLog = false;
		mino = MINO4;
		volMusic = MUTE; //TODO :実装時，MEDIUMに．
		volSound = SEMEDIUM;
		difficulty = AUTO;
		keybind = KEYDEFAULT;
		up = KEYBIND[keybind][0]; down = KEYBIND[keybind][1];
		left = KEYBIND[keybind][2]; right = KEYBIND[keybind][3];
		leftSpin = KEYBIND[keybind][4]; rightSpin = KEYBIND[keybind][5];
		key = new int[]{up, down, left, right, leftSpin, rightSpin};
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
	 * キーバインドを設定する
	 * @param keybind
	 */
	public static void setKeyBind(byte b){ keybind = b; }
	/**
	 * 上キーを設定する
	 * @param up
	 */
	public static void setKeyUp(int d){ up = key[0] = d;}
	/**
	 * 下キーを設定する
	 * @param down
	 */
	public static void setKeyDown(int d){ down = key[1] = d; }
	/**
	 * 左キーを設定する
	 * @param left
	 */
	public static void setKeyLeft(int d){ left = key[2] = d; }
	/**
	 * 右キーを設定する
	 * @param right
	 */
	public static void setKeyRight(int d){ right = key[3] = d; }
	/**
	 * 左回転キーを設定する
	 * @param leftSpin
	 */
	public static void setKeyLeftSpin(int d){ leftSpin = key[4] = d; }
	/**
	 * 右回転キーを設定する
	 * @param rightSpin
	 */
	public static void setKeyRightSpin(int d){ rightSpin = key[5] = d; }
	
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
	 * キーバインドの設定を取得する
	 * @return キーバインド（Default, Gamer, Vim）
	 */
	public static byte getKeyBind(){ return keybind; }
	/**
	 * 上キーの設定を取得する
	 * @return 上キー
	 */
	public static int getKeyUp(){ return up; }
	/**
	 * 下キーの設定を取得する
	 * @return 下キー
	 */
	public static int getKeyDown(){ return down; }
	/**
	 * 左キーの設定を取得する
	 * @return 左キー
	 */
	public static int getKeyLeft(){ return left; }
	/**
	 * 右キーの設定を取得する
	 * @return 右キー
	 */
	public static int getKeyRight(){ return right; }
	/**
	 * 左回転キーの設定を取得する
	 * @return 左回転キー
	 */
	public static int getKeyLeftSpin(){ return leftSpin; }
	/**
	 * 右回転キーの設定を取得する
	 * @return 右回転キー
	 */
	public static int getKeyRightSpin(){ return rightSpin; }
	/**
	 * 各キーの配列を取得する
	 * @return 各キーの配列
	 */
	public static int[] getKey(){ return key; }
	
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
		System.out.println(keybind);
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
