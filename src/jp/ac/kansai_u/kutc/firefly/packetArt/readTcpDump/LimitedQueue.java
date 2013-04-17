package jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
//http://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java

/**
 * サイズ制限付きQueueです。limitを超えた場合、先頭データを削除します。
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
     * @return limit このリストの最大装填数を返します。
    */
    public int getMaxSize() {
        return limit;
    }

    /**
     * pollをhowMany回やる関数です。途中で弾切れになった場合はそこまでのListを返します。
     * 
     * @param howMany howMany個のオブジェクトをこのキューから取り出します。
     * @return listBuf howMany個のオブジェクトが入った配列です。
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
