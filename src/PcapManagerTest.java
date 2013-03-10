import org.jnetpcap.packet.PcapPacket;
class PcapManagerTest {
    public static void main(String[] args) {
        try{
            if ( 1 > args.length ) {
                System.out.println("java PcapManagerTest <url|filename|IP>");
                System.exit(-1);
            }
            String name = args[0];
            PcapManager pm = new PcapManager(name);
            TcpHandler th = new TcpHandler();
            System.err.println("");
            System.err.println("[*] isReadyRun: " + pm.isReadyRun());
            System.err.println("Waiting Packets....");
            PcapPacket pkt = pm.nextPacket();
            System.err.printf("********RUN********");
            th.inspect(pkt);
            System.err.println("*******************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
