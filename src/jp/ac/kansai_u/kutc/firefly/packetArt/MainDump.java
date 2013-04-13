package jp.ac.kansai_u.kutc.firefly.packetArt;
import java.util.Arrays;

import readTcpDump.ReadDumpForm;

public class MainDump {
	public static void main(String [] args) {
		//new Form("Entry", EntryForm.class).show();
		Form form = new Form("ReadDump", ReadDumpForm.class);
		//if(Arrays.asList(args).contains("-verbose"))
		form.setVisible(true);
		//frame.show();
		System.out.println("Closed.");
	}
}
