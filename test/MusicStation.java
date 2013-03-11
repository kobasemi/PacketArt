import javax.sound.midi.*;

import org.jnetpcap.protocol.tcpip.Tcp;

class MusicStation extends ProtocolHandlerBase {
    
    Receiver receiver;
    ShortMessage message;

    MusicStation() {
        init();
    }

    public void init() {
        try {
            Receiver receiver = MidiSystem.getReceiver();
            message = new ShortMessage();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void tcpHandler(Tcp tcp) {
        sound(tcp.source(),tcp.destination());
    }

    public void sound(int data1,int data2) {
        try {
            //message.setMessage(ShortMessage.PROGRAM_CHANGE, num%127, 0);
            message.setMessage(ShortMessage.NOTE_ON, data1%128, data2%128);
            receiver.send(message, -1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        receiver.close();
    }
}
