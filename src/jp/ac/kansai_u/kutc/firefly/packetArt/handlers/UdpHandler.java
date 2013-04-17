package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.tcpip.Udp;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/tcpip/Udp.html

/**
 * @author sya-ke
*/
public interface UdpHandler{
    public void handleUdp(Udp udp);
}
