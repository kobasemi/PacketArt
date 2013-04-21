package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import jp.ac.kansai_u.kutc.firefly.packetArt.Form;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

/**
 * このフォームはPcapManagerのopenDevとopenFileを実行するためだけのフォームです。
 *
 * @author sya-ke
 */
public class ReadDumpForm extends FormBase {

    private LoadButton loadButton;
    private PcapManager pcapManager;

    public void initialize() {
        setBackground(Color.white);
        loadButton = new LoadButton();
        loadButton.setBounds(0,0,getSize().width,getSize().height);
        getContentPane().add(loadButton, 0);
    }

    public void update() {
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
}
