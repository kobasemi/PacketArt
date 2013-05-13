package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import jp.ac.kansai_u.kutc.firefly.packetArt.Location;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * パケリスのモデルです。
 *
 * @author midolin
 */
public class PacketrisModel<T extends Block> {
	ArrayList<ArrayList<T>> board;
	final int row = 30;
	final int column = 15;
	boolean canReverse;
	boolean isAsphyxia = false;

	/**
	 * ミノ
	 */
	ArrayList<T> currentMinos;
	int currentMinoRotationPattern = 0;
	/**
	 * 現在操作対象のミノの親(0,0)の位置
	 */
	Location parentLocation;
	private boolean isGranded;

	public PacketrisModel(T instance) {
		board = new ArrayList<ArrayList<T>>();
		for (int i = 0; i < row; i++) {
			board.add(new ArrayList<T>(column));
			for (int j = 0; j < column; j++) {
				board.get(i).add((T) instance.clone());
			}
		}
		currentMinos = new ArrayList<T>();
	}

	/**
	 * 初期化を行います。
	 */
	public void initialize() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				board.get(i).get(j).setBlockType(BlockType.Void);
			}
		}
		for (int i = 0; i < row; i++) {
			board.get(i).get(0).setBlockType(BlockType.Wall);
			board.get(i).get(0).setLocation(0, 1);
			board.get(i).get(column - 1).setBlockType(BlockType.Wall);
			board.get(i).get(column - 1).setLocation(column - 1, i);
		}
		for (int i = 0; i < column; i++) {
			board.get(row - 1).get(i).setBlockType(BlockType.Wall);
			board.get(row - 1).get(i).setLocation(i, row - 1);
		}

		currentMinos.clear();
		isAsphyxia = false;
	}

	/**
	 * 指定座標のブロックを返します。
	 *
	 * @param x
	 * @param y
	 * @return その座標に設定されているブロック
	 */
	public T getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= column || y >= row)
			throw new InvalidParameterException("Specified parameter is [" + x + ", " + y + "] is out of range.");
		else
			return board.get(y).get(x);
	}

	/**
	 * 指定座標のブロックを返します。
	 *
	 * @param location 座標
	 * @return その座標に設定されているブロック
	 */
	public T getBlock(Location location) {
		if (location == null)
			throw new NullPointerException();
		else
			return getBlock(location.getX(), location.getY());
	}

	/**
	 * 盤面を取得します。
	 *
	 * @return 盤面
	 */
	public ArrayList<ArrayList<T>> getBoard() {
		return board;
	}

	/**
	 * 現在操作対象に設定されているミノが反転可能かどうかを取得します。
	 *
	 * @return 反転の可否
	 */
	public boolean canReverse() {
		return canReverse;
	}


	/**
	 * ミノが配置可能かどうかを返します。
	 *
	 * @param location ミノを配置する位置
	 * @return 配置結果
	 */
	boolean canAllocate(Location location) {
		if (location == null)
			throw new NullPointerException();

		if (location.getX() >= column || location.getY() >= row || location.getX() < 0 || location.getY() < 0)
			return false;

		for (Block item : currentMinos) {
			Location lo = new Location(item.location.getX() + location.getX(), item.location.getY() + location.getY());
			if (lo.getX() >= column || lo.getY() >= row || lo.getX() < 0 || lo.getY() < 0)
				return false;
			if (getBlock(lo) != null && getBlock(lo).blockType != BlockType.Void)
				return false;
		}
		return true;
	}

	/**
	 * 現在操作対象となっているミノの反転の可否を設定します。
	 *
	 * @param canReverse 反転の可否
	 */
	public void setCanReverse(boolean canReverse) {
		this.canReverse = canReverse;
	}

	public void translate(Direction direction) {
		if (direction == null)
			return;
		if (canAllocate(parentLocation.add(direction == Direction.Left ? -1 : 1, 0))) {
			parentLocation.setX(parentLocation.getX() + (direction == Direction.Left ? -1 : 1));
		}
	}


	/**
	 * ミノを回転させます。
	 *
	 * @param direction 回転方向
	 */
	public void rotate(Direction direction) {
		Location[] rotated = new Location[5];
		Location[] stored = new Location[5];
		int i = 0;

		for (Block item : currentMinos) {
			stored[i] = currentMinos.get(i).location;
			int x = item.location.getX();
			int y = item.location.getY();

			// 回転行列を利用
			if (direction == Direction.Right) {
				x = item.location.getY();
				y = -1 * item.location.getX();
			} else {
				x = -1 * item.location.getY();
				y = item.location.getX();
			}
			rotated[i] = new Location(x, y);
			currentMinos.get(i).location = rotated[i];
			i++;
		}

		// 配置できないとき
		if (!canAllocate(parentLocation)) {
			System.out.println("can't allocated.");
		}
	}

	/**
	 * ミノを降下させます。
	 *
	 * @return もしミノがこれ以上落下できない場合、falseを返し、落下が成功した場合、trueを返します。
	 */
	public boolean fallDown() {
		if (isGranded) {
			fixMino();
			return false;
		}
		if (canAllocate(parentLocation.add(0, 1))) {
			parentLocation = parentLocation.add(0, 1);

			if (!canAllocate(parentLocation.add(0, 1)))
				isGranded = true;
			return true;
		} else {
			// 窒息処理
			if (parentLocation.getY() < 2)
				isAsphyxia = true;
		}
		return false;
	}

	/**
	 * 移動可能状態にあるミノを移動不可能状態にして、盤面に固定します。
	 */
	public void fixMino() {
		for (T item : currentMinos) {
			Location l = new Location(parentLocation.getX() + item.location.getX(),
					parentLocation.getY() + item.location.getY());

			board.get(l.getY()).set(l.getX(), item);
			getBlock(l).location = l;

		}
	}

	/**
	 * ゲームが継続不可能かどうかを返します。
	 *
	 * @return ゲームの継続不可能性
	 */
	public boolean isGameOverd() {
		return isAsphyxia;
	}

	/**
	 * ミノの反転を行います。
	 */
	public void reverse() {
		if (canReverse)
			for (Block item : currentMinos)
				item.location.setX(item.location.getX() * -1);
	}

	/**
	 * 行の削除を行います。
	 *
	 * @return 削除された行数
	 */
	public void deleteLines() {
		ArrayList<Integer> deletedLines = new ArrayList<Integer>(5);

		// 調べる処理
		for (int i = row - 2; i > 0; i--) {
			boolean canDeleteFlag = true;
			for (int j = 0; j < column; j++)
				if (getBlock(j, i).blockType == BlockType.Void)
					canDeleteFlag = false;

			// 消す処理
			if (canDeleteFlag) {
				deletedLines.add(i);
				/*for(int j = 1; j < column - 1; j++)   {}
					//board[i][j]  = new PacketBlock();*/
			}
		}

		// 落とす処理
		if (deletedLines.size() > 0) {
			int offset = 0;
			for (int i = deletedLines.get(deletedLines.size() - 1); i > 0; i--) {
				if (deletedLines.contains(i))
					offset++;
				for (int j = 1; j < column - 1; j++) {
					board.get(i).set(j, board.get(i - offset).get(j));
					getBlock(j, i).location.set(j, i);
				}
			}
		}
	}

	/**
	 * ミノを生成します。
	 *
	 * @param mino       生成するミノの種類
	 * @param instance   ミノの実体(これがコピーされます)
	 * @param reversible ミノの生成時反転
	 * @param x          座標
	 */
	public void generateMino(Mino mino, T instance, boolean reversible, int x) {
		// 入力チェック
		if (x < 0 || x > column)
			throw new InvalidParameterException("座標指定に問題があります");
		if (mino == null)
			throw new NullPointerException();
		parentLocation = new Location(x, 2);
		currentMinos.clear();

		currentMinos.add((T) instance.clone());
		currentMinos.get(0).setBlockType(BlockType.Mino);

		for (int j = 0; j < 4; j++) {
			if (mino instanceof TetroMino && j >= 3)
				break;
			else {
				currentMinos.add((T) instance.clone());
				currentMinos.get(j + 1).setBlockType(BlockType.Mino);
				currentMinos.get(j + 1).setLocation(mino.value()[j][0], mino.value()[j][1]);
			}
		}

		// 窒息死
		if (!canAllocate(parentLocation))
			isAsphyxia = true;

		// 反転
		if (reversible)
			reverse();
	}

	public boolean isGameOver() {
		return isAsphyxia;
	}

	public ArrayList<T> getCurrentMinos() {
		return currentMinos;
	}

	public boolean isGranded() {
		return isGranded;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (ArrayList<T> column : board) {
			for (Block item : column)
				stringBuilder.append(item.blockType == BlockType.Wall ? "W" : item.blockType == BlockType.Void ? "_" : "o");
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
}
