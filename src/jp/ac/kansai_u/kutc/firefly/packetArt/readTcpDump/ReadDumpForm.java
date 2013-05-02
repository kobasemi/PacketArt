package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;//TEST
import jp.ac.kansai_u.kutc.firefly.packetArt.Form;//TEST
import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

/**
 * このフォームはPcapManagerのopenDevとopenFileを実行するためだけのフォームです。
 *
 * @author sya-ke
 */
public class ReadDumpForm extends FormBase{

    PcapPanel pcapPanel;

    public void initialize() {
        int X = getSize().width;
        int Y = getSize().height;
        Container contentPane = getContentPane();
        //contentPane.setLayout(new GridLayout(2, 1));

        //コンポーネント配置ここから
        pcapPanel = new PcapPanel();
        //pcapPanel.setPreferredSize(new Dimension(X,Y*(2/3)));
        pcapPanel.setPreferredSize(new Dimension(X,Y));
        //pcapPanel.setBounds(0,0,X,Y*(2/3));
        pcapPanel.setBounds(0,0,X,Y);
        contentPane.add(pcapPanel, 0);
        //コンポーネント配置ここまで

        //描画用初期化ここから
        setBackground(Color.white);
        //描画用初期化ここまで
    }

    public void update() {
        pcapPanel.update();
    }

    public void paint(Graphics g) {
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
    }

    public static void main(String[] args) {
        PcapManager pm = PcapManager.getInstance();
        pm.start();
        Form form = new Form("ReadDump", ReadDumpForm.class);
        form.setVisible(true);
    }
}
