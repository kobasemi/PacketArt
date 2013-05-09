package jp.ac.kansai_u.kutc.firefly.packetArt.test;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.MusicPlayer;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;

/**
 * @author Lisa-Kudryavka
 */
public class MusicPlayerTest{

    public static void main(String[] args) {
    	PcapManager pm = PcapManager.getInstance();
    	pm.start();
    	pm.openFile("src/jp/ac/kansai_u/kutc/firefly/PacketArt/test/10000.cap");
        final MusicPlayer musicPlayer = new MusicPlayer(75, 10000, false);
        musicPlayer.start();
//        musicPlayer.playMusic();
//        (new Thread(new Runnable(){
 //   		public void run(){
 //   			try {
 //   				Thread.sleep(10000);
 //   				musicPlayer.stopMusic();
//    			} catch (InterruptedException e){
//    			}
//    		}})).start();
        //pm.kill();
        //pm.close();
    }
}
