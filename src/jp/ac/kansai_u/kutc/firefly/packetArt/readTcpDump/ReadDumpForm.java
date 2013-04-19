package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.lang.Runnable;
import java.io.File;
import java.awt.Color;
import java.awt.Graphics;
//import java.awt.BorderLayout;
/*import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
*/import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;

import org.jnetpcap.packet.PcapPacket;

import jp.ac.kansai_u.kutc.firefly.packetArt.Form;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;

/**
 * このフォームはユーザからファイル名を受けとる用です。<br>
 * ファイル名はgetFileName()で取得してください。<br>
 * このフォームってファイル名受け取ったら要らない子だよね～
 *
 * @author sya-ke
 */
public class ReadDumpForm extends FormBase {

    private LoadDumpFileButton fileButton;
    private PcapManager pcapManager;
    private String tempName;
    private String fileName;
    private Runnable fileButton_OnActed;
    public DevUtil devUtil;

    public void initialize() {
        pcapManager = PcapManager.getInstance();
        devUtil = new DevUtil();

        //ここからファイルロードボタン
        fileButton_OnActed = new Runnable(){
            public void run(){
                tempName = fileButton.getFileName();
                if (tempName != null) {
                    File f = new File(tempName);
                    if ( f.exists()) {
                        pcapManager.openFile(tempName);
                        if ( pcapManager.isReadyRun() ) {
                            fileName = tempName;
                            //このファイル名は、現在PcapManagerが保持しているものである
                            setFileButton();
                        } else {
                            //TODO: エラーメッセージをこのFormのどこかに表示
                            //エラー内容：Fileのオープンに失敗しました。
                        }
                    }
                }
            }
        };
        fileButton = new LoadDumpFileButton("tcpdumpファイルを開く",
            null, fileButton_OnActed);
        fileButton.setBounds((getSize().width / 3), (getSize().height / 5) * 3,
                                 getSize().width / 3, getSize().height / 5);
        getContentPane().add(fileButton, 0);
        //ここまでファイルロードボタン

       //ここからデバイスメニュー
        JComboBox deviceSelector = new JComboBox(devUtil.getGoodInformations());
        deviceSelector.setBounds(0, 0,
                                 getSize().width, getSize().height / 20);
        getContentPane().add(deviceSelector, 0);
        //ここまでデバイスメニュー

        //ここからデバイスロードボタン
        //if onclick then
        //String info = deviceSelector.getSelectedItem();
        //String name = devUtil.getNameByGoodInformation(info);
        //pcapManager.openDev(name);
        //ここからデバイスロードボタン
    }

    private String packetStream;
    PcapPacket pkt;

    public void update() {
        if (pcapManager.isReadyRun()) {
            pkt = pcapManager.nextPacket();
            if (pkt != null) {
                packetStream = pkt.toDebugString();
            } else {
                System.out.println("弾切れ！");
            }
        } else {
            packetStream = null;
        }
    }

    /**
     * PcapManagerの状態に応じてボタンのテキストを変更します。
    */
    public void setFileButton() { 
        if (fileName != null) {
            if( pcapManager.isReadyRun() ) {
                fileButton.setText("すでにロードされています。");
                fileButton.setVisible(false);
            } else if (pcapManager.isReadyRun() == false) {
                    //fileButton.setText("Pcapファイルが正しくロードされました。");
                    fileButton.setText("次のtcpdumpファイルが要ります。");
                    fileButton.setVisible(true);
            }
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.white);
        if (packetStream != null) {
            g.drawString(packetStream, 0,50);
        }
        g.drawString("Hello",100,200);
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
        //pcapManager.close();
        //pcapManager = null;
    }
}
