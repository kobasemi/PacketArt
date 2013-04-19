package jp.ac.kansai_u.kutc.firefly.packetArt.test;

import jp.ac.kansai_u.kutc.firefly.packetArt.music.*;
import jp.ac.kansai_u.kutc.firefly.packetArt.handlers.*;

import jp.ac.kansai_u.kutc.firefly.packetArt.readTcpDump.*;
import org.jnetpcap.protocol.network.*;
import org.jnetpcap.protocol.tcpip.*;

//BGMのメロディを作成するアルゴリズム
public class MelodyAlgorithm implements Ip4Handler,TcpHandler{

    public PcapManager pm;
    public DevUtil devUtil;
    public String devName;
    public int[] ip4List;
    public int counter;

    public MelodyAlgorithm() {
        super();
        pm = PcapManager.getInstance();
        devUtil = new DevUtil();
        devName = null;
        ip4List = new int[1500];
        counter = 0;
    }

    public static void main(String[] args) {
        MelodyAlgorithm myMelody = new MelodyAlgorithm();
        myMelody.pm.addHandler(myMelody);
        (new Thread(myMelody.pm)).start();

        System.out.println("###PcapManager is running in background...");
/*
        myMelody.devName = null;
        int i = 0;
        for (String info : myMelody.devUtil.getGoodInformations() ) {
            System.out.println( "Press 1 if you want to capture devices!");
            System.out.println( info );
            System.out.flush();             // 強制出力
            try {
                i = System.in.read();
                System.in.read();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 0x31) {
                myMelody.devName = myMelody.devUtil.getNameByGoodInformation(info);
            }
        }

        if (myMelody.devName != null) {
            boolean wasOK = myMelody.pm.openDev(myMelody.devName);
*/
        if (true) {
            boolean wasOK = myMelody.pm.openFile("1000.cap");
            if (wasOK) {
                System.out.println("###PcapManager has opened file (1000.cap)");
            } else {
                myMelody.pm.close();
                myMelody.pm = null;
                System.out.println("###PcapManager killed");
                return;
            }
            while( myMelody.counter < 1500){
//                System.out.println(myMelody.counter + " integer(s) captured...");
            }
            for (int i: myMelody.ip4List) {
                System.out.println(i);
            }
            melodyAlgorithm( myMelody.ip4List);
        }
        myMelody.pm.close();
        myMelody.pm = null;
        System.out.println("###PcapManager killed");
        return;
    }

    public static int[] bytes2ints(byte[] b) {
        int[] a = new int[b.length];
        for (int i=0; i<b.length; i++) {
            a[i] = b[i] & 0xff;
        }
        return a;
    }

    private void addData(byte[] b) {
        int[] buf = bytes2ints(b);
        ip4List[counter] = buf[0];
        ip4List[counter+1] = buf[1];
        ip4List[counter+2] = buf[2];
        ip4List[counter+3] = buf[3];
        counter = counter + 4;
    }


    //今回はまとめたパケットが欲しいということで..
    public void handleIp4(Ip4 ip4) {
        try {
            addData(ip4.destination());
            addData(ip4.source());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Called Ip4! counter =>" + counter);
    }

    public void handleTcp(Tcp tcp) {
        System.out.println("Called Tcp! counter =>" + counter);
    }


    static public int[] melodyAlgorithm(int[] a){
        int[] desposedip = a;
        
        //コード情報が入ったString配列を持ってくる．
        String[] code = CodeMaker.codeMaker();
        
        //Disposeされたパケットが入っている配列を持ってくる．
        
        //音程情報が入る配列．
        int[] melody = new int[24];
        
        //使用する鍵盤配列を持ってくる．
        int[] melodyscale = ScaleMaker.setMelodyScale();
        
        //各コードに対応する音程．(出現比率)
        //Em : E G B (4:3:3)
        //Am : A C E (4:3:3)
        //B7 : F# A B D# (3:3:3:1)
        //D#に関してはScaleMakerに無いのでここで数値を弄って作る．        
        int[] emmel = {melodyscale[0], melodyscale[2], melodyscale[4]};
        int[] ammel = {melodyscale[3], melodyscale[5], melodyscale[7]};
        int[] b7mel = {melodyscale[1], melodyscale[3], melodyscale[4]}; //melodyscale[6] + 1
        
        //メロディの設定．
        for (int i = 0; i < 24; i++) {    
            int judge = 0;
            if(desposedip[i] < 4){
                judge = 0;
            }else if(desposedip[i] < 7){
                judge = 1;
            }else if(desposedip[i] < 10){
                judge = 2;
            }
            
            if("Em".equals(code[i])){
                melody[i] = emmel[judge];
            }else if("Am".equals(code[i])){
                melody[i] = ammel[judge];
            }else if("B7".equals(code[i])){
                melody[i] = b7mel[judge];
            }
        }
        return melody;
    }
}
