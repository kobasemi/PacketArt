package jp.ac.kansai_u.kutc.firefly.packetArt;

/**
 * 2次元のxおよびyで示される座標を表すクラスです。
 * @author midolin
 *
 */
public class Location {
	int x = 0;
	int y = 0;

	/**
	 * x = 0, y = 0で座標を作成します。
	 */
	public Location() {
	}

	/**
	 * 指定した座標を作成します。
	 * @param x x座標
	 * @param y y座標
	 */
	public Location(int x, int y) {
		set(x, y);
	}

	/**
	 * 座標を設定します。
	 * @param x x座標
	 * @param y y座標
	 */
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * x座標を設定します。
	 * @param value x座標
	 */
	public void setX(int value) {
		x = value;
	}

	/**
	 * y座標を設定します。
	 * @param value y座標
	 */
	public void setY(int value) {
		y = value;
	}

	/**
	 * x座標を取得します。
	 * @return x座標
	 */
	public int getX() {
		return x;
	}

	/**
	 * y座標を取得します。
	 * @return y座標
	 */
	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object comparison) {
		if (this == comparison)
			return true;
		if (comparison == null)
			return false;
		if (getClass() != comparison.getClass())
			return false;
		return getX() == ((Location) comparison).getX()
				&& getY() == ((Location) comparison).getY();
	}

	@Override
	public String toString(){
		return "[" + getX() + ", " + getY() + "]";

	}
}
