package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.wan.PPP;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/wan/PPP.html

/**
 * @author sya-ke
*/
public interface PPPHandler{
    public void handlePPP(PPP ppp);
}
