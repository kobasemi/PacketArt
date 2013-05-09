package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * ゲームBGMを再生するメソッドです。
 * スレッドに対応してます。
 * <code>playMusic(100, 240, true)</code>
 * のように呼び出して使用して下さい。
 * @author Lisa
 */
public class MusicPlayer extends Thread{
    private Sequencer sequencer;
    private Sequence sequence;
    private int velocity;
    private int length;
    private boolean judgetone;
    private boolean killMe;

    public MusicPlayer(int _velocity, int _length, boolean _judgetone){
        velocity = _velocity;
        length = _length;
        judgetone = _judgetone;
        killMe = false;
    }

    public void changeValues(int _velocity, int _length, boolean _judgetone){
        velocity = _velocity;
        length = _length;
        judgetone = _judgetone;
    }

    public void run(){
        while (killMe == false) {
            playMusic();
        }
    }

    public void playMusic() {
        if(judgetone == true){
            System.out.print("Make Cheerful Song.\r\n");
            try{
                sequence = new Sequence(Sequence.PPQ, 24, 3);
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
        }else if(judgetone == false){
            System.out.print("Make Gloomy Song.\r\n");
            try{
                sequence = new Sequence(Sequence.PPQ, 24, 3);
                MelodyMaker.setGloomyMelody(sequence, length, velocity);
                AccompanimentMaker.makeGloomyAccompaniment(sequence, length, velocity);
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
        killMe = true;
        if (sequencer.isRunning() == true)
            sequencer.stop();
    }
}

