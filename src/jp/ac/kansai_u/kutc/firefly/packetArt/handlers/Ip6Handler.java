package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.network.Ip6;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Ip6.html

/**
 * @author sya-ke
*/
public interface Ip6Handler{
    public void handleIp6(final Ip6 ip6);
}
