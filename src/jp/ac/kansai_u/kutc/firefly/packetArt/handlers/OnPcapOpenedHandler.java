package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

/**
 * PcapManagerに搭載するためのインターフェースです。<br>
 * 搭載完了した場合、onPcapOpened関数はPcapファイルが新規オープンされた時に呼ばれます。<br>
 * @author sya-ke
*/
public interface OnPcapOpenedHandler{
    public void onPcapOpened();
}
