package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.jnetpcap.packet.PcapPacket;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.Form;//TEST
import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.DrawUtil;

/**
 * このフォームはPcapManagerのopenDevとopenFileを実行するためだけのフォームです。
 *
 * @author sya-ke
 */
public class ReadDumpForm extends FormBase{

    private LoadButton loadButton;
    private int X;
    private int Y;
    private int counter;
    private final int MAX_ROOT = 30;//30個の始点
    private final int MAX = 1000;//1000個のつぎはぎ
    private PacketBar[] rootBars;
    private PcapManager pm;

    public void initialize() {
        X = getSize().width;//なんども呼び出さないように。
        Y = getSize().height;//なんども呼び出さないように。

        //本来必要な機能ここから
        loadButton = new LoadButton();
        loadButton.setBounds(0,0,X,Y);
        getContentPane().add(loadButton, 0);
        //本来必要な機能ここまで

        //描画用初期化ここから
        pm = PcapManager.getInstance();
        setBackground(Color.white);
        counter = 0;
        rootBars = new PacketBar[MAX_ROOT];
        for (int i=0;i<MAX_ROOT;i++)
            rootBars[i] = PacketBar.getStartPacketBar(new Point(X/2,Y/2));
        System.out.println(rootBars);
        //描画用初期化ここまで

        pm.debugMe("readumpform.init");
    }

    //パケットが残ってるなら棒を追加
    private synchronized void addBar(PacketBar root) {
        synchronized(barLock){
        if (root == null) {
            return;
        }
        int counter = 0;
        while(root.next != null) {
            root = root.next;//木の先端へ
            counter++;
        }
        if ( !(counter < MAX) ) {
            return;
        }
        PcapPacket pkt = null;
        PacketBar buf = null;
        pkt = pm.nextPacketFromQueue();
        if (pkt != null) {
            synchronized(root) {
                if (root == null) {
                    return;
                }
                buf = new PacketBar(root, pkt);
            }
            DrawUtil.pointResolver(buf.endPoint,X,Y);
            System.out.println(buf.endPoint);
            root.next = buf;
            return;
        }
        }
    }

    private Object barLock = new Object();
    public void update() {
        synchronized(barLock) {
            for (PacketBar root : rootBars) {
                    addBar(root);
            }
        }
    }

    public void paint(Graphics g) {
        synchronized(barLock) {
            Graphics2D g2 = (Graphics2D)g;
            BasicStroke wideStroke = new BasicStroke(2.0f);
            g2.setStroke(wideStroke);
            for (PacketBar root : rootBars) {
                if (root != null) {
                    int counter = 0;
                    while (root.next != null) {
                        root = root.next;
                        if (root.endPoint != null) {
                            g2.setColor(root.color);
                            g2.drawLine((int)root.prev.endPoint.getX(), (int)root.prev.endPoint.getY(),
                                (int)root.endPoint.getX(), (int)root.endPoint.getY() );
                            counter++;
                        }
                    }
                }
            }
        }
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

    public static void main(String[] args) {
        PcapManager pm = PcapManager.getInstance();
        pm.start();
        pm.debugMe("pm.start()");
        Form form = new Form("ReadDump", ReadDumpForm.class);
        form.setVisible(true);
    }
}
