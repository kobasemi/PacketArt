package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

public enum MinoType {
	Tetro(7),
	Pento(12),
	Both(19);

	private int value;

	private MinoType(int value) {
		this.value = value;
	}
}
