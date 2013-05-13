package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.event.KeyEvent;

/**
 * 設定項目のデータ構造
 * @author akasaka
 */
public class ConfigStatus {
	private static boolean viewLog;		// ビューの表示のオン・オフ
	private static byte    mino;		// ミノの設定（4つ，5つ，両方）
	private static boolean melody;		// メロディ（明るめ，暗め）
	private static byte    volMusic;	// 音楽の音量の設定（Mute, Low, Medium, High）
	private static byte    volSound;	// 効果音の音量の設定（Mute, Low, Medium, High）
	private static byte    difficulty;	// 難易度（静的，動的，自動）
	private static byte    keybind;		// キーバインド（Default, Gamer, Vim）
	private static int     up, down, left, right, leftSpin, rightSpin;	// キー
	
	//各キーバインドの中身
	final static int DEFAULTKEYCODE[] =
			new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_ALT, KeyEvent.VK_SPACE};
	final static int GAMERKEYCODE[] =
			new int[]{KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_J, KeyEvent.VK_K};
	final static int VIMKEYCODE[] =
			new int[]{KeyEvent.VK_H, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_J, KeyEvent.VK_A, KeyEvent.VK_S};
	final static int[][] KEYBIND= new int[][]{DEFAULTKEYCODE, GAMERKEYCODE, VIMKEYCODE};
	
	/**
	 * コンストラクタ
	 */
	public ConfigStatus(){
		viewLog = false;
		mino = ConfigInfo.MINO4;
		melody = true;
		volMusic = ConfigInfo.MUTE; //TODO :実装時，MEDIUMに．
		volSound = ConfigInfo.VOLSEMEDIUM;
		difficulty = ConfigInfo.AUTO;
		keybind = ConfigInfo.KEYDEFAULT;
		left = KEYBIND[keybind][ConfigInfo.LEFT]; up = KEYBIND[keybind][ConfigInfo.UP];
		right = KEYBIND[keybind][ConfigInfo.RIGHT]; down = KEYBIND[keybind][ConfigInfo.DOWN];
		leftSpin = KEYBIND[keybind][ConfigInfo.LSPIN]; rightSpin = KEYBIND[keybind][ConfigInfo.RSPIN];
	}
	
	/**
	 * ログ表示のオン・オフを設定する
	 * @param f ログの表示のON/OFF
	 */
	public static void setViewLog(boolean f){ viewLog = f; }
	/**
	 * ミノの出現個数を設定する
	 * @param b ミノの個数（4つ, 5つ, 両方）
	 */
	public static void setMino(byte b){ mino = b; }
	/**
	 * メロディ調を設定する
	 * @param f メロディ調（明るめ，暗め）
	 */
	public static void setMelody(boolean f) { melody = f; }
	/**
	 * 音楽の音量を設定する
	 * @param b 音楽の音量（Mute, Low, Medium, High）
	 */
	public static void setVolMusic(byte b){ volMusic = b; }
	/**
	 * 効果音の音量を設定する
	 * @param b 効果音の音量（Mute, Low, Medium, High）
	 */
	public static void setVolSound(byte b){ volSound = b; }
	/**
	 * 難易度を設定する
	 * @param b 難易度（Static, Dynamic, Auto）
	 */
	public static void setDifficulty(byte b){ difficulty = b; }
	/**
	 * キーバインドを設定する
	 * @param b キーバインド（Default, Gamer, Vim）
	 */
	public static void setKeyBind(byte b){ keybind = b; }
	/**
	 * 上キーを設定する
	 * @param d 上キーコード
	 */
	public static void setKeyUp(int d){ up = d;}
	/**
	 * 下キーを設定する
	 * @param d 下キーコード
	 */
	public static void setKeyDown(int d){ down = d; }
	/**
	 * 左キーを設定する
	 * @param d 左キーコード
	 */
	public static void setKeyLeft(int d){ left = d; }
	/**
	 * 右キーを設定する
	 * @param d 右キーコード
	 */
	public static void setKeyRight(int d){ right = d; }
	/**
	 * 左回転キーを設定する
	 * @param d 左回転キーコード
	 */
	public static void setKeyLeftSpin(int d){ leftSpin = d; }
	/**
	 * 右回転キーを設定する
	 * @param d 右回転キーコード
	 */
	public static void setKeyRightSpin(int d){ rightSpin = d; }
	
	/**
	 * ログの表示設定を取得する
	 * @return ログの表示のON/OFF
	 */
	public static boolean isViewLog(){ return viewLog; }
	/**
	 * ミノの出現個数設定を取得する
	 * @return ミノの個数（4つ, 5つ, 両方）
	 */
	public static byte getMino(){ return mino; }
	/**
	 * メロディ調を取得する
	 * @return メロディ調（明るめ，暗め）
	 */
	public static boolean isMelody(){ return melody; }
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
	 * @return 上キーコード
	 */
	public static int getKeyUp(){ return up; }
	/**
	 * 下キーの設定を取得する
	 * @return 下キーコード
	 */
	public static int getKeyDown(){ return down; }
	/**
	 * 左キーの設定を取得する
	 * @return 左キーコード
	 */
	public static int getKeyLeft(){ return left; }
	/**
	 * 右キーの設定を取得する
	 * @return 右キーコード
	 */
	public static int getKeyRight(){ return right; }
	/**
	 * 左回転キーの設定を取得する
	 * @return 左回転キーコード
	 */
	public static int getKeyLeftSpin(){ return leftSpin; }
	/**
	 * 右回転キーの設定を取得する
	 * @return 右回転キーコード
	 */
	public static int getKeyRightSpin(){ return rightSpin; }

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
		System.out.println(left);
		System.out.println(up);
		System.out.println(right);
		System.out.println(down);
		System.out.println(leftSpin);
		System.out.println(rightSpin);
		System.out.println();
	}
}
