package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * MusicPlayerクラス <br>
 * このクラスは音楽を生成，再生するシステムを司るクラスです。<br>
 * 引数に0~127の数値を入れて以下のように記述することで，音楽が生成，再生されます．<br>
 * <code>
 * MusicPlayer.playMusic(100);
 * </code>
 * 
 * @author Lisa-Kudryavka
 *
 */
public class MusicPlayer{
	public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException{
		playMusic(50);
	}
	
	public static void playMusic(int velo) throws InvalidMidiDataException, MidiUnavailableException{
		VelocityModulator.setVelocity(velo);
		Sequencer sequencer = null;
		Sequence sequence = DrumMaker.setDrumLine(velo);
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
