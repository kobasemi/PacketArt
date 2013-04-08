package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.lang.Runnable;
import java.io.File;

//import java.awt.Color;
import java.awt.Graphics;
/*import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
*/import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.PcapManager;

/**
 * このフォームはユーザからファイル名を受けとる用です。
 * ファイル名はgetFileName()で取得してください。
 * このフォームってファイル名受け取ったら要らない子だよね。
 */
public class ReadDumpForm extends FormBase {

    private LoadDumpFileButton fileButton;
    private PcapManager pcapManager;
    private String tempFileName;
    private String fileName;
    private Runnable fileButton_OnActed;

    public String getFileName() { return fileName; }

    public void initialize() {
        pcapManager = new PcapManager();

        fileButton_OnActed = new Runnable(){
            public void run(){
                tempFileName = fileButton.getFileName();
                File f = new File(tempFileName);
                if ( f.exists()) {
                    pcapManager.openFile(tempFileName);
                    if ( pcapManager.isReadyRun() ) {
                        fileName = tempFileName;
                        //このファイル名は、現在PcapManagerが保持しているものである
                    } else {
                        //TODO: エラーメッセージをこのFormのどこかに表示
                        //エラー内容：Fileのオープンに失敗しました。
                    }
                }
            }
        };
        fileButton = new LoadDumpFileButton("tcpdumpファイルを開く",
            null, fileButton_OnActed);
        //TODO: 既に読み込んでいて、パケットも使い切ってない場合は？
        fileButton.setBounds((getSize().width / 3), (getSize().height / 5) * 3,
                                 getSize().width / 3, getSize().height / 5);
        getContentPane().add(fileButton, 0);
    }

    public void update() {
        if (fileName != null) {
            if( pcapManager.isReadyRun() ) {
                fileButton.setText("すでにロードされています。");
                //fileButton.setVisible(false);
            } else if (pcapManager.isReadyRun() == false) {
                     //fileButton.setText("Pcapファイルが正しくロードされました。");
                     fileButton.setText("次のtcpdumpファイルが要ります。");
            }
        }
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
