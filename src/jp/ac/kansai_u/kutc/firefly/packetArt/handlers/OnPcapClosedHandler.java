package jp.ac.kansai_u.kutc.firefly.packetArt.handlers;

/**
 * PcapManagerに搭載するためのインターフェースです。<br>
 * 搭載完了した場合、onPcapClosed関数はPcapファイルが無くなった時に呼ばれます。<br>
 * 但し、closeが間に合った場合は呼ばれないので、OnNoPacketsLeftHandlerを使ってください。
 *
 * @author sya-ke
*/
public interface OnPcapClosedHandler{
    public void onPcapClosed();
}
