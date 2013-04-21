package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import jp.ac.kansai_u.kutc.firefly.packetArt.Form;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.TcpHandler;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.UdpHandler;

/**
 * このフォームはPcapManagerのopenDevとopenFileを実行するためだけのフォームです。
 *
 * @author sya-ke
 */
public class ReadDumpForm extends FormBase implements TcpHandler, UdpHandler {

    private LoadButton loadButton;
    private int counter;
    private int X;
    private int Y;
    private int MAX;
    private PcapManager pm;
    private int[][] rects;
    private final Color[] colors = {Color.red,Color.green,Color.blue,Color.yellow};
    private boolean[] flag;

    public void initialize() {
        X = getSize().width;
        Y = getSize().height;
        MAX = 4;
        pm = PcapManager.getInstance();
        //counter = 0;
        setBackground(Color.white);
        loadButton = new LoadButton();
        System.out.println(X);
        System.out.println(Y);
        loadButton.setBounds(0,0,X,Y);
        getContentPane().add(loadButton, 0);
        pm.addHandler(this);
        pm.debugMe("ReadDumpForm.init");
        rects = new int[4][MAX];//4つの隅。
        //[x,y,width,height][個数]
        flag = new boolean[4];
    }

//    http://jnetpcap.com/docs/javadoc/latest/org/jnetpcap/protocol/tcpip/Tcp.html
    public void handleTcp(Tcp tcp){
        //if (MAX <= counter) {
        //    pm.removeHandler(this);
        //    return;
        //}
        //counter++;
        if (tcp.flags_SYN()) {
            rects[0][0] = tcp.source() % X;
            rects[0][1] = tcp.destination() % Y;
            rects[0][2] = tcp.checksum() % X; 
            rects[0][3] = tcp.window() % Y;
            System.out.println("rects[0] => OK");
            flag[0] = true;
        } else if (tcp.flags_PSH()){
            rects[1][0] = tcp.source() % X;
            rects[1][1] = tcp.destination() % Y;
            rects[1][2] = tcp.checksum() % X; 
            rects[1][3] = tcp.window() % Y;
            System.out.println("rects[1] => OK");
            flag[1] = true;
        }
    }


// http://jnetpcap.com/docs/javadoc/latest/org/jnetpcap/protocol/tcpip/Udp.html
    public void handleUdp(Udp udp){
        //if (MAX <= counter) {
        //    pm.removeHandler(this);
        //    return;
        //}
        //counter++;
        if (udp.length() % 2 == 0) {
            rects[2][0] = udp.source() % X;
            rects[2][1] = udp.destination() % Y;
            rects[2][2] = udp.checksum() % X;
            rects[2][3] = udp.length() % Y;
            System.out.println("rects[2] => OK");
            flag[2] = true;
        } else if (udp.length() % 2 == 1){
            rects[3][0] = udp.source() % X;
            rects[3][1] = udp.destination() % Y;
            rects[3][2] = udp.checksum() % X;
            rects[3][3] = udp.length() % Y;
            System.out.println("rects[3] => OK");
            flag[3] = true;
        }
    }

    public void update() {
    }

    public void paint(Graphics g) {
        //if ( flag[0] & flag[1] & flag[2] & flag[3] ) {
            for (int i= 0;i<4;i++) {
                g.setColor(colors[i]);
                ((Graphics2D)g).draw3DRect(rects[i][0],rects[i][1],50,60, true);//rect[2],rect[3], false);
                flag[i] = false;
            }
        //}
    }

    public void mouseClicked(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e){
    }
    public void mouseDragged(MouseEvent e){
    }
    public void keyPressed(KeyEvent e) {
    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
    public void onFormChanged(){
    }
    public void onClose(){
        pm.removeHandler(this);
    }
}
