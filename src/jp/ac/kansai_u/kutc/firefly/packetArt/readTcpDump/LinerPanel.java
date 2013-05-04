package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JPanel;
import javax.swing.JLabel;

import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.TcpHandler;

/**
 * このパネルはPcapManagerのパネルです。
 *
 * @author sya-ke
 */
public class LinerPanel extends JPanel{

    public static final int X = 600;
    public static final int Y = 600;
    private final int FONT_Y = 15;//割り切れるし、なんとなく40個列を確保。
    private final Font font = new Font(Font.MONOSPACED, Font.BOLD, FONT_Y);
    private final int FONT_X = getJLabelFontHeight(font);
    private final int GRID_X = X / FONT_X;//この値は環境のフォント幅による。
    private final int GRID_Y = Y / FONT_Y;//常に100
    private JLabel[][] labels;

    //MONOSPACEDは等幅フォント。文字通り、文字の幅が統一され、
    //描画しやすくなる。BOLDは太字。これも文字をできるだけベタ塗りにして
    //文字を一個の絵のピースのように見立てる。
    //ポイントは6にし600x600を割り切れるようにして、縦幅を隙間なく詰め込む.
    //つまり、600x600を幅で、の文字格子にする

    public LinerPanel() {
        super();
        setPreferredSize(new Dimension(X,Y));
        setLayout(new GridLayout(GRID_X,GRID_Y, 0, 0));
        labels = new JLabel[GRID_X][GRID_Y];
        initLabels();
    }

    private void initLabels() {
        for (int y=0;y<GRID_Y;y++) {
            for (int x=0;x<GRID_X;x++) {
                JLabel label = labels[x][y];
                if (label == null) {
                    label = new JLabel("@");
                    label.setFont(font);
                    label.setForeground(Color.GREEN);
                    label.setBackground(Color.BLACK);
                    label.setOpaque(true);
                    add(label);
                }
            }
        }
    }

    private static int getJLabelFontHeight(Font font) {
        JLabel temp = new JLabel("A");
        return temp.getFontMetrics(font).getHeight();
    }

}
