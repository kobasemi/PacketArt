package jp.ac.kansai_u.kutc.firefly.packetArt.test;

//import jp.ac.kansai_u.kutc.firefly.packetArt.*;
import jp.ac.kansai_u.kutc.firefly.packetArt.FormBase;
import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.*;
import jp.ac.kansai_u.kutc.firefly.packetArt.music.PacketDisposerI;

import java.lang.Runnable;
import java.io.File;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.jnetpcap.packet.PcapPacket;

public class MusicPacketTest extends FormBase {

    private LoadDumpFileButton fileButton;
    private PcapManager pcapManager;
    private PcapPacket pkt;

    private String tempFileName;
    private String fileName;
    private Runnable fileButton_OnActed;

    private PacketDisposerI pd;

    public static void main(String[] args) {
        pd = new PacketDisposerI();
        Form form = new Form("Entry", MusicPacketTest.class);
        form.setVisible(true);
        System.out.println("Closed.");
    }

    public void initialize() {
        pcapManager = new PcapManager();
        fileButton_OnActed = new Runnable(){
            public void run(){
                tempFileName = fileButton.getFileName();
                if (tempFileName != null) {
                    File f = new File(tempFileName);
                    if ( f.exists()) {
                        pcapManager.openFile(tempFileName);
                        if ( pcapManager.isReadyRun() ) {
                            fileName = tempFileName;
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
    }

    public void update() {
        pkt = pcapManager.nextPacket();
        if ( pkt != null ) {
            pd.inspect(pkt);
            if (pd.isFull()){
                
        } else {
            setFileButton();
        }
    }

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

    public void paint(Graphics g) {};
    public void mouseClicked(MouseEvent e) {};
    public void mousePressed(MouseEvent e) {};
    public void mouseReleased(MouseEvent e) {};
    public void mouseEntered(MouseEvent e) {};
    public void mouseExited(MouseEvent e) {};
    public void mouseMoved(MouseEvent e){};
    public void mouseDragged(MouseEvent e){};
    public void keyPressed(KeyEvent e) {};
    public void keyReleased(KeyEvent e) {};
    public void keyTyped(KeyEvent e) {};
    public void onFormChanged(){};

    public void onClose(){
        pcapManager.close();
    }

}
