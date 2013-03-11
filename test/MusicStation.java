import javax.sound.midi.*;
import org.jnetpcap.protocol.tcpip.Tcp;
import java.lang.Thread;

class MusicStation extends ProtocolHandlerBase {
    
    Receiver receiver;
    ShortMessage message;
    MusicStation() {
        init();
    }

    public void init() {
        try {
            receiver = MidiSystem.getReceiver();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void tcpHandler(Tcp tcpPacket) {
        sound(tcpPacket.source(),tcpPacket.destination());
    }

    public void sound(int data1,int data2) {
        try {
            //message.setMessage(ShortMessage.PROGRAM_CHANGE, num%127, 0);
            message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_ON, data1*data2%70, 60);
            receiver.send(message, -1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        receiver.close();
    }
}
