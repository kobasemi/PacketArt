package jp.ac.kansai_u.kutc.firefly.packetArt;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.packet.PcapPacket;

/**
 * このクラスはサンプルです。TCPを扱うクラスの例です。
*/
class TcpHandler extends ProtocolHandlerBase {
//ProtocolHandlerBase.javaは、プロトコル毎にパケットをフィルタするクラス。
//このクラスを継承することで、TCPパケットだけをこちらへ持ってくることができます。

//TCPパケットの使える値を取り出す関数は以下を参照してください。
//http://jnetpcap.com/docs/javadocs/jnetpcap-1.3/org/jnetpcap/protocol/tcpip/Tcp.html#destination%28%29

    private Tcp tcp;
    private PcapPacket pkt;
    private boolean newer;

    TcpHandler() {
        tcp = null;
        pkt = null;
        newer = false;
    }

    //親クラスのinspect関数が呼ばれ、しかもTCPが来た時に呼ばれる関数。
    @override
    public void tcpHandler(Tcp tcpPacket) {
//        System.err.println("TCP has come!!");
        tcp = tcpPacket;
        newer = true;
    }

    //パケットが来る(inspect関数が呼ばれる)ごとに呼ばれる関数。
    @override
    public void pcapPacketHandler(PcapPacket p) {
        if (newer == false) {
//            System.err.println("Not a TCP Packet...");
//            System.err.println("But some Packet has come!!");
            pkt = p;
        }
    }

    /**
     * パケットが来ない場合もこのクラスでなんらかのことをしたい場合は
     * Form.update()関数で呼び出せば良い。
     * ただし、その場合はこのクラスが保持しているパケットは
     * 一つ前のものであり、それしか使えない。
     * それはそれで良いが、一つ前のTCPか、新しいものかの判別は各自で
     * 例）newer変数
     * のように識別してほしい。
     */
  public void doSomeThing(Graphics g){
        //ここでtcp変数をつかって何かする。
        //この関数はtcpHandler()の中で実行すれば、TCPが来るごとに実行される
        //packetHandler()の中で実行すればパケットが来るごとに実行される
        //外部から呼び出しても良い
        newer = false
  }

}
