package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

import org.jnetpcap.protocol.tcpip.Tcp;
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/tcpip/Tcp.html

/**
 * PcapManagerに搭載するためのインターフェースです。<br>
 * 搭載完了した場合、tcpHandler関数はTCPのレイヤを含むパケットの到着毎に毎回呼ばれます。<br>
 * 使い方の例<br>
 * <code>{
 *
 * public class TcpMusic implements TcpHandler{
 *     public void handleTcp(final Tcp tcp){
 *         int dstPort = tcp.destination();
 *         (MusicPlayer.getInstance()).playInt(dstPort);
 *     }
 * }
 *
 * }</code>
 *
 * @author sya-ke
*/
public interface TcpHandler{
    public void handleTcp(final Tcp tcp);
}
