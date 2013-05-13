package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import jp.ac.kansai_u.kutc.firefly.packetArt.Location;

/**
 * 1つのブロックを表すクラスです。このブロックを複数個組み合わせることで、盤面や落下中のテトリミノおよびペントミノを表現します。
 * このブロックは1つのパケットを保持することができます。このブロックはテトリミノの形状を保持します。<br>
 * もし、テトリミノの形状を保持していない場合、nullを持ちます。パケットを持たない場合も、nullを持ちます。
 *
 * @author midolin
 */
public class Block implements Cloneable {
	Location location;
	BlockType blockType;
	Mino mino;

	// getter and setter

	/**
	 * ブロックの種類を取得します。
	 *
	 * @return ブロックの種類
	 */
	public BlockType getBlockType() {
		return blockType;
	}

	/**
	 * ブロックの種類を設定します。
	 *
	 * @param blockType ブロックの種類
	 */
	public void setBlockType(BlockType blockType) {
		this.blockType = blockType;
	}

	/**
	 * 設定されているミノを取得します。ミノはテトリミノ、ペンタミノ、もしくはnullが設定されています。
	 *
	 * @return
	 */
	public Mino getMino() {
		return mino;
	}

	/**
	 * ミノを設定します。ミノの設定後は自動的にBlockTypeがMinoに変更されます。
	 *
	 * @param mino ミノ
	 */
	public void setMino(Mino mino) {
		this.mino = mino;
		blockType = BlockType.Mino;
	}

	/**
	 * ブロックに位置指定がされている場合、位置を返します。
	 *
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 位置を設定します。
	 *
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * 位置を設定します。
	 *
	 * @param x
	 * @param y
	 */
	public void setLocation(int x, int y) {
		location = new Location(x, y);
	}

	/**
	 * ブロックを生成します。
	 */
	public Block() {
		location = new Location();
		blockType = BlockType.Void;
	}

	/**
	 * ブロックを生成します。
	 *
	 * @param location ブロックの位置
	 * @param mino     ミノの種類
	 */
	public Block(Location location, Mino mino) {
		this.location = location;
		this.mino = mino;
		this.blockType = BlockType.Mino;
	}

	@Override
	public Block clone() {
		Block block = new Block(new Location(location.getX(), location.getY()), mino);
		block.setBlockType(blockType);
		return block;
	}
}
