package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.protocol.JProtocol;

import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.PacketHandler;

import jp.ac.kansai_u.kutc.firefly.packetArt.PlaySE;//TEST

/**
 * このパネルはPcapManagerのパネルです。
 *
 * @author sya-ke
 */
public class LinerPanel extends JPanel implements PacketHandler{

    public static final int X = 600;
    public static final int Y = 600;
    private final int FONT_Y = 15;//割り切れるし、なんとなく40個列を確保。
    private final Font font = new Font(Font.MONOSPACED, Font.BOLD, FONT_Y);
    private final int FONT_X = getJLabelFontHeight(font);
    private final int GRID_X = X / FONT_X;//この値は環境のフォント幅による。
    private final int GRID_Y = Y / FONT_Y;//常に100
    private JLabel[][] labels;
    public int counter;
    public PlaySE playSE;//TEST

    //MONOSPACEDは等幅フォント。文字通り、文字の幅が統一され、
    //描画しやすくなる。BOLDは太字。これも文字をできるだけベタ塗りにして
    //文字を一個の絵のピースのように見立てる。
    //ポイントは6にし600x600を割り切れるようにして、縦幅を隙間なく詰め込む.
    //つまり、600x600を幅で、の文字格子にする

    public LinerPanel() {
        super();
        playSE = new PlaySE();//TEST
        counter = 0;
        setPreferredSize(new Dimension(X,Y));
        setLayout(new GridLayout(GRID_X,GRID_Y, 0, 0));
        labels = new JLabel[GRID_X][GRID_Y];
        initLabels("@", Color.RED, Color.BLACK);
    }

    public void handlePacket(PcapPacket pkt) {
        if (counter >= GRID_X*GRID_Y) {
            counter = 0;
            refreshLabels("$", Color.PINK, Color.BLACK);
            return;
        }
        JLabel label = labels[counter%GRID_X][counter/GRID_X];
        try {
            //WIDEの集めるパケッｔのOSI参照モデルの最上階はTCP/UDP
            if (pkt.hasHeader(JProtocol.TCP_ID)) {
                playSE.play(PlaySE.SELECT);//TEST
                label.setText("T");//TCPならT
                if (pkt.hasHeader(JProtocol.IP4_ID)) {
                    label.setForeground(Color.YELLOW);//IPv4が下の時の文字色
                } else if (pkt.hasHeader(JProtocol.IP6_ID)) {
                    label.setForeground(Color.BLUE);//IPv6が下の時の文字色
                } else {
                    label.setForeground(Color.RED);//それ以外が下の時の文字色
                }
            } else if (pkt.hasHeader(JProtocol.UDP_ID)) {
                playSE.play(PlaySE.CANCEL);//TEST
                label.setText("U");//UDPならU
                if (pkt.hasHeader(JProtocol.IP4_ID)) {
                    label.setForeground(Color.GREEN);//IPv4が下の時の文字色
                } else if (pkt.hasHeader(JProtocol.IP6_ID)) {
                    label.setForeground(Color.BLUE);//IPv6が下の時の文字色
                } else {
                    label.setForeground(Color.RED);//それ以外が下の時の文字色
                }
            //OSI参照モデルにおいて。ICMPはL3だが、
            //一般に、ICMPの上にTCPやUDPが来ることは少ないのでここで処理。
            } else if (pkt.hasHeader(JProtocol.ICMP_ID)) {
                playSE.play(PlaySE.TURN);//TEST
                label.setText("I");//ICMPならI
                if (pkt.hasHeader(JProtocol.ETHERNET_ID)) {
                    label.setForeground(Color.CYAN);//IPv4が下の時の文字色
                } else if (pkt.hasHeader(JProtocol.PPP_ID)) {
                    label.setForeground(Color.BLUE);//PPPが下の時の文字色
                } else {
                    label.setForeground(Color.WHITE);//それ以外が下の時の文字色
                }
            } else {//TCP,UDP,ICMPを含まないパケットはこちらへ。
                playSE.play(PlaySE.HARDDROP);//TEST
                if (pkt.hasHeader(JProtocol.IP6_ID)) {
                    label.setText("6");//IPv6なら6
                    if (pkt.hasHeader(JProtocol.ETHERNET_ID)) {
                        label.setForeground(Color.RED);//Ethernetが下の時の文字色
                    } else if (pkt.hasHeader(JProtocol.PPP_ID)) {
                        label.setForeground(Color.BLUE);//PPPが下の時の文字色
                    } else {
                        label.setForeground(Color.WHITE);//それ以外が下の時の文字色
                    }
                } else if (pkt.hasHeader(JProtocol.IP4_ID)) {
                    label.setText("4");
                    if (pkt.hasHeader(JProtocol.ETHERNET_ID)) {
                        label.setForeground(Color.PINK);//Ethernetが下の時の文字色
                    } else if (pkt.hasHeader(JProtocol.PPP_ID)) {
                        label.setForeground(Color.BLUE);//PPPが下の時の文字色
                    } else {
                        label.setForeground(Color.WHITE);//それ以外が下の時の文字色
                    }
                } else {
                    //ここ、L3調査がメインなWIDEでほとんど来ること無いと思う。
                    //珍しいし、背景赤いろにしよう。。
                    if (pkt.hasHeader(JProtocol.L2TP_ID)) {
                        label.setForeground(Color.BLACK);
                        label.setText("L");
                        label.setBackground(Color.RED);
                    } else if (pkt.hasHeader(JProtocol.PPP_ID)) {
                        label.setForeground(Color.BLACK);
                        label.setText("P");
                        label.setBackground(Color.RED);
                    } else if (pkt.hasHeader(JProtocol.ARP_ID)) {
                        label.setForeground(Color.BLACK);
                        label.setText("A");
                        label.setBackground(Color.RED);
                    } else if (pkt.hasHeader(JProtocol.ETHERNET_ID)) {
                        label.setForeground(Color.BLACK);
                        label.setText("E");
                        label.setBackground(Color.RED);
                    } else {//jnetpcap
                        System.out.println("WHAT THE PACKET!???");
                        label.setForeground(Color.BLACK);
                        label.setText("!");
                        label.setBackground(Color.WHITE);
                    }
                }
            }
        } catch(Exception e) {
            //TCPの断片がえぐられたあるパケットに由来するjnetpcapバグ。スルー。
        } finally {
            //バグパケットは"@"のままにする。
            counter++;
        }
    }

    private void initLabels(String id, Color fgColor,Color bgColor) {
        String idChar = id.substring(0,1);
        for (int y=0;y<GRID_Y;y++) {
            for (int x=0;x<GRID_X;x++) {
                labels[x][y] = new JLabel(idChar);
                labels[x][y].setFont(font);
                labels[x][y].setForeground(fgColor);
                labels[x][y].setBackground(bgColor);
                labels[x][y].setOpaque(true);
            }
        }
        for (JLabel[] labelz: labels) {
            for (JLabel label: labelz) {
                add(label);
            }
        }
    }

    private void refreshLabels(String id, Color fgColor,Color bgColor) {
        String idChar = id.substring(0,1);
        for (int y=0;y<GRID_Y;y++) {
            for (int x=0;x<GRID_X;x++) {
                labels[x][y].setText(idChar);
                labels[x][y].setForeground(fgColor);
                labels[x][y].setBackground(bgColor);
            }
        }
    }

    private static int getJLabelFontHeight(Font font) {
        JLabel temp = new JLabel("A");
        return temp.getFontMetrics(font).getHeight();
    }

}
