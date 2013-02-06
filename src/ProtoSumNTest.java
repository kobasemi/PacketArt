//package org.jnetpcap.examples;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import org.jnetpcap.protocol.tcpip.Tcp;

public class ProtoSumNTest {
    public static void main(String[] args) {
        if(args.length < 1) {
                System.out.println("Usage: java ProtoSumN test.cap");
                System.exit(-1);
        }
        try {
        ProtoSumN psn = new ProtoSumN(args[0]);
        psn.openPcap();//pcapファイルのio処理
        psn.run();//loop開始
        System.out.println(psn.getErrbuf() );//jnetpcap内部エラー情報を表示
        } catch (Exception e) { // エラーは個別に対処するほうがいい
            e.printStackTrace();
            System.exit(-2);
        }
    }
}

class ProtoSumN {
    private StringBuilder myErrbuf;
    //StringBufferではなくStringBuilderなので。本格的にするならStringBuffer継承しまくる。
    private String myFile;
    private ProtoSumNPacketHandler myHandler;
    private Pcap myPcap;

    ProtoSumN(String filename) {
        myErrbuf = new StringBuilder();
        myHandler = new ProtoSumNPacketHandler();
        myFile = filename;
    }
    
    public StringBuilder getErrbuf() {
        return myErrbuf;
    }
    public void openPcap() {
        myPcap = Pcap.openOffline(myFile, myErrbuf);//pcapファイルを開く
        if (myPcap == null) {
            System.err.printf("Error while opening a file for capture: "
                + myErrbuf.toString());
            System.exit(-2);
        }
    }

    public void run() {
        try {
            myPcap.loop(1000, myHandler,"DummyUserData");
            //1000個パケット読んでmyHandlerに渡す
        } finally {
            //1000回読んだらこっちに引きこむ
            myPcap.close();//開けたら閉める
            //PacketArt.close();
        }
    }
}



class ProtoSumNPacketHandler implements PcapPacketHandler<String> {
    private final Tcp TCP_PACKET = new Tcp();  
    //ProtoSumNPacketHandler(){
        //System.sout.println("This Packet has been captured by " + user);
    //}
    //<String>とか、いろんなオブジェクトをパケットハンドラに持ってこれるらしい。
    //使わんがな。
    public void nextPacket(PcapPacket packet,String user) {
        //packet.scan();
        if (packet.hasHeader(TCP_PACKET) ) {  
            System.out.print("T");
        }else{
            System.out.print(".");
        }
    }
}

