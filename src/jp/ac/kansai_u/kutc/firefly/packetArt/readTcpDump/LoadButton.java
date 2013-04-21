package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.OnPcapClosedHandler;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.OnPcapOpenedHandler;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.OnNoPacketsLeftHandler;

/**
 * pcapのファイル名を読み込むためのGUIボタン部品です。
 *
 * @author sya-ke
*/
public class LoadButton extends JButton implements ActionListener, OnPcapClosedHandler , OnNoPacketsLeftHandler {

    private PcapManager pm;
    private JFileChooser chooser;
    private DevUtil devUtil;

    /**
     * JButtonのコンストラクタです。
     *
     * @param t ボタンの上に表示される文字列です。
     * @param parentFrame ファイルChooserがアクティブを返すフレームです。
     */
    public LoadButton(String text) {
        super(text);
        init();
    }

    /**
     * JButtonのコンストラクタです。
     *
     * @param parentFrame ファイルChooserがアクティブを返すフレームです。
     */
    public LoadButton() {
        this("Open Pcap...");
    }

    public void init() {
        this.setBorderPainted(false);
        this.addActionListener(this);
        chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new Filter());
        pm = PcapManager.getInstance();
        pm.addHandler(this);
        devUtil = new DevUtil();
    }

    public void actionPerformed(ActionEvent e) {

        String options[] = {"<Device>", "<File>"};
        int select = JOptionPane.showOptionDialog(this,
          "You want packets from..", 
          "Select <Device> or <File>", 
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null, 
          options,
          options[0]
        );

        if (select == JOptionPane.CLOSED_OPTION){
            setText("ひやかしかい？");
        }else{
            if ( select == 0 ) {
                openDeviceChooser();
            } else if (select == 1) {
                openFileChooser();
            }
        }
    }

    public void openDeviceChooser() {
        String[] informations = devUtil.getGoodInformations();
        if (informations == null || informations.length < 1) {
            JOptionPane.showMessageDialog(this, "利用可能なデバイスがありません！");
            return;
        }
        Object information = JOptionPane.showInputDialog(
            this , "利用可能なデバイス" , "デバイスを選択してください。" ,
            JOptionPane.INFORMATION_MESSAGE , null , informations, informations[0]
        );
        if (information == null) {
            setText("ひやかしかい？");
            return;
        }
        String device = devUtil.getNameByGoodInformation(information.toString());
        if (device != null) {
            if (pm.openDev(device)) {
                setText("Opening Device   ' " + information +  " ' => Sucess.");
                return;
            } else {
                setText("Opening Device   ' " + information + " ' => FAIL."
                        + "      " + pm.getErr()
                );
            }
        }
        
    }

    public void openFileChooser() {
        File f = null;
        if ((int)chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
            f = chooser.getSelectedFile();
            if (f != null) {
                if (pm.openFile(f.getAbsolutePath())) {
                    setText("Opening File   ' " + f.getName() +  " ' => Sucess.");
                    return;
                } else {
                    setText("Opening File   ' " + f.getName() + " ' => FAIL."
                        + "       " + pm.getErr()
                    );
                }
            }
        } else {
            setText("ひやかしかい？");
        }
    }

    public void onPcapClosed() {
        setText("File or Device Closed. Click me");
        pm = null;
    }

    public void onNoPacketsLeft() {
        setText("GIVE ME MORE PCAP! CLICK ME!");
    }

    public class Filter extends FileFilter {
    //http://www.javadrive.jp/tutorial/jfilechooser/index12.html
        public boolean accept(File f) {
        /* ディレクトリなら無条件で表示する */
            if (f.isDirectory()) {
                return true;
            }
            /* 拡張子を取り出し、html又はhtmだったら表示する */
            String ext = getExtension(f);
            if (ext != null) {
                if (ext.equals("dump") || ext.equals("pcap") || ext.equals("pcapdump") ||
                ext.equals("cap") || ext.equals("tcpdump")) {
                //ext.equals(".dump.tar.gz") || ext.equals(".dump.tgz")) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
        public String getDescription() {
            //return ".dump .pcap .dump.tar.gz .dump.tgzの拡張子に対応";
            return ".dump .pcap .pcapdump .cap .tcpdump";
        }
        /* 拡張子を取り出す */
        private String getExtension(File f) {
            String ext = null;
            String fname = f.getName();
            int dotIndex = fname.lastIndexOf('.');
            if ((dotIndex > 0) && (dotIndex < fname.length() - 1)) {
                ext = fname.substring(dotIndex + 1).toLowerCase();
            }
            return ext;
        }
    }
}
