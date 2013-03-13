import javax.sound.midi.*;
import java.lang.Thread; 
import java.lang.Runnable; 

class MusicStation {

    private Receiver receiver;
    private ShortMessage message;
    private boolean readyRun;
    public boolean isReadyRun(){return readyRun;}

    MusicStation(){
        readyRun = false;
        init();
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

    public void changeSound(int instrument) {
        message = new ShortMessage();
        //イミュータブルじゃないらしい。
        try {
            message.setMessage(
                ShortMessage.PROGRAM_CHANGE,
                instrument % 128,//128までだった気がする。
                0
            );
            receiver.send(message, -1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void changeSound() {
        changeSound(0);
    }

    public void playSound(final int onkai,final int volume) {
        try {
            message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_ON, 0, onkai, volume);
            new Thread(new Runnable(){
                public void run(){
                    receiver.send(message, -1);
                    try{
                        Thread.sleep(1000);
                        //途中で音途切れないようにする。
                        message = new ShortMessage();
                        message.setMessage(ShortMessage.NOTE_OFF, 0, onkai, volume);
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

    public void playSound() {
        playSound(60,60);
    }

}
