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
            MusicStation th = new MusicStation();
            PcapPacket pkt = null;
            System.err.println("");
            System.err.println("[*] isReadyRun: " + pm.isReadyRun());
            System.err.println("Waiting Packets....");
            System.err.println("********RUN********");
            while (( pkt = pm.nextPacket() ) != null ) {
                System.err.printf(".");
                th.inspect(pkt);
            }
            System.in.read();
            System.err.println("*******************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
