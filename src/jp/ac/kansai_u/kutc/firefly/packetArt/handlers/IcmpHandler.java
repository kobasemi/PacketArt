package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.network.Icmp;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Icmp.html

/**
 * @author sya-ke
*/
public interface IcmpHandler{
    public void handleIcmp(Icmp icmp);
}
