package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.network.Arp;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Arp.html

/**
 * @author sya-ke
*/
public interface ArpHandler{
    public void handleArp(Arp arp);
}
