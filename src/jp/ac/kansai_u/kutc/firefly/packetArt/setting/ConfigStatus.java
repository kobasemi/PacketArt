package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

import java.awt.event.KeyEvent;

/**
 * 設定項目のデータ構造
 * @author akasaka
 */
public class ConfigStatus {
	private static boolean  viewLog;	// ビューの表示のオン・オフ
	private static MinoType mino;		// ミノの種類（4つ，5つ，両方）
	private static boolean  melody;		// メロディ（明るめ，暗め）
	private static byte     volMusic;	// 音楽の音量の設定（Mute, Low, Medium, High）
	private static byte     volSound;	// 効果音の音量の設定（Mute, Low, Medium, High）
	private static byte     difficulty;	// 難易度（静的，動的，自動）
	private static byte     keybind;		// キーバインド（Default, Gamer, Vim）
	private static int      up, down, left, right, leftSpin, rightSpin;	// キー
	
	/**
	 * コンストラクタ
	 */
	public ConfigStatus(){
		viewLog    = false;
		mino       = MinoType.values()[ConfigInfo.MINO4];
		melody     = true;
		volMusic   = ConfigInfo.VOLBGMMEDIUM;
		volSound   = ConfigInfo.VOLSEMEDIUM;
		difficulty = ConfigInfo.AUTO;
		keybind    = ConfigInfo.KEYDEFAULT;
		left       = ConfigInfo.KEYBIND[keybind][ConfigInfo.LEFT];
		up         = ConfigInfo.KEYBIND[keybind][ConfigInfo.UP];
		right      = ConfigInfo.KEYBIND[keybind][ConfigInfo.RIGHT];
		down       = ConfigInfo.KEYBIND[keybind][ConfigInfo.DOWN];
		leftSpin   = ConfigInfo.KEYBIND[keybind][ConfigInfo.LSPIN];
		rightSpin  = ConfigInfo.KEYBIND[keybind][ConfigInfo.RSPIN];
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
	public static void setMino(MinoType m){ mino = m; }
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
	public static MinoType getMino(){ return mino; }
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

//	TODO 最終的には消すけど，今は変数確認用に使う
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
