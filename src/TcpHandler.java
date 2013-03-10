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

    public void tcpHandler(Tcp tcpPacket) {
        System.err.println("TCP has come!!");
        tcp = tcpPacket;
    }

    public void paint(Graphics g,Point[] cursor) {
        int val;
        if (tcp == null) { val = 1; } else { val = tcp.destination(); }
        for (int i = 0; i < 50 ; i++) {
            if(cursor[i] != null) {
                g.setColor(Color.getHSBColor(360.0f / (1000*val % 360.0f), 0.8f, 0.8f));
                g.fillOval((int)cursor[i].getX() - 25, (int)cursor[i].getY() - 25, 50, 50);
                }
            }
        }


}
