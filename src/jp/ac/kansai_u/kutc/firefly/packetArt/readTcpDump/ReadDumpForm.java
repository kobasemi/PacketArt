package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.Form;//TEST
import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

/**
 * このフォームはPcapManagerのopenDevとopenFileを実行するためだけのフォームです。
 *
 * @author sya-ke
 */
public class ReadDumpForm extends FormBase{

    private ReadDumpPanel readDumpPanel;
    private boolean inited;
    private PcapManager pm = PcapManager.getInstance();

    public ReadDumpForm() {
        super();
        inited = false;
        readDumpPanel = new ReadDumpPanel();
    }

    @Override
    public void initialize() {
        if (inited != true) {
            Container contentPane = getContentPane();
            //縦にコンポーネントを配置する

            //コンポーネント配置ここから
            pm.addHandler(readDumpPanel.linerPanel);
            readDumpPanel.setBounds(0, 0, ReadDumpPanel.X, ReadDumpPanel.Y);
            contentPane.add(readDumpPanel, 0);
            //コンポーネント配置ここまで

            //描画用初期化ここから
            //setBackground(Color.WHITE);
            //描画用初期化ここまで
            inited = true;
        }
    }

    public void update() {
        readDumpPanel.pcapPanel.update();
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
        pm.removeHandler(readDumpPanel.linerPanel);
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
