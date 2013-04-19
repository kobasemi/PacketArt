package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

/**
 * Pcap.openLive()で使うdevNameを人間の分かる方法で取得できる。<br>
 * 使い方：<br>
 * <code>
 * StringBuilder errBuf = new StringBuilder();
 * DevUtil devUtil = new DevUtil(errBuf);
 * devUtil.getDevDescriptionByName();
 * </code>
 *
 * @author sya-ke
*/
public class DevUtil {

    private StringBuilder errBuf;//libpcapからのエラーをここに
    private List<PcapIf> allDevs;
    private ArrayList<DevInfo> allDevInfo;
    private boolean gotError;

    /*
     * テスト用。
    */
    public static void main(String[] args) {
        DevUtil d = new DevUtil();
        System.out.println("------------------");
        for (DevInfo i : d.getAllDevInfo() ) {
            System.out.println(i.getStats());
        }
        System.out.println("------------------");
        for (String i : d.getGoodInformations()) {
            System.out.println(i);
        }
        System.out.println("------------------");
    }

    /**
     * すべてのNICの情報を取得します。
     * 
     * @return alldevInfo Listで全てのDevInfoオブジェクトを返します。
    */
    public List<DevInfo> getAllDevInfo() {
        return allDevInfo;
    }

    /**
     * findAllDevsでエラーが出たかどうかを判定する。<br>
     * エラーが出ている場合、get??By??系関数は使えない。
     *
     * @return gotError デバイス情報の取得にエラーが出たか否か。
    */
    public boolean hasError() {
        return gotError;
    }

    /**
     * String型と違って、プリミティブじゃないから、この関数不要だと思うんだけど<br>
     * 一応。hasErrorでエラー出た時に呼んでみるよいいかも。
     *
     * @return errBuf libpcapからもらったエラーのString。
    */
    public StringBuilder getErrBuf() {
        return errBuf;
    }

    /**
     * コンストラクタ。失敗した場合とデバイスが存在しない場合に<br>
     * gotErrorにtrueが入ります。普通は失敗しません。
    */
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
     * コンストラクタ。エラー内容を格納するオブジェクトを引き継ぎます。
     *
     * @param errorBuffer 呼び出し元からコピーしたエラーバッファー
    */
    public DevUtil(StringBuilder errorBuffer) {
        this();
        errBuf = errorBuffer.append(errBuf.toString());
    }

    /**
     * デバイスのIPからデバイス名を取得します。<br>
     * エラーは出しません。
     *
     * @param ip デバイスの持つIPアドレス。IPv6でもOK。
     * @return name IPにひも付けされたデバイス名。該当無しならnull。
    */
    public String getNameByIP(String ip) {
        for (DevInfo devInfo : allDevInfo) {
            if (ip.equals(devInfo.ipAddr) || devInfo.ip6Addr.contains(ip)) {
                return devInfo.name;
            }
        }
        return null;
    }

    /**
     * デバイスのMACアドレスからデバイス名を取得します。<br>
     * エラーは出しません。
     *
     * @param macAddr デバイスの持つMACアドレス。フォーマットは00:CB:CA:D0:32:5A
     * @return name MACアドレスにひも付けされたデバイス名。該当無しならnull。
    */
    public String getNameByMacAddr(String macAddr) {
        for (DevInfo devInfo : allDevInfo) {
            if (macAddr.equals(devInfo.macAddr)) {
                return devInfo.name;
            }
        }
        return null;
    }

    /**
     * 人間の読めるデバイスの説明文からデバイス名を抽出します。この関数、いる？
     *
     * @param description デバイスの説明文。メーカ名とあ書いてある。
     * @return name 説明文にひも付けされたデバイス名です。該当なしならnullです。
    */
    public String getNameByDescription(String description) {
        for (DevInfo devInfo : allDevInfo) {
            if (description.equals(devInfo.description)) {
                return devInfo.name;
            }
        }
        return null;
    }

    /**
     * ループバックデバイスからデバイス名を抽出します。<br>
     * Linuxならほぼ確実に"lo" が返ってきます。Windowsならnullが返ってくるはず。
     *
     * @return name ループバックデバイス名です。該当なしならnullです。
    */
    public String getNameByLoopback() {
        for (DevInfo devInfo : allDevInfo) {
            if (devInfo.loopback) {
                return devInfo.name;
            }
        }
        return null;
    }

    /**
     * 結局、これが一番安定して有益な情報を取得できます。<br>
     * "MACアドレス デバイスの説明"のリストを返します。
     * 
     * @return ret "MACアドレス デバイスの説明"のString配列を返します。
    */
    public String[] getGoodInformations() {
        String[] ret = new String[ allDevs.size() ];
        int i=0;
        for (DevInfo devInfo : allDevInfo) {
            String buf = "";
            buf += devInfo.macAddr + " " + devInfo.description;
            if (devInfo.ipAddr != null) {
                buf += " " + devInfo.ipAddr;
            }
            ret[i] = buf;
            i++;
        }
        return ret;
    }

    /**
     * ↑のメソッドから得た情報を元に、デバイス名を特定します。
     * 
     * @param goodInformation ↑のメソッドで得たString
     * @return name デバイス名、該当なしならnull
    */
    public String getNameByGoodInformation(String goodInformation) {
        int l = 0;
        String macAddr = null;
        for (String buf : getGoodInformations() ) {
            if (buf.equals(goodInformation)) {
                l = buf.indexOf(" ");
                macAddr = buf.substring(0,l);
                buf = getNameByMacAddr(macAddr);
            } else {
                buf = null;
            }
            if (buf != null) {
                return buf;
            }
        }
        return null;
    }
}
