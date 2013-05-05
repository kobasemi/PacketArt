package jp.ac.kansai_u.kutc.firefly.packetArt;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import jp.ac.kansai_u.kutc.firefly.packetArt.util.LimitedRing;


/**
 * 効果音を調整するクラスです。<br>
 * <a href="http://www.ibm.com/developerworks/jp/java/library/j-5things12/">IBM</a><br>のページに書かれた手法はCPUを食います。<br>
 * そこで、非常に再生時間が短いSEのために、変わった手法でClipを再生することにしました。<br>
 * リングバッファのようなものを使用し、重複しまくったSEを流す場合は音声が鳴らないバグを許容する、という方法です。<br>
 * 実際のプレイには全く問題ないので、こちらを使ってください。
 * 
 * @author Nakata
*/    
public class PlaySE extends HashMap<String,LimitedRing<Clip>> implements LineListener{
    private static final PlaySE instance = new PlaySE();

    /**
     * @return シングルトンのインスタンスを返します。
    */
    public static  synchronized PlaySE getInstance(){
        //System.out.println("PlaySE.getInstance()");
        return instance;
    }

    //固定のSEここから
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

    public final String RDFSE1_FILE = "resource/se/readdump1se.wav";//ReadDump : TCPパケット到着
    public static final byte RDFSE1 = 20;
    public final String RDFSE2_FILE = "resource/se/readdump2se.wav";//ReadDump : UDPパケット到着
    public static final byte RDFSE2 = 21;
    public final String RDFSE3_FILE = "resource/se/readdump3se.wav";//ReadDump : ICMPパケット到着
    public static final byte RDFSE3 = 22;
    public final String RDFSE4_FILE = "resource/se/readdump4se.wav";//ReadDump : その他パケット到着
    public static final byte RDFSE4 = 23;
    //固定のSEここまで

    //このクラスの肝です。
    public static final int RING_SIZE= 30;//30個の同時重複再生を許します。
    //このクラスの肝です。

    /**
     * このコンストラクタは呼び出されると固定SEをすべて読み出し、
     * 大量のClipへと登録します。
    */
    private PlaySE() {
        super();
        //openFile(MOVE_FILE);//まだファイルが無い
        openFile(HARDDROP_FILE);
        openFile(TURN_FILE);
        //openFile(DEMISE_FILE);//まだファイルが無い
        openFile(SELECT_FILE);
        openFile(CANCEL_FILE);
        //openFile(OPEN_FILE);//まだファイルが無い
        openFile(RDFSE1_FILE);
        openFile(RDFSE2_FILE);
        openFile(RDFSE3_FILE);
        openFile(RDFSE4_FILE);
    }

    /**
     * 新たにファイルから音楽ファイルを読み出し、ClipをRING_SIZE個このクラスに登録し、<br>
     * いつでも多重再生できる状態にします。<br>
     * <br>
     * 不正なファイルを指定された場合・ファイルが（権限不足等で）読めない場合・PCが音声を出力できない場合は<br>
     * falseが返ります。<br>
     *
     * @param fileName ファイル名です。絶対パスで指定してください。
     * @return 成功ならtrueを返します。
    */
    public boolean openFile(String fileName) {
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
            return true;
        } catch (UnsupportedAudioFileException e) {
            //mp4とか、無理なファイルはこっち。
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            //そもそもファイルが存在しないとかいう場合はこっち
            e.printStackTrace();
            return false;
        } catch (LineUnavailableException e) {
            //PCが、音楽を再生できないPCでした。
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 簡易的にstaticなSEを動かすための関数です。<br>
     * 使い方：　playSE.play(PlaySE.SELECT);<br>
     *
     * @param fileID PlaySE.SELECTなど
    */
    public void play(byte fileID) {
        switch(fileID) {
            //case MOVE: play(MOVE_FILE);break;
            case HARDDROP: play(HARDDROP_FILE);break;
            case TURN: play(TURN_FILE);break;
            //case DEMISE: play(DEMISE_FILE);break;
            case SELECT: play(SELECT_FILE);break;
            case CANCEL: play(CANCEL_FILE);break;
            case RDFSE1: play(RDFSE1_FILE);break;
            case RDFSE2: play(RDFSE2_FILE);break;
            case RDFSE3: play(RDFSE3_FILE);break;
            case RDFSE4: play(RDFSE4_FILE);break;
            //case OPEN: play(OPEN_FILE);break;
            default : System.out.println("NO SUCH FILE ID");break;
        }
    }

    /**
     * ファイル名に紐付けされたClipを一つ再生します。
     *
     * @param fileName ファイル名
    */
    public void play(String fileName) {
        get(fileName).peek().start();
    }

    //http://aidiary.hatenablog.com/entry/20061105/1275137770
    /**
     * LineListenerを実装したので必要になった関数です。
     * 能動的に呼びださないでください。
    */
    public void update(LineEvent event) {
        // ストップか最後まで再生された場合
        if (event.getType() == LineEvent.Type.STOP) {
            Clip clip = (Clip) event.getSource();
            clip.stop();
            clip.setFramePosition(0); // 再生位置を最初に戻す
        }
    }

    //http://www.javadocexamples.com/java_source/de/pxlab/pxl/sound/Controls.java.html
    /**
     * 名前のとおりです。
     * 
     * @param clip くりっぷ
     * @param pos 正か負のfloat。ボリュームの増分(マイナスなら減分)を表す
    */
    public static void dialVolume(Line line, float pos) {
        FloatControl ctrl = null;
        try {
            ctrl = (FloatControl)(line.getControl(FloatControl.Type.MASTER_GAIN));
        } catch (IllegalArgumentException iax1) {
            try {
                ctrl = (FloatControl)(line.getControl(FloatControl.Type.VOLUME));
            } catch (IllegalArgumentException iax2) {
                System.out.println("Your Computer setVolume() not supported.");
                return;
            }
        }
        ctrl.setValue(pos);
    }

    //http://www.javadocexamples.com/java_source/de/pxlab/pxl/sound/Controls.java.html
    /**
     * Lineを継承した(Clipなど)何らかの音声用オブジェクトのボリュームを変更します。<br>
     * 多くの場合、そのLineはopen()を呼びだされていなければなりません。 <br>
     * 
     * @param line 音量を変更するLineオブジェクト です。
     * @param volume 1-100で指定された音量の数値です。-1は0に、101は100に自動で変換されます。
    */
    public static void setVolume(Line line, double volume) {
        if (volume > 100.0) volume = 100.0;
        if (volume >= 0.0) volume = 0.0;
        FloatControl ctrl = null;
        try {
            ctrl = (FloatControl)(line.getControl(FloatControl.Type.MASTER_GAIN));
        } catch (IllegalArgumentException iax1) {
            try {
                ctrl = (FloatControl)(line.getControl(FloatControl.Type.VOLUME));
            } catch (IllegalArgumentException iax2) {
                System.out.println("Your Computer setVolume() not supported.");
                return;
            }
        }
        float current = ctrl.getValue();
        float minimum = ctrl.getMinimum();
        float maximum = ctrl.getMaximum();
        float newValue = (float)(minimum + volume * (maximum - minimum) / 100.0F);
        ctrl.setValue(newValue);
    }

    /**
     * このクラスに登録されたすべてのClipに対してsetVolumeを実行します<br>
     *
     * @param volume 1-100で指定された音量の数値です。
    */
    public void setVolumeAll(double volume) {
        for (LimitedRing<Clip> clips : values()) {
            for(Clip clip : clips) {
                setVolume(clip, volume);
            }
        }
    }

}
