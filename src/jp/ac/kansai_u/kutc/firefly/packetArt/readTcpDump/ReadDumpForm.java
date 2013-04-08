package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

/* このファイルがクラスの基本的な構造と使い方 
 * テンプレートをコピー→メソッドを編集
 */

/**
 * フォームです.
 */
public class ReadDumpForm extends FormBase {

    private JButton loadButton;
    private String fileName;
    private String tempFileName;

    public String getFileName() { return fileName; }

    public void initialize() {
        loadButton = new JButton("ファイルを開く");
        loadButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFileChooser chooser = new JFileChooser();
                if((int)chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION)
                    tempFileName = chooser.getSelectedFile().getAbsolutePath();
                if (tempFileName != null) {
                    loadButton.setText(tempFileName);
                    fileName = tempFileName;
                    //loadButton.setVisible(false);
                }
            }
        });
        loadButton.setBounds((getSize().width / 3) , (getSize().height / 5) * 3, getSize().width / 3, getSize().height / 5);
        getContentPane().add(loadButton, 0);
    }

    public void paint(Graphics g) {
    }

    public void update() {
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
