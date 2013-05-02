package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import jp.ac.kansai_u.kutc.firefly.packetArt.FormUtil;

/**
 * このボタンはFormChange用ののタンです。
 *
 * @author sya-ke
 */
public class FormChanger extends JButton implements ActionListener{

    String formChangeTo;
    public FormChanger(String text, String formName) {
        super(text);
        formChangeTo = formName;
        addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        FormUtil.getInstance().changeForm(formChangeTo);
    }
}
