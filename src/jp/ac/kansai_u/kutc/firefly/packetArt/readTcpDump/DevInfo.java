package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapSockAddr;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketUtil;

/**
 * このクラスはPcapIfのバイナリな情報を
 * 文字列に変え、保持するクラスです。<br>
 * 基本的にただの構造体です。<br>
 * 必要な情報しか保持しません。
 *
 * @author sya-ke
*/
public class DevInfo {

    public final PcapIf device;
    //いわゆる、主キー。Map使わんでも、NICを100個つけてる人とかいないしね。
    //jnetpcapが拾ってきた時点でnull回避する。重複は絶対にない。

    public final String name;//名前。""はほぼありえない。
    public final String description;//デバイスの説明。（チップセットとか）。""アリ
    public String macAddr;//OSが勝手につけたMACアドレス。"" == OSが取得を許さなかった
    public String ipAddr;//デバイスの持つIPアドレス。当然""アリ。
    public final ArrayList<String> ip6Addr;//IPv4とIPv6の両方が割り当てられている場合が多い。
    public boolean loopback;//ループバックかどうか。なんの役に立つかは不明

    /**
     * このクラスのコンストラクタは<br>
     * この一つのみです。
     *
     * @see PcapIf
     * @param pcapIf バイト、数字、booleanな情報を文字列な情報に変換したいデバイスのクラスです。
    */
    DevInfo(PcapIf pcapIf) {
        this.device = pcapIf;
        if (this.device != null) {
            this.ip6Addr = new ArrayList<String>();
            this.name = device.getName();
            this.description = device.getDescription();
            try {
                this.macAddr = PacketUtil.getMacAddress(device.getHardwareAddress());
            } catch (IOException e) {
                //OSがMACアドレスのクエリを遮断した。なぜ！？
                System.out.println( "Retributing MAC ADDRESS '" + device.toString()
                                    + "'Denied by OS!");
                e.printStackTrace();
                this.macAddr = "";
            }
            final List<PcapAddr> addresses = device.getAddresses();
            for (final PcapAddr pcapAddr : addresses) {
                final PcapSockAddr pcapSockAddr = pcapAddr.getAddr();
                if (pcapSockAddr.getFamily() == PcapSockAddr.AF_INET) {
                    this.ipAddr = PacketUtil.getInetAddress(pcapSockAddr.getData());
                } else {
                    this.ip6Addr.add( PacketUtil.getInetAddress(pcapSockAddr.getData() ));
                }
            }
            if (ipAddr != null && ipAddr.equals("127.0.0.1") || ip6Addr.contains("::1")) {
                loopback = true;
            } else {
                loopback = false;
            }
        } else {
            this.name = "";
            this.description = "";
            this.macAddr = "";
            this.ipAddr = "";
            this.ip6Addr = null;
            this.loopback = false;
        }
    }

    /**
     * libpcapがキャプチャ可能なデバイスについて、<br>
     * 情報をすべて返します。デバッグに使えます。
     *
     * @return 全ての情報をひとつのString型で返します。改行コードは"\n"です。
    */
    public String getStats() {
        String ret = "";
        String ln = "\n";
        ret += "NAME: " + name + " = " + description + ln;
        ret += "MAC: " + macAddr + ln;
        ret += "IPv4: " + ipAddr + ln;
        for (String ip6 : ip6Addr) {
            ret += "IPv6: " + ip6 + ln;
        }
        ret += "LOOPBACK: ";
        if (loopback) {
            ret+="true";
        } else {
            ret+="false";
        }
        ret += ln;
        return ret;
    }
}
