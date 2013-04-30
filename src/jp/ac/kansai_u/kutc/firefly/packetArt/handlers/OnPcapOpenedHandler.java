package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

/**
 * PcapManagerに搭載するためのインターフェースです。<br>
 * 搭載完了した場合、onPcapOpened関数はPcapファイルが新規オープンされた時に呼ばれます。<br>
 * 具体的には、PcapManager.openDevとPcapManager.openFile関数が成功した直後です。
 *
 * @author sya-ke
*/
public interface OnPcapOpenedHandler{
    public void onPcapOpened();
}
