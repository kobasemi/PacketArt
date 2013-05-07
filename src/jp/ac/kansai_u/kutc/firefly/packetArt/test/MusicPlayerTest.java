package jp.ac.kansai_u.kutc.firefly.packetArt.test;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.MusicPlayer;

/**
 * @author Lisa-Kudryavka
 */
public class MusicPlayerTest{

    public static void main(String[] args) {
        MusicPlayer musicPlayer = new MusicPlayer(9,24,true);
        musicPlayer.playMusic();
    }
}
