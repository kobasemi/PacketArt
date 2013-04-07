package jp.ac.kansai_u.kutc.firefly.packetArt;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.jnetpcap.protocol.tcpip.Tcp;

class Liner extends ProtocolHandlerBase {

    private int xMax;
    private int yMax;
    private int x;
    private Tcp tcp;
    private MusicStation ms;

    Liner(int width, int height) {
        xMax = width;//画面横
        yMax = height;//画面縦
        x = 0;
        tcp = null;
        ms = new MusicStation();
    }

    public void tcpHandler(Tcp tcpPacket) {
        tcp = tcpPacket;
        ms.changeSound(tcp.checksum());
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
        ms.playSound();
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
