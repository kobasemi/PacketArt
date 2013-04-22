package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

import jp.ac.kansai_u.kutc.firefly.packetArt.Location;

/**
 * パケリスのモデルです。
 * @author midolin
 *
 */
public class PacketrisModel {
	boolean canReverse;
	final int row = 30;
	final int column = 15;
	PacketBlock[][] board;
	ArrayList<PacketBlock> currentMinos;
	int currentMinoRotationPattern = 0;
	Location parentLocation;

	public PacketrisModel(){
		board = new PacketBlock[30][15];
	}

	/**
	 * 初期化を行います。
	 */
	public void initialize(){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new PacketBlock();
			}
		}
		for (int i = 0; i < board.length; i++) {
			board[i][0].setBlockType(BlockType.Wall);
			board[i][column - 1].setBlockType(BlockType.Wall);
		}
		for (int i = 0; i < board[row - 1].length; i++) {
			board[row - 1][i].setBlockType(BlockType.Wall);
		}
	}

	/**
	 * 指定座標のブロックを返します。
	 * @param x
	 * @param y
	 * @return その座標に設定されているブロック
	 */
	public PacketBlock getBoardBlock(int x, int y) {
		return board[x][y];
	}

	/**
	 * 盤面を取得します。
	 * @return 盤面
	 */
	public PacketBlock[][] getBoard(){
		return board;
	}

	/**
	 * 現在操作対象に設定されているミノが反転可能かどうかを取得します。
	 * @return 反転の可否
	 */
	public boolean canReverse() {
		return canReverse;
	}

	/**
	 * 現在操作対象となっているミノの反転の可否を設定します。
	 * @param canReverse 反転の可否
	 */
	public void setCanReverse(boolean canReverse) {
		this.canReverse = canReverse;
	}


	/**
	 * ミノを回転させます。
	 * @param direction 回転方向
	 */
	public void rotate(Direction direction){
		// TODO: 回転させる際に、壁にめり込んだ場合、横にずらして回す
		// TODO: 昇竜ぷよ？みたいなことはさせないようにする
		for (Block item : currentMinos)
			// 回転行列を利用
			if(direction == Direction.Left)
				item.location.set(item.location.getY(), -1 * item.location.getX());
			else
				item.location.set(-1 * item.location.getY(), item.location.getX());

	}

	/**
	 * ミノを降下させます。
	 * @return もしミノがこれ以上落下できない場合、falseを返し、落下が成功した場合、trueを返します。
	 */
	public boolean fallDown(){
		// TODO: あたり判定
		for(Block item : currentMinos)
			item.location.setY(item.location.getY() + 1);
		return true;
	}

	/**
	 * ミノの反転を行います。
	 */
	public void reverse(){
		if(canReverse)
			for(Block item : currentMinos)
				item.location.setX(item.location.getX() * -1);
	}

	/**
	 * ミノを生成します。
	 * @param mino 生成するミノの種類
	 * @param isReversed ミノの生成時反転
	 * @param x 座標
	 */
	public void generateMino(Mino mino, boolean isReversed, int x){
		// 入力チェック
		if(x < 0 || x > column)
			throw new InvalidParameterException("座標指定に問題があります");
		if(mino == null)
			throw new NullPointerException();

		// TODO: ミノを作る(相対座標)
		if(mino instanceof TetroMino){
			switch ((TetroMino)mino) {
			case I:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 1,0,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
					 new PacketBlock(-2,0,mino),
				 }));
				break;
			case O:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 1,0,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock( 1,1,mino),
					 new PacketBlock( 0,1,mino),
				 }));
				break;
			case S:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock(-1,1,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock( 1,0,mino),
				 }));
				break;
			case Two:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 1,1,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case L:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock( 1,0,mino),
				 }));
				break;
			case LReverse:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;

			default:
				assert(false);
			}
		} else if(mino instanceof PentoMino){
			switch((PentoMino)mino){
			case F:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case I:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case L:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case N:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case P:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case T:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case U:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case V:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case W:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] { new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case X:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] { new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case Y:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] { new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			case Z:
				currentMinos.addAll(Arrays.asList(new PacketBlock[] {
					 new PacketBlock( 0,2,mino),
					 new PacketBlock( 0,1,mino),
					 new PacketBlock( 0,0,mino),
					 new PacketBlock(-1,0,mino),
				 }));
				break;
			default:
				assert(false);
				break;

			}
		}
	}
}
