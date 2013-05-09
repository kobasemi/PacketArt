package jp.ac.kansai_u.kutc.firefly.packetArt.music;

public class ThreadTest {
	
	public static void main(String[] args) {
		Thread thread;
		thread = new MusicPlayer(100, 60, true);
		thread.start();
		for(int i = 0; i < 1000000; i++){
			System.out.println(i);
		}
		thread.stop();

	}
}
