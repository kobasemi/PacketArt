package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapSockAddr;

import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import java.lang.StringBuilder;
import java.io.IOException;

/**
 * このクラスはPcapIfのバイナリな情報を
 * 文字列に変え、保持するクラスです。<br>
 * 基本的にただの構造体です。<br>
 * 必要な情報しか保持しません。
 *
 * @author sya-ke
*/
public class DevInfo {

    public PcapIf device;
    //いわゆる、主キー。Map使わんでも、NICを100個つけてる人とかいないしね。
    //jnetpcapが拾ってきた時点でnull回避する。重複は絶対にない。

    public String name;//名前。""はほぼありえない。
    public String description;//デバイスの説明。（チップセットとか）。""アリ
    public String macAddr;//OSが勝手につけたMACアドレス。"" == OSが取得を許さなかった
    public String ipAddr;//デバイスの持つIPアドレス。当然""アリ。
    public ArrayList<String> ip6Addr;//IPv4とIPv6の両方が割り当てられている場合が多い。
    public boolean loopback;//ループバックかどうか。なんの役に立つかは不明

    /**
     * このクラスのコンストラクタは<br>
     * この一つのみです。
     *
     * @see PcapIf
     * @param pcapIf バイト、数字、booleanな情報を文字列な情報に変換したいデバイスのクラスです。
    */
    DevInfo(PcapIf pcapIf) {
        ip6Addr = new ArrayList<String>();
        if (pcapIf != null) {
            this.device = pcapIf;
            complete();
        } else {
            decomplete();
        }
    }

    /**
     * 必要な情報を取得し、文字列でデータ化します。<br>
     * この関数はコンストラクタで呼ばれます。
    */
    private void complete() {
        name = device.getName();
        description = device.getDescription();
        try {
            macAddr = getHex(device.getHardwareAddress());
        } catch (IOException e) {
            //OSがMACアドレスのクエリを遮断した。なぜ！？
            System.out.println( "Retributing MAC ADDRESS '" + device.toString()
                                + "'Denied by OS!");
            e.printStackTrace();
            macAddr = "";
        }
        List<PcapAddr> addresses = device.getAddresses();
        for (PcapAddr pcapAddr : addresses) {
            PcapSockAddr pcapSockAddr = pcapAddr.getAddr();
            if (pcapSockAddr.getFamily() == PcapSockAddr.AF_INET) {
                ipAddr = getInetAddress(pcapSockAddr.getData());
            } else {
                ip6Addr.add( getInetAddress(pcapSockAddr.getData() ));
            }
        }
        if (ipAddr != null && ipAddr.equals("127.0.0.1") || ip6Addr.contains("::1")) {
            loopback = true;
        }
    }

    /**
     * 初期化します。
    */
    private void decomplete() {
        name = "";
        description = "";
        macAddr = "";
        ipAddr = "";
        ip6Addr = null;
        loopback = false;
    }

    /**
     * バイト列のIPアドレスをStringにして、<br>
     * ひとるにまとめる関数です。
     * 
     * @param raw IP(v4,v6)アドレスをバイト列で表現したものです。
     * @return バイト列のIPアドレスを文字列にしたものが返ります。エラーならnullが返ります。
    */
    public static String getInetAddress(byte[] raw) {
        String address = "";
        try {
            address = InetAddress.getByAddress(raw).getHostAddress();
        } catch (Exception e){
            return null;
            //今回は正当なアドレスなので、ここには絶対にきません。
        }
        return address;
    }

    /**
     * http://rgagnon.com/javadetails/java-0596.html を<br>
     * MACアドレス用に魔改造した関数です。
     *
     * @param raw MACアドレスをバイト配列で表現したものです。
     * @return StringなMACアドレスが返ります。例："AA:BB:CC:DD:EE:FF"。エラーならnullが返ります。
    */
    public static String getHex( byte [] raw ) {
        final String HEXES = "0123456789ABCDEF";
        if ( raw == null || raw.length != 6) {//isMacAddress
            return null;
        }
        final StringBuilder hex = new StringBuilder( 3 * 6);
        // 01-23-45-67-89-AB で、ちょうど17個のASCII.
        for ( final byte b : raw ) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4));
            hex.append(HEXES.charAt((b & 0x0F)));
            hex.append(":");
        }
        //hex.lastIndexOf(":")
        hex.deleteCharAt(hex.length() - 1);
        return hex.toString();
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
