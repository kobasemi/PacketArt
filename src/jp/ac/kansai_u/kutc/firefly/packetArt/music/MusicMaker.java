package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;


//コロブチカ風BGMを生成するクラス．親玉．
public class MusicMaker {
	public static void main(String[] args) {
		MusicMaker.koroMusicMixer();
	}
	
	public static void koroMusicMixer(){
		try{
			//KoroDrumMakerからsequenceを持ってくる．
			Sequence sequence = DrumMaker.koroDrumMaker();
			int[] midi = MidiSystem.getMidiFileTypes(sequence);
			
			//書き出し．koroDrumMaker()=sequence, midi[0]を使わないと複数トラックの出力ができない．
			MidiSystem.write(sequence, midi[0], new java.io.File("hello.mid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
