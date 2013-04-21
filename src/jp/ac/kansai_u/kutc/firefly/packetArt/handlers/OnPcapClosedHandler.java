package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

/**
 * PcapManagerに搭載するためのインターフェースです。<br>
 * 搭載完了した場合、onPcapClosed関数はPcapファイルが無くなった時に呼ばれます。<br>
 * @author sya-ke
*/
public interface OnPcapClosedHandler{
    public void onPcapClosed();
}
