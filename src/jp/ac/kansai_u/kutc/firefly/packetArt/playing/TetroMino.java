package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

/**
 * テトロミノを定義する列挙体です。
 *
 * @author midolin
 */
public enum TetroMino implements Mino {
	I(new int[][]{{1, 0}, {-1, 0}, {-2, 0}}),
	O(new int[][]{{1, 0}, {1, 1}, {0, 1}}),
	S(new int[][]{{-1, 1}, {0, 1}, {1, 0}}),
	Two(new int[][]{{1, 1}, {0, 1}, {-1, 0}}),
	L(new int[][]{{2, 0}, {1, 0}, {0, 1}}),
	LReverse(new int[][]{{2, 0}, {1, 0}, {0, -1}}),
	T(new int[][]{{-1, 0}, {1, 0}, {0, -1}});

	private int[][] value;

	private TetroMino(int[][] value) {
		this.value = value;
	}

	public int[][] value() {
		return value;
	}
}