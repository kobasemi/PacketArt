package jp.ac.kansai_u.kutc.firefly.packetArt;
//import java.util.Arrays;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.ReadDumpForm;

public class MainReadDumpForm {
    public static void main(String [] args) {
        PcapManager p = PcapManager.getInstance();
        p.debugMe("MainReadDumpForm.main");

        Form form = new Form("ReadDump", ReadDumpForm.class);

        p.start();
        form.setVisible(true);
        p.debugMe("MainReadDumpForm.mainEnd");
	}
}
