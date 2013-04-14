package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;
import java.lang.Runnable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JButton;

/**
 * pcapのファイル名を読み込むためのGUIボタン部品。JButton。
 * 
*/
public class LoadDumpFileButton extends JButton {

    private static String fileName;
    public String getFileName(){ return fileName; }
    private Runnable onBeginAct;
    private Runnable onEndAct;

    /**
     * @param t ボタンに表示される説明 null NG
     * @param beforeActFunc 無名関数入れ。こいつに好きなrunを入れられる。nullOK
     * @param afterActFunc 無名関数入れ。こいつに好きなrunを入れられる。nullOK
     */
    public LoadDumpFileButton(String t, Runnable beforeActFunc, Runnable afterActFunc) {
        super(t);
        this.onBeginAct = beforeActFunc;
        this.onEndAct = afterActFunc;
        this.fileName = null;
        this.addActionListener(new OnClickListener());
    }

    private void onBeginAction() {
        if ( onBeginAct != null ) {
            onBeginAct.run();
        }
    }

    private void onEndAction() {
        if ( onEndAct != null ) {
            onEndAct.run();
        }
    }

    public class OnClickListener implements ActionListener {
        private JFileChooser chooser;

        OnClickListener() {
            this.chooser = new JFileChooser();
            this.chooser.addChoosableFileFilter(new DumpFileFilter());
        }

        public void actionPerformed(ActionEvent e) {
            onBeginAction();
            if ((int)chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION)
                fileName = chooser.getSelectedFile().getAbsolutePath();
            onEndAction();
        }

        public class DumpFileFilter extends FileFilter {
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
                    ext.equals(".cap") || ext.equals(".tcpdump")) {
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
                return ".dump .pcap .pcapdump .cap .tcpdumpの拡張子に対応";
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
}
