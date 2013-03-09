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
            System.err.println("");
            System.err.println("[*] isReadyRun: " + pm.isReadyRun());
            if(pm.isReadyRun()){
                System.err.println("********RUN********");
                //pm.handlePackets();
                PcapPacket p = pm.nextPacket();
                pm.packetHandler.protocolHandler(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
