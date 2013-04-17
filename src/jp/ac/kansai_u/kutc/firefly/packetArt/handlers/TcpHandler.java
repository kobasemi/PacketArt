package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.tcpip.Tcp;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/tcpip/Tcp.html

/**
 * PcapManagerに搭載するためのインターフェースです。<br>
 * 搭載完了した場合、tcpHandler関数はTCPパケットの到着毎に毎回呼ばれます。<br>
 * 使い方の例<br>
 * <code>
 * public class TcpMusic extends ProtocolHandler implements TcpHandler{
 *     MusicPlayer mp;
 *     TcpMusic(){
 *         mp = new MusicPlayer();
 *     }
 *     public void handleTcp(Tcp tcp){
 *         int dstPort = tcp.destination();
 *         mp.playInt(dstPort);
 *     }
 * }
 * </code>
 * @author sya-ke
*/
public interface TcpHandler{
    public void handleTcp(Tcp tcp);
}
