package jp.ac.kansai_u.kutc.firefly.packetArt.util;

import org.jnetpcap.packet.JHeader;
import org.jnetpcap.packet.JPacket;

import java.lang.StringBuilder;
import java.net.InetAddress;

/**
 *
 * @author sya-ke
*/
public class PacketUtil {

    /**
     * 「実際にキャプチャした時点での」パケットのサイズを返します。<br>
     * 実際にキャプチャした時間は、tcpdumpで保存したファイルに<br>
     * パケットのヘッダとして格納されています。<br>
     * リアルタイム受信した場合、その時の時間が格納されています。
     *
     * @param jHeader TcpやUdpなどのオブジェクト
     * @return パケットのサイズ(Byte)
    */
    public static long getCaplen(JHeader jHeader) {
        return jHeader.getPacket().getCaptureHeader().caplen();
    }

    /**
     * 「実際にキャプチャした時点での」1970年からの秒数を返します。
     *
     * @param jHeader TcpやUdpなどのオブジェクト
     * @return パケットの到着時刻(sec)
    */
    public static long getTimeStamp(JHeader jHeader) {
        return jHeader.getPacket().getCaptureHeader().seconds();
    }

    /**
     *  「実際にキャプチャした時点での」1970年からの秒数をミリ秒で返します。
     *
     * @param jHeader TcpやUdpなどのオブジェクト
     * @return パケットの到着時刻(milli sec)
    */
    public static long getMilliTimeStamp(JHeader jHeader) {
        return jHeader.getPacket().getCaptureHeader().timestampInMillis();
    }

    /**
     *  「実際にキャプチャした時点での」1970年からの秒数をナノ秒で返します。パケット到着間隔がほぼ同じ場合<br>
     * ミリ単位で区別することができるかもしれません。
     *
     * @param jHeader TcpやUdpなどのオブジェクト
     * @return パケットの到着時刻(nano sec)
    */
    public static long getNanos(JHeader jHeader) {
        return jHeader.getPacket().getCaptureHeader().nanos();
    }


    /**
     * 「実際にキャプチャした時点での」パケットのサイズを返します。<br>
     *
     * @param jPacket PcapPacketオブジェクト
     * @return パケットのサイズ(Byte)
    */
    public static long getCaplen(JPacket jPacket) {
        return jPacket.getCaptureHeader().caplen();
    }

    /**
     *  「実際にキャプチャした時点での」1970年からの秒数を返します。
     *
     * @param jPacket PcapPacketオブジェクト
     * @return パケットの到着時刻(sec)
    */
    public static long getTimeStamp(JPacket jPacket) {
        return jPacket.getCaptureHeader().seconds();
    }

    /**
     *  「実際にキャプチャした時点での」1970年からの秒数をミリ秒で返します。
     *
     * @param jPacket PcapPacketオブジェクト
     * @return パケットの到着時刻(milli sec)
    */
    public static long getMilliTimeStamp(JPacket jPacket) {
        return jPacket.getCaptureHeader().timestampInMillis();
    }

    /**
     *  「実際にキャプチャした時点での」1970年からの秒数をナノ秒で返します。パケット到着間隔がほぼ同じ場合<br>
     * ミリ単位で区別することができるかもしれません。
     *
     * @param jPacket PcapPacketオブジェクト
     * @return パケットの到着時刻(nano sec)
    */
    public static long getNanos(JPacket jPacket) {
        return jPacket.getCaptureHeader().nanos();
    }

    /**
     * バイト列のIPアドレスをStringにして、<br>
     * ひとつにまとめる関数です。
     * 
     * @param raw IP(v4,v6)アドレスをバイト列で表現したものです。
     * @return バイト列のIPアドレスを文字列にしたものが返ります。 例："192.168.0.1" "::1"。エラーならnullが返ります。
    */
    public static String getInetAddress(byte[] raw) {
        try {
            return InetAddress.getByAddress(raw).getHostAddress();
        } catch (Exception e){
            return null;
            //正当なアドレスでない
        }
    }

    /**
     * http://rgagnon.com/javadetails/java-0596.html を<br>
     * 「:」区切りのMACアドレス用に魔改造した関数です。
     *
     * @param raw MACアドレスをバイト配列で表現したものです。
     * @return バイト列のMACアドレスを文字列にしたものが返ります。例："AA:BB:CC:DD:EE:FF"。エラーならnullが返ります。
    */
    public static String getMacAddress( byte [] raw ) {
        final String HEXES = "0123456789ABCDEF";
        if ( raw == null || raw.length != 6) {
            return null;
            //MACアドレスでない
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
     * バイト列をint列にします。
     *
     * @param b バイト列
     * @return int列
    */
    public static int[] bytes2ints(byte[] b) {
        int[] a = new int[b.length];
        for (int i=0; i<b.length; i++) {
            a[i] = b[i] & 0xff;
        }
        return a;
    }

}
