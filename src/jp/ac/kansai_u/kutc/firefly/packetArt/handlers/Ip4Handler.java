package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.network.Ip4;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/network/Ip4.html

/**
 * @author sya-ke
*/
public interface Ip4Handler{
    public void handleIp4(Ip4 ip4);
}
