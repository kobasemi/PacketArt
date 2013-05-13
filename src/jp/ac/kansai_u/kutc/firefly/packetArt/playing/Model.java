package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import jp.ac.kansai_u.kutc.firefly.packetArt.Location;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * #oneline description.
 *
 * @author midolin
 *         Date: 13/05/12 6:49
 *         <p/>
 *         # long descriptions.
 */
public class Model<T extends Block> {
	public final int row = 30;
	public final int column = 15;
	public Location parentLocation;

	private ArrayList<ArrayList<T>> board;
	private ArrayList<T> currentMinos;
	private int[][] minoLocations;
	private boolean isGameOver;
	private boolean isGranded;

	public Model(T instance) {
		board = new ArrayList<ArrayList<T>>();
		for (int i = 0; i < row; i++) {
			board.add(new ArrayList<T>(column));
			for (int j = 0; j < column; j++) {
				board.get(i).add((T) instance.clone());
			}
		}
		currentMinos = new ArrayList<T>();
	}

	public void initialize() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				board.get(i).get(j).setBlockType(BlockType.Void);
			}
		}
		for (int i = 0; i < row; i++) {
			board.get(i).get(0).setBlockType(BlockType.Wall);
			board.get(i).get(column - 1).setBlockType(BlockType.Wall);
		}
		for (int i = 0; i < column; i++) {
			board.get(row - 1).get(i).setBlockType(BlockType.Wall);
		}

		isGameOver = false;
		currentMinos.clear();
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

	T getBoardItem(int x, int y) {
		return board.get(y).get(x);
	}

	void setBoardItem(int x, int y, T item) {
		board.get(y).set(x, item);
	}

	Location[] getBlockDestinations(Location location) {
		Location[] locations = new Location[minoLocations.length];
		for (int i = 0; i < minoLocations.length; i++)
			locations[i] = new Location(
					location.getX() + minoLocations[i][0],
					location.getY() + minoLocations[i][1]);
		return locations;
	}

	boolean canAllocate(Location location) {
		if (location == null)
			throw new NullPointerException();

		Location[] locations = getBlockDestinations(location);
		for (Location item : locations) {
			if (item.getX() >= column || item.getY() >= row || item.getX() < 0 || item.getY() < 0)
				return false;
			if (getBoardItem(item.getX(), item.getY()).blockType != BlockType.Void)
				return false;
		}
		return true;
	}

	public void rotate(Direction d) {
		Location[] rotated = new Location[5];
		Location[] stored = new Location[5];
		int i = 0;

		for (T item : currentMinos) {
			stored[i] = currentMinos.get(i).location;
			int x = minoLocations[i][0];
			int y = minoLocations[i][1];

			// 回転行列を利用
			if (d == Direction.Right) {
				x = minoLocations[i][1];
				y = -1 * minoLocations[i][0];
			} else {
				x = -1 * minoLocations[i][1];
				y = minoLocations[i][0];
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

	public void translate(Direction d) {
		if (d == null)
			return;
		Location l = parentLocation.add(parentLocation.getX() +
				(d == Direction.Left ? -1 : 1), 0);
		if (canAllocate(l))
			parentLocation = l;
	}

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
		}
		return false;
	}

	public void fixMino() {
		Location[] l = getBlockDestinations(parentLocation);
		for (int i = 0; i < minoLocations.length; i++) {
			currentMinos.get(i + 1).location = l[i];
			setBoardItem(
					parentLocation.getX() + minoLocations[i][0],
					parentLocation.getY() + minoLocations[i][1],
					currentMinos.get(i));
		}
		currentMinos.clear();
	}

	public void deleteLines() {
	}

	public void generateMino(Mino i, T instance, boolean reversible, int x) {
		parentLocation = new Location(x, 2);
		if (i != null && i instanceof TetroMino) {
			minoLocations = i.value();
			for (int j = 0; j < 4; j++) {
				currentMinos.add((T) instance.clone());
				currentMinos.get(j).setBlockType(BlockType.Mino);
			}
		} else {
			throw new InvalidParameterException();
		}
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public ArrayList<ArrayList<T>> getBoard() {
		return board;
	}

	public ArrayList<T> getCurrentMinos() {
		return currentMinos;
	}

	public boolean isGranded() {
		return isGranded;
	}
}

