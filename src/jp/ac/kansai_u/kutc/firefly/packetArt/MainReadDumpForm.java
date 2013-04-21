package jp.ac.kansai_u.kutc.firefly.packetArt;
//import java.util.Arrays;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.ReadDumpForm;

public class MainReadDumpForm {
	public static void main(String [] args) {
        PcapManager p = PcapManager.getInstance();
        p.start();
		//new Form("Entry", EntryForm.class).show();
		Form form = new Form("ReadDump", ReadDumpForm.class);
		//if(Arrays.asList(args).contains("-verbose"))
		form.setVisible(true);
		//frame.show();
		System.out.println("Closed.");
        p.kill();
        p.close();
        p = null;
	}
}
