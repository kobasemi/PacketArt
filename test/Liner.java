import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jnetpcap.protocol.tcpip.Tcp;
import java.lang.Thread; 
import java.lang.Runnable; 

class Liner extends ProtocolHandlerBase {

    private int xMax;
    private int yMax;
    private int x;
    private Receiver receiver;//デバイスをこのクラスで占有してしまう・・ロック・・
    private ShortMessage message;
    private Tcp tcp;

    Liner(int width, int height) {
        xMax = width;//画面横
        yMax = height;//画面縦
        x = 0;
        tcp = null;
        init();
    }

    public void tcpHandler(Tcp tcpPacket) {
        tcp = tcpPacket;
        changeSound();
    }

    public void changeSound() {
        message = new ShortMessage();
        try {
            System.err.println("CHANGING GAKKI :" + tcp.source()*tcp.destination()%128);
            message.setMessage(
                ShortMessage.PROGRAM_CHANGE,
                tcp.source()*tcp.destination()%128,//ココがパケットアート！
                0
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        receiver.send(message, -1);
    }

    public void init() {
        try {
            receiver = MidiSystem.getReceiver();
            message = new ShortMessage();
            message.setMessage(
                ShortMessage.PROGRAM_CHANGE,
                0,
                0
            );
            receiver.send(message, -1);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g,Point[] cursor) {
        marble(g,cursor);
        sound(cursor);
        walk(g);
        tcp = null;//前のTCPパケットは放棄する。
    }

    public void marble(Graphics g,Point[] cursor) {
        for(Point point : cursor) {
            if(point != null) {
                int distance = x - (int)point.getX();
                if( 5 > distance && -5 < distance ) {
                    for (int i=20;i>10;i=i-3) {
                        g.setColor(Color.red);
                        g.drawOval((int)point.getX() - i, (int)point.getY() - i, 2*i, 2*i);
                    }
                }
                g.setColor(Color.black);
                g.fillOval((int)point.getX() - 10, (int)point.getY() - 10, 20, 20);
            }
        }
    }


    public void sound(Point[] cursor) {
        if ( isOverLine(cursor) != true) {
            return;
        }
        try {
            //message.setMessage(ShortMessage.PROGRAM_CHANGE, num%127, 0);
            message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_ON, 60, 60);
            new Thread(new Runnable(){
                public void run(){
                    receiver.send(message, -1);
                    try{
                        Thread.sleep(1000);
                        message = new ShortMessage();
                        message.setMessage(ShortMessage.NOTE_OFF, 60, 60);
                    } catch(Exception e) {
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void walk(Graphics g) {
        g.setColor(Color.red);
        g.drawLine(x,0,x,yMax);
        if (x == xMax -1) {
            x=0;
        }
        x++;
    }

    private boolean isOverLine(Point[] cursor) {
        for (Point point : cursor) {
            if (point != null && (int)point.getX() == x) {
                return true;
            }
        }
        return false;
    }

}
