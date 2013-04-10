package jp.ac.kansai_u.kutc.firefly.packetArt;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*1.enum playSEでwavaファイルを読み込む
 *2.playSE.名前.play()で再生
 *3.playSE.volumeで音量調整*/

//ファイルの読み込み
public enum playSE {
  DECISION("click.wav"),  //暫定1
	CANCEL("cancel.wav"),   //暫定2
	DEMISE("move.wav"); 	//暫定3
	
	//音量調節
	public static enum Volume {
		MUTE, LOW, MEDIUM, HIGH
	}
	//デフォルト音量はLOWに
	public static Volume volume = Volume.LOW;
	private Clip clip;
	playSE(String soundFileName) {
		try {
			//ファイル名を定義
			URL url = this.getClass().getClassLoader().getResource(soundFileName);
			//wavaファイルからオーディオストリームにセット
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
			//clipにgetClip
			clip = AudioSystem.getClip();
			//clipにオーディオストリームを展開
			clip.open(audioInputStream);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
	}
	//予め読み込んだ効果音の再生,mute設定ならばclipの再生を停止
	public void play() {
		if (volume != Volume.MUTE) {
			if (clip.isRunning())
				clip.stop();   //音の停止
			clip.setFramePosition(0); //rewind
			clip.start();     // 音を出力
			}
		}
	//初期化
	static void init() {
		values(); // 全ての要素のコンストラクタを呼ぶ
   }
}
