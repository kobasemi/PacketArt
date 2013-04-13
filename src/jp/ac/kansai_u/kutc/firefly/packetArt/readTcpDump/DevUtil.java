package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

/**
 * Pcap.openLive()で使うdevNameを人間の分かる方法で取得できる。
 * 使い方：
 * StringBuilder errBuf = new StringBuilder();
 * DevUtil devUtil = new DevUtil(errBuf);
*/
public class DevUtil {

    private StringBuilder errBuf;//libpcapからのエラーをここに
    private List<PcapIf> allDevs;
    private ArrayList<DevInfo> allDevInfo;
    private boolean gotError;

    /**
     * すべてのNICの情報を取得します。
    */
    public ArrayList getAllDevInfo() {
        return allDevInfo;
    }

    /**
     * findAllDevsでエラーが出たかどうかを判定する。
     * エラーが出ている場合、get??By??系関数は使えない。
     * @return gotError デバイス情報の取得にエラーが出たか否か。
    */
    public boolean hasError() {
        return gotError;
    }

    /**
     * String型と違って、プリミティブじゃないから、この関数不要だと思うんだけど
     * 一応。hasErrorでエラー出た時に呼んでみるよいいかも。
     * @return errBuf libpcapからもらったエラーのString。
    */
    public StringBuilder getErrBuf() {
        return errBuf;
    }

    public DevUtil() {
        errBuf = new StringBuilder();
        allDevs = new ArrayList<PcapIf>();
        allDevInfo = new ArrayList<DevInfo>();
        if ( Pcap.findAllDevs(allDevs, errBuf) == Pcap.NOT_OK
            || allDevs.isEmpty() ) {
            System.err.printf("Couldn't read list of devices, error is %s",
             errBuf.toString());
            gotError = true;
        } else {
            for (PcapIf pcapIf : allDevs) {
                allDevInfo.add(new DevInfo(pcapIf));
            }
        }
    }

    /**
     * @param errorBuffer 呼び出し元からコピーしたエラーバッファー
    */
    public DevUtil(StringBuilder errorBuffer) {
        this();
        errBuf = errorBuffer.append(errBuf.toString());
    }

    /**
     * デバイスのIPからデバイス名を取得します。
     * エラーは出しません。
     * @param ip デバイスの持つIPアドレス。IPv6でもOK。
     * @return name デバイス名。該当無しならnull。
    */
    public String getDevNameByIP(String ip) {
        for (DevInfo devInfo : allDevInfo) {
            if (ip == devInfo.ipAddr || devInfo.ip6Addr.contains(ip)) {
                return devInfo.name;
            }
        }
        return null;
    }

    /**
     * デバイスのMACアドレスからデバイス名を取得します。
     * エラーは出しません。
     * @param macAddr デバイスの持つMACアドレス。フォーマットは00:CB:CA:D0:32:5A
     * @return name デバイス名。該当無しならnull。
    */
    public String getDevNameByMacAddr(String macAddr) {
        for (DevInfo devInfo : allDevInfo) {
            if (macAddr == devInfo.macAddr) {
                return devInfo.name;
            }
        }
        return null;
    }
}
