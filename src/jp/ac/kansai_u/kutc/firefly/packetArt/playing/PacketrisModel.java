package jp.ac.kansai_u.kutc.firefly.packetArt.playing;

public class PacketrisModel {
	final int row = 30;
	final int column = 15;
	Block[][] board;
	
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
}
