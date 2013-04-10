import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;


//生成された音楽を再生するクラス
//【実行するとよくわからない音楽が大音量で鳴るので注意】
public class MusicPlayer {
	public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException {
		Sequencer sequencer = null;
		Sequence sequence = DrumMaker.koroDrumMaker();
		try{
			sequencer  = MidiSystem.getSequencer();
			sequencer.open();
			
			sequencer.setSequence(sequence);
			sequencer.start();
			while(sequencer.isRunning()) Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if (sequencer != null && sequencer.isOpen()) sequencer.close();
		}

	}

}
