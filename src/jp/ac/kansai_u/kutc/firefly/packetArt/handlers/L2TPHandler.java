package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.vpn.L2TP;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/vpn/L2TP.html

/**
 * @author sya-ke
*/
public interface L2TPHandler{
    public void handleL2TP(L2TP l2tp);
}
