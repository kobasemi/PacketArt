package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

//BGMをMidiで出すクラス．製品化前に除去すること．
public class MusicMaker {
	public static void main(String[] args) {
		makeMidi(60);
	}
		
	public static void makeMidi(int velo){
		try{
			//KoroDrumMakerからsequenceを持ってくる．
			Sequence sequence = DrumMaker.setDrumLine(velo);
			int[] midi = MidiSystem.getMidiFileTypes(sequence);
			
			//書き出し．koroDrumMaker()=sequence, midi[0]を使わないと複数トラックの出力ができない．
			MidiSystem.write(sequence, midi[0], new java.io.File("bgm.mid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

