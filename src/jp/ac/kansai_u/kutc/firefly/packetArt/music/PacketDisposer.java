package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.util.LinkedList;

import jp.ac.kansai_u.kutc.firefly.packetArt.ProtocolHandlerBase;

import org.jnetpcap.protocol.network.Ip4;

/**
 * PacketDisposerクラス <br>
 * このクラスはパケットを受信し、それを処理しやすい形に変換するためのクラスです。<br>
 * このクラスはSingletonとして動作します。そのため、このクラスのインスタンスを生成することはできません。<br>
 * このクラスのインスタンスを取得するためには、以下のように記述します。
 * <code>
 * PacketDisposer instance = PacketDisposer.getInstance();
 * </code>
 * 
 * @author midolin
 *
 */
public class PacketDisposer extends ProtocolHandlerBase{
    private static final PacketDisposer instance = new PacketDisposer();
    private boolean fully;
    private final int MAX_PACKETS = 3000;
    private int[] data = new int[MAX_PACKETS * 4];//IPv4は4オクテット
    private int counter;

//    LinkedList<byte[]> destQueue; //3000パケット一括にする。
    //LinkedList<byte[]> srcQueue;

    PacketDisposer() {
        fully = false;
        counter = 0;
        //destQueue = new LinkedList<byte[]>();
    }

    public static PacketDisposer getInstance(){
        return instance;
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
        data[counter] = buf[0];
        data[counter+1] = buf[1];
        data[counter+2] = buf[2];
        data[counter+3] = buf[3];
    }
/*        
        //何とかしてIPv4アドレスをString[]で持ってくる．
        String[] rawip = foo();
        String ipsentence = "";
        
        //splitを使って"."を処理後配列に入れたいので2度手間っぽいが一度String型にする．
        for(int i = 0; i < rawip.length; i++){
            ipsentence = ipsentence + rawip[i] + ".";
        }
        
        //オクテットごとにString配列に格納．
        String[] stringip = ipsentence.split("\\.");
        
        
        int[] ipdata = new int[stringip.length]; //String→intの作業を行うために一時的に使用する配列．
        int[] deposedip = new int[stringip.length]; //こいつの中身の値が後のメロディ生成に影響する．
        
        //IPのオクテットの数字をとにかく1桁の数字に変換する．
        for(int j = 0; j < stringip.length; j++){
            ipdata[j] = Integer.parseInt(stringip[j]);
            if(ipdata[j] < 9){
                deposedip[j] = ipdata[j];
            }else if(ipdata[j] < 100){
                deposedip[j] = Math.round(ipdata[j]/10);
            }else if(ipdata[j] < 200){
                deposedip[j] = Math.round((ipdata[j]-100)/10);
            }else if(ipdata[j] < 256){
                deposedip[j] = Math.round((ipdata[j]-200)/10);
            }
        }
        return deposedip;
    }
*/
    
    @Override
    public void ip4Handler(Ip4 pkt){
        counter++;
       /* destQueue.push(pkt.destination());*/
        if (counter < MAX_PACKETS) {
            addData(pkt.destination());
        } else {
            fully = true;
            //お腹いっぱい
        }
        //srcQueue.push(pkt.source());
    }

    public boolean isFull() {
        return fully;
    }

	public static int[] disposePacket() {
		// TODO Auto-generated method stub
		return null;
	}
}
