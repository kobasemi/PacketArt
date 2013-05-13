package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

/**
 * ペントミノを表す列挙体です。
 *
 * @author midolin
 */
public enum PentoMino implements Mino {
	F(new int[][]{{-1, 1}, {-1, 0}, {1, 0}, {0, -1}}),
	I(new int[][]{{2, 0}, {1, 0}, {-1, 0}, {-2, 0}}),
	L(new int[][]{{3, 0}, {2, 0}, {1, 0}, {0, 1}}),
	N(new int[][]{{-2, 0}, {-1, 0}, {0, -1}, {-1, -1}}),
	P(new int[][]{{1, 1}, {0, 1}, {1, 0}, {0, -1}}),
	T(new int[][]{{1, 0}, {-1, 0}, {0, -1}, {0, -2}}),
	U(new int[][]{{-1, 1}, {-1, 0}, {1, 0}, {1, 1}}),
	V(new int[][]{{0, 2}, {0, 1}, {1, 0}, {2, 0}}),
	W(new int[][]{{-1, 1}, {-1, 0}, {0, -1}, {1, -1}}),
	X(new int[][]{{0, -1}, {0, 1}, {1, 0}, {-1, 0}}),
	Y(new int[][]{{0, 1}, {1, 0}, {-1, 0}, {-2, 0}}),
	Z(new int[][]{{-1, -1}, {0, -1}, {0, 1}, {1, 1}});

	private int[][] value;

	private PentoMino(int[][] value) {
		this.value = value;
	}

	public int[][] value() {
		return value;
	}
}
