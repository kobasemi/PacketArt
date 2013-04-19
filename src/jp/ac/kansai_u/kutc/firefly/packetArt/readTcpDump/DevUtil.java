package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.util.List;
import java.util.ArrayList;
import java.lang.StringBuilder;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

/**
 * Pcap.openLive()で使うdevNameを人間の分かる方法で取得できる。<br>
 *
 * 使い方：<br>
 * <code>
 * DevUtil devUtil = new DevUtil();
 * String[] sa = devUtil.getGoodInformations();
 * for (String s : sa) {
 *     System.out.println(s);
 * }
 * </code>
 *
 * @author sya-ke
*/
public class DevUtil {

    private StringBuilder errBuf;//libpcapからのエラーをここに
    private List<PcapIf> allDevs;
    private ArrayList<DevInfo> allDevInfo;
    private boolean gotError;

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
     * テスト用のメソッドです。libpcapが対応するデバイスを表示します。
     *
     * @param args なんでも構いません
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
     * @return allDevInfo Listで全てのDevInfoオブジェクトを返します。
    */
    public List<DevInfo> getAllDevInfo() {
        return allDevInfo;
    }

    /**
     * findAllDevsでエラーが出たかどうかを判定します。<br>
     * 例えば、NICが一個もささっていない状態にtrueになります。<br>
     * エラーが出ている場合、get??By??系関数は使えません。
     *
     * @return gotError デバイス情報の取得にエラーが出たか否かが分かります。
    */
    public boolean hasError() {
        return gotError;
    }

    /**
     * hasErrorでエラーが出た時に呼ぶと便利な関数です。。
     *
     * @return errBuf libpcapからもらったエラーのStringBuilderです。
    */
    public StringBuilder getErrBuf() {
        return errBuf;
    }


    /**
     * デバイスのIPからデバイス名を取得します。
     *
     * @param ip デバイスの持つIPアドレスです。IPv6でもOKです。
     * @return devInfo.name IPにひも付けされたデバイス名です。該当無しならnull返ります。
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
     * デバイスのMACアドレスからデバイス名を取得します。
     *
     * @param macAddr デバイスの持つMACアドレスです。フォーマットは00:CB:CA:D0:32:5Aでお願いします。
     * @return devInfo.name MACアドレスにひも付けされたデバイス名です。該当無しならnullが返ります。
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
     * 人間の読めるデバイスの説明文からデバイス名を抽出します。
     *
     * @param description デバイスの説明文。ベンダ名などが書いてあります。
     * @return devInfo.name 説明文にひも付けされたデバイス名です。該当なしならnullです。
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
     * Linuxならほぼ確実に"lo" が返ってきます。Windowsならおそらくnullが返ってきます。
     *
     * @return devInfo.name ループバックデバイス名です。該当なしならnullです。
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
     * デバイスの選択に有益な情報を取得します。<br>
     * "MACアドレス デバイスの説明"のリストを返します。
     * 
     * @return information "MACアドレス デバイスの説明"のString配列を返します。
    */
    public String[] getGoodInformations() {
        String[] information = new String[ allDevs.size() ];
        int i=0;
        for (DevInfo devInfo : allDevInfo) {
            String buf = "";
            buf += devInfo.macAddr + " " + devInfo.description;
            if (devInfo.ipAddr != null) {
                buf += " " + devInfo.ipAddr;
            }
            information[i] = buf;
            i++;
        }
        return information;
    }

    /**
     * getGoodInformationsメソッドから得た情報を元に、デバイス名を特定します。
     * 
     * @param goodInformation getGoodInformations()で得たString
     * @return info デバイス名、該当なしならnull
     * @see getGoodInformations
    */
    public String getNameByGoodInformation(String goodInformation) {
        int l = 0;
        String macAddr = null;
        for (String info : getGoodInformations() ) {
            if (info.equals(goodInformation)) {
                l = info.indexOf(" ");
                macAddr = info.substring(0,l);
                info = getNameByMacAddr(macAddr);
            } else {
                info = null;
            }
            if (info != null) {
                return info;
            }
        }
        return null;
    }
}
