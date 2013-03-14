import javax.sound.midi.*;
import java.lang.Thread; 
import java.lang.Runnable; 
import java.lang.Math;

// About MiDi:
// https://ccrma.stanford.edu/~craig/articles/linuxmidi/misc/essenmidi.html



/**
 * 使い方：
 * MusicStation ms = new MusicStation();
 * ms.playSound(maxPlayLength, channel, pitch, volume);
 * ms.changeSound();
*/
class MusicStation {

    private Receiver receiver;
    private ShortMessage message;
    private boolean readyRun;
    public boolean isReadyRun(){ return readyRun; }

    public static final int BYTE = 256;
    public static final int MAX_CHANNEL = 16;
    public static final int MAX_PITCH = 16;
    public static final int MAX_VOLUME = 128;
    public static final int MAX_INSTRUMENT = 128;

    public static int DEFAULT_PLAY_LENGTH = 1000;//1秒間待ってやる
    public static int DEFAULT_CHANNEL = 1;
    public static int DEFAULT_PITCH = 15;
    public static int DEFAULT_VOLUME = 63;
    public static int DEFAULT_INSTRUMENT = 12;

    MusicStation(){
        readyRun = false;
        init();
        if (readyRun) {
            changeSound(DEFAULT_INSTRUMENT);//最初、なんとなくこの音
        }
    }

    private void init() {
        try {
            receiver = MidiSystem.getReceiver();
        } catch(Exception e) {
            e.printStackTrace();
            readyRun = false;
            return;
        }
        readyRun = true;
    }

    public int changeSound(int channel, int instrument) {
        message = new ShortMessage();
        //イミュータブルじゃないらしい。
        try {
            message.setMessage(
                ShortMessage.PROGRAM_CHANGE,
                Math.abs(channel % MAX_CHANNEL),
                Math.abs(instrument % MAX_INSTRUMENT),
                0
            );
            receiver.send(message, -1);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Math.abs(instrument % MAX_INSTRUMENT);
    }

    public int changeSound(int instrument) {
        return changeSound(DEFAULT_CHANNEL, instrument);
    }


    /**
     * 音を鳴らします。
     * @param maxPlayLength 最大再生長さ
     * @param channel チャンネル。1～16。16個の楽器を同時に演奏できる
     * @param pitch 音の高さ
     * @param volume 音の強さ
    */
    public void playSound(final int maxPlayLength , final int channel, final int pitch, final int volume) {
        try {
            message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_ON,
             Math.abs(channel % MAX_CHANNEL),
             Math.abs(pitch % MAX_PITCH),
             Math.abs(volume % MAX_VOLUME));
            new Thread(new Runnable(){
                public void run(){
                    receiver.send(message, -1);
                    try{
                        Thread.sleep(Math.abs(maxPlayLength) );
                        //途中で音途切れないようにする。
                        message = new ShortMessage();
                        message.setMessage(ShortMessage.NOTE_OFF,
                         Math.abs(channel % MAX_CHANNEL),
                         Math.abs(pitch % MAX_PITCH),
                         Math.abs(volume % MAX_VOLUME) );
                        receiver.send(message, -1);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSound(int channel, int pitch, int volume) {
        playSound(DEFAULT_PLAY_LENGTH, channel, pitch, volume);
    }

    public void playSound(int pitch, int volume) {
        playSound(DEFAULT_PLAY_LENGTH, DEFAULT_CHANNEL, pitch, volume);
    }

    public void playSound(int volume) {
        playSound(DEFAULT_PLAY_LENGTH, DEFAULT_CHANNEL, DEFAULT_PITCH, volume);
    }
    
    public void playSound() {
        playSound(DEFAULT_PLAY_LENGTH, DEFAULT_CHANNEL, DEFAULT_PITCH, DEFAULT_VOLUME);
    }

}
