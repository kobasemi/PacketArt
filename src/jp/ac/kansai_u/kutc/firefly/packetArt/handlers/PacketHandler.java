package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.packet.PcapPacket;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/wan/PPP.html

/**
 * @author sya-ke
*/
public interface PacketHandler{
    public void handlePacket(PcapPacket packet);
}
