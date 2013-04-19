package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
//http://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java

/**
 * http://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java<br>
 * サイズ制限付きQueueです。<br>
 * limitを超えた場合、先頭データを削除します。
 *
 * @author sya-ke
*/
public class LimitedQueue<E> extends LinkedList<E> {

    private int limit;

    public LimitedQueue(int limit) {
        if (limit <= 0) {
            limit = 1;
        }
        this.limit = limit;
    }

    @Override
    public boolean add(E o) {
        super.add(o);
        while (size() > limit) {
            super.remove();
        }
        return true;
    }

    /**
     * そのまんま名前の通りです。
     *
     * @return このリストの最大装填数を返します。
    */
    public int getMaxSize() {
        return limit;
    }

    /**
     * pollを複数回やる関数です。途中で<br>
     * 弾切れになった場合はそこまでのListを返します。
     * 
     * @param howMany この個数だけオブジェクトをこのキューから取り出します。
     * @return 個数分のオブジェクトが入った配列です。空の場合も有り得ます。
    */
    public List<E> poll(int howMany) {
        List<E> listBuf = new ArrayList<E>(howMany);
        E o;
        for (int i=0;i<howMany && (o = poll()) != null;i++) {
            listBuf.add(o);
        }
        return listBuf;
    }
}
