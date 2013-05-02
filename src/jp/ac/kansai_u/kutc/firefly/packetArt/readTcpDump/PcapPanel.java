package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JComboBox;

import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.OnPcapClosedHandler;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.OnNoPacketsLeftHandler;

/**
 * このパネルはPcapManagerのパネルです。
 *
 * @author sya-ke
 */
public class PcapPanel extends JPanel{
    public PcapPanel() {
        super();
        setLayout(new GridLayout(4,1));
        add(new FileFlow());
        add(new DeviceFlow());
        add(new BpfFlow());
        add(new QueueFlow());
    }
}

class FileFlow extends JPanel {
    public FileFlow() {
        super();
        final FileFlow parent = this;
        setLayout(new FlowLayout());

        final PcapManager pm = PcapManager.getInstance();
        JLabel label = new JLabel("ファイルからロード");
        final JTextField textField = new JTextField();
        textField.setEditable(false);
        textField.setText("選択ボタンを押してファイル名を入力");
        File f = pm.getPcapFile();
        if (f != null) {
            textField.setText(f.getAbsolutePath());
        }
        JButton button1 = new JButton("選択..");
        final JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = 
                new FileNameExtensionFilter("PcapDumpファイル",
                    "pcap", "pcapdump", "dump", "cap", "tcpdump", "pkt");
            fileChooser.addChoosableFileFilter(filter);
        button1.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selected = fileChooser.showOpenDialog(parent);
                    if (selected == JFileChooser.APPROVE_OPTION){
                        File file = fileChooser.getSelectedFile();
                        if (file != null) {
                            textField.setText(file.getAbsolutePath());
                        }
                    }
                }
            }
        );
        JButton button2 = new JButton("開く");
        button2.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if ( pm.openFile(textField.getText()) ) {
                        textField.setText("正常にロードされました");
                    } else {
                        textField.setText("エラー：" + pm.getErr());
                    }
                }
            }
        );
        add(label);
        add(textField);
        add(button1);
        add(button2);
        //TODO: OK画像,running画像
    }
}

class DeviceFlow extends JPanel {
    public DeviceFlow() {
        super();
        setLayout(new FlowLayout());

        final PcapManager pm = PcapManager.getInstance();
        JLabel label = new JLabel("デバイスからロード");
        final DeviceSelector comboBox1 = new DeviceSelector();
        final JButton button1 = new JButton("開く");
        button1.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if ( pm.openDev(comboBox1.getSelectedDevName()) ) {
                        button1.setText("正常にロードされました");
                    } else {
                        button1.setText("エラー：" + pm.getErr());
                    }
                }
            }
        );
        add(label);
        add(comboBox1);
        add(button1);
        //TODO: OK画像,running画像
    }
}
        

class DeviceSelector extends JComboBox implements ItemListener{

    private DevUtil devUtil;
    private String selectedDevName;

    public String getSelectedDevName() {
        return selectedDevName;
    }

    public DeviceSelector() {
        setEnabled(true);
        selectedDevName = "";
        devUtil = new DevUtil();
        String[] informations = devUtil.getGoodInformations();
        if (informations == null || informations.length < 1) {
            addItem("jnetpcapが利用可能なデバイスがありません！");
            setEnabled(false);
            return;
        }
        for (String information : informations) {
            addItem(information);
        }
        addItemListener(this);
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;//２度撃ち回避
        }
        String dname = devUtil.getNameByGoodInformation((String)getSelectedItem());
        if (dname != null) {
            selectedDevName = dname;
        }
    }
}

class BpfFlow extends JPanel {
    public BpfFlow() {
        super();
        setLayout(new FlowLayout());

        final PcapManager pm = PcapManager.getInstance();
        JLabel label = new JLabel("BPFフィルタ");
        final JTextField textField = new JTextField();
        textField.setEditable(true);
        textField.setText("");
        String bpf = pm.getBpfText();
        if (bpf != null) {
            textField.setText(bpf);
        }
        final JButton button1 = new JButton("適用");
        button1.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    textField.setEditable(false);
                    if ( pm.setBPFfilter(textField.getText()) ) {
                        button1.setText("OK");
                        (new Thread(new Runnable(){
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch(InterruptedException e) {
                                }
                                button1.setText("適用");
                            }
                        })).start();
                        textField.setEditable(true);
                    } else {
                        button1.setText("Error");
                        (new Thread(new Runnable(){
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch(InterruptedException e) {
                                }
                                button1.setText("適用");
                            }
                        })).start();
                        textField.setEditable(true);
                    }
                }
            }
        );
        add(label);
        add(textField);
        add(button1);
        //TODO: OK画像,running画像
    }
}

class QueueFlow extends JPanel {
    public QueueFlow() {
        super();
        setLayout(new FlowLayout());
        PcapManager pm = PcapManager.getInstance();
        JLabel label1 = new JLabel("残りパケット保持数");
        int Qsize = pm.getQueueLimit();
        JProgressBar progressBar = new JProgressBar(1,Qsize);
        int Qleft = pm.getQueueLeft();
        progressBar.setValue(Qleft);
        JLabel label2 = new JLabel(Qleft + " / " + Qsize);
        add(label1);
        add(progressBar);
        add(label2);
        //TODO: pm.setQueueLimit()ボタン
    }
}
