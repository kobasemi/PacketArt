package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

public class ConfigStatus {
	/*
	 * Field
	 */
	private boolean viewLog;
	private byte    mino;
	private byte    volMusic;
	private byte    volSound;
	private byte    difficulty;
	private char    up, down, left, right;
	private char    leftSpin, rightSpin;
	private char[]  key;
	
	/*
	 * Constructor : INITIALIZE VARIABLE NUMBER
	 */
	ConfigStatus(){
		viewLog = false;
		mino = 0;
		volMusic = 2;
		volSound = 2;
		difficulty = 2;
		up = 'i';
		down = 'k';
		left = 'j';
		right = 'l';
		leftSpin = 'z';
		rightSpin = 'x';
		key = new char[]{up, down, left, right, leftSpin, rightSpin};
	}
	
	/*
	 * Method : SET STATUS
	 */
	public void setViewLog(boolean viewLog){ this.viewLog = viewLog; }
	public void setMino(byte mino){ this.mino = mino; }
	public void setVolMusic(byte volMusic){ this.volMusic = volMusic; }
	public void setVolSound(byte volSound){ this.volSound = volSound; }
	public void setDifficulty(byte difficulty){ this.difficulty = difficulty; }
	public void setUp(char up){ this.up = up; }
	public void setDown(char down){ this.down = down; }
	public void setLeft(char left){ this.left = left; }
	public void setRight(char right){ this.right = right; }
	public void setRightSpin(char rightSpin){ this.rightSpin = rightSpin; }
	public void setleftSpin(char leftSpin){	this.leftSpin = leftSpin; }
	
	/*
	 * Method : GET STATUS
	 */
	public boolean isViewLog(){ return viewLog; }
	public byte getMino(){ return mino; }
	public byte getVolMusic(){ return volMusic; }
	public byte getVolSound(){ return volSound; }
	public byte getDifficulty(){ return difficulty; }
	public char getUp(){ return up; }
	public char getDown(){ return down; }
	public char getLeft(){ return left; }
	public char getRight(){ return right; }
	public char getRightSpin(){ return rightSpin; }
	public char getLeftSpin(){ return leftSpin; }
	public char[] getKey(){ return key; }
	
	/*
	 * Method : PRINT STATUS
	 */
	public void printStatus(){
		System.out.println(viewLog);
		System.out.println(mino);
		System.out.println(volMusic);
		System.out.println(volSound);
		System.out.println(difficulty);
		System.out.println(up);
		System.out.println(down);
		System.out.println(left);
		System.out.println(right);
		System.out.println(leftSpin);
		System.out.println(rightSpin);
		for(int i=0; i<6; i++)
			System.out.print(key[i] + " ");
		System.out.println();
	}
}
