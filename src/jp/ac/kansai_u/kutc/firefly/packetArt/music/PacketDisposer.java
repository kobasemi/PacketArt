package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.PcapManager;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.Ip4Handler;
import org.jnetpcap.protocol.network.Ip4;

/**
 * PacketDisposerクラス <br>
 * このクラスはパケットを受信し、それを処理しやすい形に変換するためのクラスです。<br>
 * 
 * @author midolin
 */
public final class PacketDisposer implements Ip4Handler{
    private static PacketDisposer instance = new PacketDisposer(24);
    public static synchronized PacketDisposer getInstance() {
        if (instance == null) {
            instance = new PacketDisposer(24);
        }
        return instance;
    }
    private static int max;//スレッドは急には止まれないかも
    private static int[] data;
    private static int counter;
    private static PcapManager pm = PcapManager.getInstance();

    private PacketDisposer(int maxInt) {
        data = new int[maxInt];
        max = maxInt;
        counter = 0;
        System.out.println("PacketDisposer.handler atttached");
    }

    public int[] bytes2ints(byte[] b) {
        int[] a = new int[b.length];
        for (int i=0; i<b.length; i++) {
            a[i] = b[i] & 0xff;
        }
        return a;
    }

    public void addData(byte[] b) {
        int[] buf = bytes2ints(b);
        int i;
        for (i=0;i<buf.length && counter + i < max;i++) {
            System.out.println("Data"+"["+ (counter + i) +"] = " + buf[i] % 10);
            data[counter + i] = buf[i] % 10;
        }
        counter += i;
    }

    //PcapManagerから呼ばれる
    public void handleIp4(Ip4 ip4) {
        System.out.print("IPv4 COMES!");
        if (counter >= max) {
            System.out.println("  BUT COUNTER FULL!");
        } else {
            System.out.println("");
        }
        synchronized(ip4){
        addData(ip4.destination());
        addData(ip4.source());
        }
    }

    //MelodyAlgorithmから呼ばれる
    public static int[] disposePacket() {
        pm.addHandler(instance);
        while(counter < max){
        }
        System.out.println("PacketDisposer.handler detach!");
        if ( pm.removeHandler(instance) == false) {
            //これが呼ばれた時点でこのhandleIp4関数はPcapManagerに呼ばれない
            System.out.println("PacketDisposer.handler detach FAIL!!!!");
        }
        //pm = null;
        System.out.println("PacketDisposer: returning Data...");
        return data;
    }
}
