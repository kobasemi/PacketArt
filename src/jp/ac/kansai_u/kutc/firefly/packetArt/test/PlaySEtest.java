package jp.ac.kansai_u.kutc.firefly.packetArt.test;

import jp.ac.kansai_u.kutc.firefly.packetArt.PlaySE;

public class PlaySEtest{
    public static void main(String[] args){
        PlaySE playSE = PlaySE.getInstance();
        playSE.initialize();
        System.out.println("Default Volume => " + playSE.getVolume("select"));
        System.out.println("inited!");
        System.out.println("PlaySelect => " + playSE.play("select"));
        System.out.println("PlaySelect => " + playSE.play("select"));
        System.out.println("PlaySelect => " + playSE.play("select"));
        try {
            Thread.sleep(1000);
        } catch (Exception e){
        }
        System.out.println("SetVolume => 100");
        playSE.setVolumeAll(100.0);
        System.out.println("PlaySelect => " + playSE.play("select"));
        System.out.println("PlaySelect => " + playSE.play("select"));
        System.out.println("PlaySelect => " + playSE.play("select"));
        try {
            Thread.sleep(1000);
        } catch (Exception e){
        }
        System.out.println("SetVolume => 50");
        playSE.setVolumeAll(50.0);
        System.out.println("PlaySelect => " + playSE.play("select"));
        System.out.println("PlaySelect => " + playSE.play("select"));
        System.out.println("PlaySelect => " + playSE.play("select"));
    }
}
