package jp.ac.kansai_u.kutc.firefly.packetArt.setting;

public class ConfigInfo {
	final static byte MINO4=0, MINO5=1, MINOBOTH=2;
	final static byte MUTE=0;
	final static byte VOLLOW=50, VOLMEDIUM=75, VOLHIGH=100;
	final static byte STATIC=0, DYNAMIC=1, AUTO=2;
	final static byte KEYDEFAULT=0, KEYGAMER=1, KEYVIM=2;
	final static byte LEFT=0, UP=1, RIGHT=2, DOWN=3, LSPIN=4, RSPIN=5;
	
	final static String IMGPATH = new String("./resource/image/config/");
	final static String BTNPATH = new String(IMGPATH + "button/");
	
	final static int WIDTH=600, HEIGHT=800;
	final static int MARGIN=30;
	
}
