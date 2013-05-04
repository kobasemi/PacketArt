package jp.ac.kansai_u.kutc.firefly.packetArt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.LimitedRing;


/**
 * 効果音を調整するクラスです。
 * 
 * @author Nakata
*/    
public class PlaySE extends HashMap<String,LimitedRing<Clip>> implements LineListener{
    //TODO SoundeEffect wavを決定すること
    public final String MOVE_FILE = "resource/se/move.wav";//ゲーム：ミノ左右下移動
    public static final byte MOVE = 0;
    public final String HARDDROP_FILE = "resource/se/harddrop.wav";//ゲーム：ミノハードドロップ
    public static final byte HARDDROP = 1;
    public final String TURN_FILE = "resource/se/turn.wav";//ゲーム：ミノ回転
    public static final byte TURN = 2;
    public final String DEMISE_FILE = "resource/se/demise.wav";//ゲーム：ミノ消滅
    public static final byte DEMISE = 3;
    public final String SELECT_FILE = "resource/se/select.wav";//メニュー：セレクト用効果音
    public static final byte SELECT = 4;
    public final String CANCEL_FILE = "resource/se/cancel.wav";//メニュー：キャンセル音
    public static final byte CANCEL = 5;
    public final String OPEN_FILE = "resource/se/open.wav";//アプリケーション起動音（仮)
    public static final byte OPEN = 6;

    public static final int RING_SIZE= 5;//５個の同時重複再生を許す

    public PlaySE() {
        super();
        //openFile(MOVE_FILE);
        openFile(HARDDROP_FILE);
        openFile(TURN_FILE);
        //openFile(DEMISE_FILE);
        openFile(SELECT_FILE);
        openFile(CANCEL_FILE);
        //openFile(OPEN_FILE);
    }

    /**
     * サウンドファイルからLINEを呼び出すための関数です。
     * 返ってきたオブジェクトはnullチェックしといてね
     *
     * @param fileName サウンドファイル名です。 
    */

    private void openFile(String fileName) {
        try {
            LimitedRing<Clip> clips = new LimitedRing<Clip>(RING_SIZE);
            File file = new File(fileName);
            for (int i=0;i<RING_SIZE;i++) {
                AudioInputStream inputStream = 
                        AudioSystem.getAudioInputStream(file);
                AudioFormat format = inputStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(inputStream);
                clip.addLineListener(this);
                inputStream.close();
                clips.add(clip);
            }
            put(fileName, clips);
        } catch (UnsupportedAudioFileException e) {
            //mp4とか、無理なファイルはこっち。
            e.printStackTrace();
        } catch (IOException e) {
            //そもそもファイルが存在しないとかいう場合はこっち
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            //PCが、音楽を再生できないPCでした。
            e.printStackTrace();
        }
    }

    public void play(byte fileID) {
        switch(fileID) {
            //case MOVE: play(MOVE_FILE);break;
            case HARDDROP: play(HARDDROP_FILE);break;
            case TURN: play(TURN_FILE);break;
            //case DEMISE: play(DEMISE_FILE);break;
            case SELECT: play(SELECT_FILE);break;
            case CANCEL: play(CANCEL_FILE);break;
            //case OPEN: play(OPEN_FILE);break;
            default : System.out.println("NO SUCH FILE ID");break;
        }
    }

    public void play(String fileName) {
        get(fileName).peek().start();
    }

    //http://aidiary.hatenablog.com/entry/20061105/1275137770
    public void update(LineEvent event) {
        // ストップか最後まで再生された場合
        if (event.getType() == LineEvent.Type.STOP) {
            Clip clip = (Clip) event.getSource();
            clip.stop();
            clip.setFramePosition(0); // 再生位置を最初に戻す
        }
    }

    /**
     * 名前のとおりです。
     * 
     * @param clip くりっぷ
     * @param pos 正か負のfloat。ボリュームの増分(マイナスなら減分)を表す
    */
    public void dialVolume(Clip clip, float pos) {
        FloatControl gainer = 
                (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainer.setValue(pos);
    }

    public static void main(String[] args) {
        PlaySE ps = new PlaySE();
        ps.play(PlaySE.SELECT);
        ps.play(PlaySE.CANCEL);
        ps.play(PlaySE.TURN);
        ps.play(PlaySE.HARDDROP);
        try {
            Thread.sleep(100);
        } catch(Exception e){
        }
        ps.play(PlaySE.SELECT);
        try {
            Thread.sleep(200);
        } catch(Exception e){
        }
        ps.play(PlaySE.SELECT);
        try {
            Thread.sleep(300);
        } catch(Exception e){
        }
        ps.play(PlaySE.SELECT);
    }
}
