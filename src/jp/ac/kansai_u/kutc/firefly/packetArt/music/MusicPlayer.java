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

    private Sequencer sequencer;
    public boolean isPlaying() {
        return sequencer.isRunning();
    }

    MusicPlayer() {
        try {
            sequencer  = MidiSystem.getSequencer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.err.println("残念ながら、あなたのPCはMIDIを再生できない");
        }
    }

	public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException{
        MusicPlayer musicPlayer = new MusicPlayer();
		musicPlayer.playMusic(50);
	}
	
	public void playMusic(int velo) throws InvalidMidiDataException, MidiUnavailableException{
		VelocityModulator.setVelocity(velo);
		Sequence sequence = DrumMaker.setDrumLine(velo);
		try{
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

    public void stopMusic() {
        if (sequencer != null && isPlaying()) {
            sequencer.stop();
            sequencer.setMicrosecondPosition(0);
        }
    }
}
