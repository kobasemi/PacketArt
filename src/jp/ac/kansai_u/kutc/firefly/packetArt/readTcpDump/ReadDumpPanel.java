package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * このパネルはReadDumpFormのmainパネルです。
 *
 * @author sya-ke
 */
public class ReadDumpPanel extends JPanel{

    public final static int X = 600;
    public final static int Y = 800;

    private LinerPanel linerPanel;
    private PcapPanel pcapPanel;

    public ReadDumpPanel() {
        super();
        setPreferredSize(new Dimension(X,Y));

        linerPanel = new LinerPanel();
        linerPanel.setBounds(0, 0, LinerPanel.X, LinerPanel.Y);
        pcapPanel = new PcapPanel();
        pcapPanel.setBounds(0, LinerPanel.Y, PcapPanel.X, PcapPanel.Y);
        add(linerPanel);
        add(pcapPanel);
    }

    public void update() {
        pcapPanel.update();
    }
}
