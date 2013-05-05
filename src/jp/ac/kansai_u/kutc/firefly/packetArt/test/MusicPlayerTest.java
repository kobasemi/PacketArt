package jp.ac.kansai_u.kutc.firefly.packetArt.test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.AccompanimentMaker;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.MelodyMaker;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;



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
public class MusicPlayerTest{

    private Sequencer sequencer;
    public boolean isPlaying() {
        return sequencer.isRunning();
    }

    MusicPlayerTest() {
        try {
            sequencer  = MidiSystem.getSequencer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.err.println("残念ながら、あなたのPCはMIDIを再生できない");
        }
    }

    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException{
        MusicPlayerTest musicPlayer = new MusicPlayerTest();
        PcapManager pm = PcapManager.getInstance();
        pm.start();
        pm.debugMe("DEBUG! FROM PLAYMUSIC");
        System.out.println("---------------------");
        pm.openFile("src/jp/ac/kansai_u/kutc/firefly/packetArt/test/10000.cap");
        pm.debugMe("DEBUG! FROM PLAYMUSIC");
        //pm.openFile("1000.cap" );
        musicPlayer.playMusic(50, 60, true);
        pm.debugMe("DEBUG! FROM PLAYMUSIC");
        while ( pm.isReadyRun() ) {
            pm.debugMe("DEBUG! FROM PLAYMUSIC");
            musicPlayer.playMusic(50, 60, true);
            while(musicPlayer.isPlaying()){
            }
        }
        pm.kill();
        pm.close();
        pm = null;
        System.out.println("---------------------");
    }

    public void playMusic(int velocity, int length, boolean judgetone) throws InvalidMidiDataException, MidiUnavailableException{
		Sequence sequence = new Sequence(Sequence.PPQ, 24, 3);
		if(judgetone == true){
			System.out.print("Make Cheerful Song.\r\n");
			try{
				MelodyMaker.setCheerfulMelody(sequence, length, velocity);
				AccompanimentMaker.makeCheerfulAccompaniment(sequence, length, velocity);
				sequencer = MidiSystem.getSequencer();
				sequencer.open();
				sequencer.setSequence(sequence);
				sequencer.start();
				while(sequencer.isRunning()) Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}catch(MidiUnavailableException e){
				e.printStackTrace();
			}catch(InvalidMidiDataException e){
				e.printStackTrace();
			}finally{
				if(sequencer != null && sequencer.isOpen()) sequencer.close();
			}
		}
    }

    public void stopMusic() {
        if (sequencer != null && isPlaying()) {
            sequencer.stop();
            //sequencer.setMicrosecondPosition(0);
        }
    }
}
