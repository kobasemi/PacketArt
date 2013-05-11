package jp.ac.kansai_u.kutc.firefly.packetArt.util;

/**
 参照渡しできないバイト型を参照渡しするのに使えます。
*/
public class PrimitiveHolder<T> {
    public T value;
    public PrimitiveHolder(T t){
        this.value = t;
    }
}
