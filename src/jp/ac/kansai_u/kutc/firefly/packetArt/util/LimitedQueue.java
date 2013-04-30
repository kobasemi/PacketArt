package jp.ac.kansai_u.kutc.firefly.packetArt.util;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

//http://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java

/**
 * http://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java<br>
 * サイズ制限付きQueueです。<br>
 * limitを超えた場合、先頭データを削除します。
 *
 * @author sya-ke
*/
public class LimitedQueue<E> extends ConcurrentLinkedQueue<E> {

    private int limit;
    private Object limitLock = new Object();

    /**
     * 最大装填数を初期設定します。
     *
     * @param lim 最大装填数です。
    */
    public LimitedQueue(int lim) {
        setLimit(lim);
    }
//ちょこっとかえるだけでlong limも行けそうだなあ。。
//でもパケットをlong個も保持したらメモリが吹き飛ぶなあ・・

    /**
     * 動的にキューの長さを変更できます。
     *
     * @param lim 
     * @return 向こうなキューの長さが与えられた場合はfalseが返ります。
    */
    public boolean setLimit(int lim) {
        if (lim <= 0) {
            lim = 1;
            return false;
        }
        synchronized(limitLock) {
            limit = lim;
        }
        return true;
    }

    /**
     * LinkedListの関数をオーバーライドしたものです。
     *
     * @return 基本的にtrueですが、nullが渡された場合はnullを要素に追加した後falseを返します。
    */
    @Override
    public synchronized boolean add(E o) {
        if (o == null) {
            return false;
        }
        super.add(o);
        synchronized(limitLock) {
            while (size() > limit) {
                super.remove();
            }
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
