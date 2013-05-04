package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.io.File;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.OnPcapClosedHandler;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.OnNoPacketsLeftHandler;

/**
 * このパネルはPcapManagerのパネルです。
 *
 * @author sya-ke
 */
public class PcapPanel extends JPanel{

    private FileFlow fileFlow;
    private DeviceFlow deviceFlow;
    private BpfFlow bpfFlow;
    private QueueFlow queueFlow;
    public final static int X = 600;
    public final static int Y = 200;

    public PcapPanel() {
        super();
        setLayout(new GridLayout(5,1));
        setPreferredSize(new Dimension(X,Y));

        fileFlow = new FileFlow();
        deviceFlow = new DeviceFlow();
        bpfFlow = new BpfFlow();
        queueFlow = new QueueFlow();
        FormChanger formChangerButton = new FormChanger("タイトルへ戻る", "Title");
        
        add(fileFlow);
        add(deviceFlow);
        add(bpfFlow);
        add(queueFlow);
        add(formChangerButton);
    }
    public void update() {
        queueFlow.update();
    }
}

class FileFlow extends JPanel {
    public FileFlow() {
        super();
        final FileFlow parent = this;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("ファイルからロード");
        final JTextField textField = new JTextField("選択ボタンを押してファイル名を入力してください", 30);
        textField.setPreferredSize(new Dimension(200, 24));
        textField.setEditable(false);
        File f = PcapManager.getInstance().getPcapFile();
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
        final JButton button2 = new JButton("開く");
        button2.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if ( PcapManager.getInstance().openFile(textField.getText()) ) {
                        button2.setText("OK");
                        (new Thread(new Runnable(){
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch(InterruptedException e) {
                                }
                                button2.setText("開く");
                            }
                        })).start();
                    } else {
                        textField.setText("ERROR!: " + 
                                PcapManager.getInstance().getErr());
                        (new Thread(new Runnable(){
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch(InterruptedException e) {
                                }
                                button2.setText("開く");
                            }
                        })).start();
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
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("デバイスからロード");
        final DeviceSelector comboBox1 = new DeviceSelector();
        final JButton button1 = new JButton("開く");
        button1.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String buf = (String)comboBox1.getSelectedItem();
                    if ( PcapManager.getInstance().openDev(
                                comboBox1.getSelectedDevName()) ) {
                        button1.setText("OK");
                        (new Thread(new Runnable(){
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                } catch(InterruptedException e) {
                                }
                                button1.setText("開く");
                            }
                        })).start();
                    } else {
                        JOptionPane.showMessageDialog(
                            null, "ERROR: " + PcapManager.getInstance().getErr(), 
                            "ERROR!",JOptionPane.ERROR_MESSAGE);
                        button1.setText("エラー：" + PcapManager.getInstance().getErr());
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
        setPreferredSize(new Dimension(350, 24));
        setEnabled(true);
        devUtil = new DevUtil();
        String[] informations = devUtil.getGoodInformations();
        if (informations == null || informations.length < 1) {
            addItem("jnetpcapが利用可能なデバイスがありません！");
            setEnabled(false);
            return;
        }
        for (String information : informations) {
            addItem(information);
            selectedDevName = devUtil.getNameByGoodInformation(information);
            setSelectedItem(information);//大体最後のデバイスが正解だった。
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
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel("BPFフィルタ");
        final JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(350,24));
        textField.setEditable(true);
        textField.setText("");
        String bpf = PcapManager.getInstance().getBpfText();
        if (bpf != null) {
            textField.setText(bpf);
        }
        final JButton button1 = new JButton("適用");
        button1.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    textField.setEditable(false);
                    if ( PcapManager.getInstance().setBPFfilter(
                                textField.getText()) ) {
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
    
    private JProgressBar progressBar;
    private JLabel label2;
    private PcapManager pm;

    public QueueFlow() {
        super();
        pm = PcapManager.getInstance();
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label1 = new JLabel("残りパケット/MAX");
        int Qsize = PcapManager.getInstance().getQueueLimit();
        progressBar = new JProgressBar(1,Qsize);
        int Qleft = PcapManager.getInstance().getQueueLeft();
        progressBar.setValue(Qleft);
        progressBar.setPreferredSize(new Dimension(380,24));
        label2 = new JLabel(Qleft + " / " + Qsize);
        add(label1);
        add(progressBar);
        add(label2);
        //TODO: pm.setQueueLimit()ボタン
    }
    public void update() {
        int Qsize = pm.getQueueLimit();
        int Qleft = pm.getQueueLeft();
        //if update Limit == true Qsiz;;;;
        progressBar.setValue(Qleft);
        label2.setText(Qleft + " / " + Qsize);
    }
}
