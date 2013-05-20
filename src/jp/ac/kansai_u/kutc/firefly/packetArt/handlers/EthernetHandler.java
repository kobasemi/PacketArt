package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.lan.Ethernet;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/lan/Ethernet.html

/**
 * @author sya-ke
*/
public interface EthernetHandler{
    public void handleEthernet(final Ethernet ethernet);
}
