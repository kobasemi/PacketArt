import org.jnetpcap.protocol.tcpip.Tcp;

class TcpHandler extends ProtocolHandlerBase {

    public void tcpHandler(Tcp tcp) {
        System.err.println("TCP has come!");
    }

}
