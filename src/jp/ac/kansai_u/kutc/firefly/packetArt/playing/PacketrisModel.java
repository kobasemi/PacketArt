package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

import java.util.ArrayList;

public class PacketrisModel {
	final int row = 30;
	final int column = 15;
	Block[][] board;
	ArrayList<Block> currentMinos;
	
	public PacketrisModel(){
		board = new Block[30][15];
	}
	
	public void initialize(){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new Block();
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

	public Block getBoardBlock(int x, int y) {
		return board[x][y];
	}
	
	public Block[][] getBoard(){
		return board;
	}
	
	/**
	 * ミノを回転させます
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
}

enum Direction{
	Left, Right
}
