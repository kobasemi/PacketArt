package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;

import java.awt.Color;
import java.awt.Point;

import org.jnetpcap.packet.PcapPacket;
//import org.jnetpcap.protocol.lan.Ethernet;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.DrawUtil;
import jp.ac.kansai_u.kutc.firefly.packetArt.util.PacketHolder;

public class PacketBar extends PacketHolder {

    public Point endPoint;//PacketBarの終点が代入される
    public PacketBar prev;//始点を求めるのに必要
    public PacketBar next;//一つ次のPacketBarの描画処理に必要
    public int dir;//方角
    public int len;//方角
    public Color color;//色。アート。

    /**
     * コンストラクタです。引数はnullチェックしといてね。
     *
     * @param prevBar 一つ前のPacketBar。連結します。
     * @param packet 棒データの核となるPcapPacketです。
    */
    public PacketBar(PacketBar prevBar, PcapPacket pkt) {
        this(prevBar);
        eatPacket(pkt);
        endPoint = DrawUtil.polarPointing(prev.endPoint.getLocation(), dir, len);
    }

    /**
     * コンストラクタです。引数はnullチェックしといてね。
     *
     * @param prevBar 一つ前のPacketBar。連結します。
    */
    public PacketBar(PacketBar prevBar) {
        super();
        prev = prevBar;//一つ前の棒(!=null)
        prev.next = this;//連結
        endPoint = null;//終点座標
        next = null;//１つ後の棒(null=>最終の棒)
        dir = prev.dir;//一つ前の棒の向きを継承
        color = Color.BLACK;//色
        len = 5;
    }

    
    //ダミー生成用PacketBarコンストラクタです.アクセスするには<br>
    //PacketBar.getStartPacketBar(Point startPoint)を呼び出します。
    private PacketBar(Point startPoint) {
        super();
        endPoint = startPoint;
        prev = null;
        next = null;
        dir = 0;
        color = Color.BLACK;
        len = 5;
    }

    /**
     * まず、パケットのサイズを棒の長さにします。
     * 次に、Ethernetの送信元アドレス先頭4つから棒の色を決定します。<br>
     * (Ethernetレイヤが無い場合は強制的に黒色に決定します。)<br>
     * 次に、TCPレイヤがあるならば棒は右方向に向きを(送信元ポート{度})回転します。<br>
     * 次に、UDPレイヤがあるならば棒は左方向に向きを(送信元ポート{度})回転します。<br>
     * (TCP/UDPレイヤが無い場合は回転を行いません。)
     *
     * @param pkt データ元となるPcapPacketです。
     * @return 棒の生成成功ならtrue,失敗ならfalseを返します。
    */
    private void eatPacket(PcapPacket pkt) {
        if (pkt == null) {
            return;
        }
        len = (new Long(PacketUtil.getCaplen(pkt))).intValue();
        //lenの長さは実際にICT運営がどうやってパケットを流すかに激しく依存する。
        try {
            setPacket(pkt);//プロトコル別に分け、getTcp等を使用可能にします（親クラスを参照）
        } catch (Exception e) {
            //jnetpcapのバグは握り潰します。
        }
        Ip4 ip4 = getIp4();
        if (ip4 != null) {
            int[] src = PacketUtil.bytes2ints(ip4.source());//ココがパケットアート
            //MacAddressは6バイト。6個のintに変換！

            //Color(int r, int g, int b, int a)
            //範囲 (0 - 255) の指定された赤、緑、青、およびアルファ値を使って sRGB カラーを生成します。
            //バイト列も8bit区切りなので、0-255のColorの範囲に収まる
            color = new Color(src[0], src[1], src[2], 0xFF);
            //棒の色の決定
        }    
        Tcp tcp = getTcp();
        Udp udp = getUdp();
        //TCPとUDPが同程度ならば、いい感じになるはずだが・・・
        if (tcp != null) {
            dir = DrawUtil.relativeRad(dir, tcp.source()%90);//右を向きます
            //dir = DrawUtil.relativeRad(dir, tcp.source()%180);//右を向きます
        } else if (udp != null) {
            dir = DrawUtil.relativeRad(dir, udp.source()%-90);//左を向きます
            //dir = DrawUtil.relativeRad(dir, udp.source()%-180);//左を向きます
        }
    }

    /**
     * 最初の始点のダミーPacketBarを生み出すための関数です。
     *
     * @param startPoint スタート地点の座標です
     * @return スタート地点となるダミーPacketBarを返します。
    */
    public static PacketBar getStartPacketBar(Point startPoint) {
        return new PacketBar(startPoint);
    }
}
