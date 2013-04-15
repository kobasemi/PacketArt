package jp.ac.kansai_u.kutc.firefly.packetArt;
// 参考:http://stackoverflow.com/questions/2670982/using-tuples-in-java
/**
 * タプル。 3組の値を格納するクラスです。
 * 
 * @author midolin
 * 
 * @param <X>
 *            型1
 * @param <Y>
 *            型2
 * @param <Z>
 *            型3
 */
public class Tuple3<X, Y, Z> {
	public final X x;
	public final Y y;
	public final Z z;

	public Tuple3(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}