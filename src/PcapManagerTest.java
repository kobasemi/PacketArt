class PcapManagerTest {
    public static void main(String[] args) {
        try{
            if ( 1 > args.length ) {
                System.out.println("java PcapManagerTest name");
                System.exit(-1);
            }
            String name = args[0];
            PcapManager pm = new PcapManager(name);
            System.err.println("");
            System.err.println("[*] isReadyRun: " + pm.isReadyRun());
            if(pm.isReadyRun()){
                pm.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
