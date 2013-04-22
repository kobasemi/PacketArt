package jp.ac.kansai_u.kutc.firefly.packetArt;
// 参考:http://stackoverflow.com/questions/2670982/using-tuples-in-java
/**
 * タプル。 2組の値を格納するクラスです。
 *
 * @author midolin
 *
 * @param <X>
 *            型1
 * @param <Y>
 *            型2
 */
public class Tuple<X, Y> {
	public final X x;
	public final Y y;

	/**
	 * タプルを設定します。
	 * @param x 1つめの値
	 * @param y 2つめの値
	 */
	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}
}