import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jnetpcap.protocol.tcpip.Tcp;

class TcpHandler extends ProtocolHandlerBase {
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/tcpip/Tcp.html#destination%28%29

    private Tcp tcp;
    TcpHandler() {
        tcp = null;
    }

    boolean isNewer = false;
    public void tcpHandler(Tcp tcpPacket) {
//        System.err.println("TCP has come!!");
        tcp = tcpPacket;
        isNewer = true;
    }

    public void paint(Graphics g,Point[] cursor,int h,int w) {
        if (isNewer) {
            g.drawString("Tcp Comes!!", h - 120, w - 120);
        } else {
            g.drawString("Tcp not come", h - 120, w - 120);
        }
        long val;
        if (tcp == null) { val = 180; } else { val = tcp.seq(); }
        for (int i = 0; i < 50 ; i++) {
            if(cursor[i] != null) {
                g.setColor(Color.getHSBColor(360.0f / (val % 360.0f), 0.8f, 0.8f));
                g.fillOval((int)cursor[i].getX() - 25, (int)cursor[i].getY() - 25, (int)val%50, (int)val%50);
            }
        }
        isNewer = false;
    }
}
