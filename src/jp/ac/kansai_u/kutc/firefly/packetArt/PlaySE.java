package jp.ac.kansai_u.kutc.firefly.packetArt;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.net.URL;
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
//import jp.ac.kansai_u.kutc.firefly.packetArt.util.LimitedQueue;


/**
 * SEをならすクラスです。
 * 多重再生をするために、何種類ものClipを予めロードし、複数保有します。<br>
 * <br>
 * つかいかた：<br>
 * <code>
 * //固定SEの場合<br>
 * PlaySE playSE = PlaySE.getInstance();<br>
 * playSE.initialize()//はじめに、ロードする余裕がある時にする。<br>
 * playSE.play("select");<br>
 * <br>
 * //適当なファイルの場合<br>
 * PlaySE playSE = PlaySE.getInstance();<br>
 * playSE.openSE("unko", new File("unko.wav"));<br>
 * //playSE.openSE("unko", new URL("http://www.uncom.jp/files/unko.wav"));でも可<br>
 * playSE.play("unko")<br>
 * <br>
 * //ボリューム変更<br>
 * setVolumeAll(100)<br>
 * <br>
 * </code>
 *
 * @author Nakata
*/
public class PlaySE extends HashMap<String,LimitedRing<Clip>> implements LineListener{
    private static final PlaySE instance = new PlaySE();

    /**
     * 最初はこいつを呼び出したああとinitialize関数を呼び出してください。
     *
     * @return シングルトンのインスタンスを返します。
    */
    public static synchronized PlaySE getInstance(){
        return instance;
    }

    //固定のSEここから
    public static final String MOVE_FILE = "resource/se/move.wav";//ゲーム：ミノ左右下移動
    public static final String MOVE = "move";
    public static final String HARDDROP_FILE = "resource/se/harddrop.wav";//ゲーム：ミノハードドロップ
    public static final String HARDDROP = "harddrop";
    public static final String TURN_FILE = "resource/se/turn.wav";//ゲーム：ミノ回転
    public static final String TURN = "turn";
    public static final String DEMISE_FILE = "resource/se/demise.wav";//ゲーム：ミノ消滅
    public static final String DEMISE = "demise";
    public static final String SELECT_FILE = "resource/se/select.wav";//メニュー：セレクト用効果音
    public static final String SELECT = "select";
    public static final String SELECT2_FILE = "resource/se/select2.wav";//メニュー：セレクト用効果音2
    public static final String SELECT2 = "select2";
    public static final String CANCEL_FILE = "resource/se/cancel.wav";//メニュー：キャンセル音
    public static final String CANCEL = "cancel";
    public static final String OPEN_FILE = "resource/se/open.wav";//アプリケーション起動音（仮)
    public static final String OPEN = "open";

    public static final String RDFSE1_FILE = "resource/se/readdump1se.wav";//ReadDump : TCPパケット到着
    public static final String RDFSE1 = "readdump1se";
    public static final String RDFSE2_FILE = "resource/se/readdump2se.wav";//ReadDump : UDPパケット到着
    public static final String RDFSE2 = "readdump2se";
    public static final String RDFSE3_FILE = "resource/se/readdump3se.wav";//ReadDump : ICMPパケット到着
    public static final String RDFSE3 = "readdump3se";
    public static final String RDFSE4_FILE = "resource/se/readdump4se.wav";//ReadDump : その他パケット到着
    public static final String RDFSE4 = "readdump4se";
    //固定のSEここまで

    //このクラスの肝です。
    public static final int RING_SIZE= 10;//10個の重複再生を許します。
    public static final int QUEUE_SIZE= 5;//5個の同時再生を許します。
    //このクラスの肝です。

    public static boolean inited;
    //private LimitedQueue<Clip> clipHolder;

    /**
     * 何もしません。
    */
    private PlaySE() {
        super();
        inited = false;
        //clipHolder = new LimitedQueue<Clip>(QUEUE_SIZE);
    }

    /**
     * すべての固定SEをファイルからロードします。
     * 数秒の時間がかかります。
    */
    public synchronized void initialize() {
        if (!inited) {
            //openSE(MOVE, new File(MOVE_FILE));
            //System.out.println("Loading " + MOVE_FILE);
            //まだファイルが無い
            System.out.println("Loading " + HARDDROP_FILE);
            openSE(HARDDROP, new File(HARDDROP_FILE));
            System.out.println("Loading " + TURN_FILE);
            openSE(TURN, new File(TURN_FILE));
            System.out.println("Loading " + DEMISE_FILE);
            openSE(DEMISE, new File(DEMISE_FILE));
            System.out.println("Loading " + SELECT_FILE);
            openSE(SELECT, new File(SELECT_FILE));
            System.out.println("Loading " + SELECT2_FILE);
            openSE(SELECT2, new File(SELECT2_FILE));
            System.out.println("Loading " + CANCEL_FILE);
            openSE(CANCEL, new File(CANCEL_FILE));
            System.out.println("Loading " + OPEN_FILE);
            openSE(OPEN, new File(OPEN_FILE));
            System.out.println("Loading " + RDFSE1_FILE);
            openSE(RDFSE1, new File(RDFSE1_FILE));
            System.out.println("Loading " + RDFSE2_FILE);
            openSE(RDFSE2, new File(RDFSE2_FILE));
            System.out.println("Loading " + RDFSE3_FILE);
            openSE(RDFSE3, new File(RDFSE3_FILE));
            System.out.println("Loading " + RDFSE4_FILE);
            openSE(RDFSE4, new File(RDFSE4_FILE));
            System.out.println("PlaySE initialized! ");
            inited = true;
        }
    }

    /**
     * <a href="http://aidiary.hatenablog.com/entry/20061105/1275137770">Clip使い回し</a>
     * 新たにファイルから音楽ファイルを読み出し、ClipをRING_SIZE個このクラスに登録し、<br>
     * いつでも多重再生できる状態にします。<br>
     * <br>
     * 不正なファイルを指定された場合・ファイルが（権限不足等で）読めない場合・PCが音声を出力できない場合は<br>
     * falseが返ります。<br>
     *
     * @param key キーです。どのSEを再生するか選択するのに使います。
     * @param file 音楽ファイルです。
     * @return 成功ならtrueを返します。失敗した場合、登録されません。
    */
    public synchronized boolean openSE(String key, File file) {
        InputStream is = null;
        byte[] data = null;//バイト列で保管
        try {
            is = new FileInputStream(file);
            data = getBytes(is);//バイト列で保管
            is.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
        if (is == null || data == null) {
            return false;
        }
        boolean ret = false;
        synchronized(this) {
            ret = addClips(key, data);
        }
        return ret;
    }

    /**
     * <a href="http://aidiary.hatenablog.com/entry/20061105/1275137770">Clip使い回し</a>
     * 新たにURLから音楽ファイルを読み出し、ClipをRING_SIZE個このクラスに登録し、<br>
     * いつでも多重再生できる状態にします。<br>
     * <br>
     * 不正なURLを指定された場合・PCが音声を出力できない場合は<br>
     * falseが返ります。<br>
     *
     * @param key キーです。どのSEを再生するか選択するのに使います。
     * @param url 音楽ファイルを指し示すURLです。
     * @return 成功ならtrueを返します。失敗した場合、登録されません。
    */
    public synchronized boolean openSE(String key, URL url) {
        byte[] data = null;
        try {
            final InputStream is = url.openStream();
            data = getBytes(is);//バイト列で保管
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (data == null) {
            return false;
        }
        boolean ret = false;
        synchronized(this) {
            ret = addClips(key, data);
        }
        return ret;
    }

    /**
     * <a href="http://aidiary.hatenablog.com/entry/20061105/1275137770">Clip使い回し</a>
     * 新たにバイト列から音楽ファイルを読み出し、ClipをRING_SIZE個このクラスに登録し、<br>
     * いつでも多重再生できる状態にします。<br>
     * <br>
     * 不正なバイト列を指定された場合・PCが音声を出力できない場合は<br>
     * falseが返ります。<br>
     *
     * @param key キーです。どのSEを再生するか選択するのに使います。
     * @param b 音楽のバイト列です
     * @return 成功ならtrueを返します。失敗した場合、登録されません。
    */
    public synchronized boolean openSE(String key, byte[] b) {
        boolean ret = false;
        synchronized(this) {
            ret = addClips(key, b);
        }
        return ret;
    }

    //こいつが核です。
    private boolean addClips(String key, byte[] data) {
        try {
            LimitedRing<Clip> clips = new LimitedRing<Clip>(RING_SIZE);
            for (int i=0;i<RING_SIZE;i++) {
                AudioInputStream inputStream =  //バイト列から読み込み
                        AudioSystem.getAudioInputStream(getInputStream(data));
                AudioFormat format = inputStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(inputStream);
                clip.addLineListener(this);
                inputStream.close();
                clips.add(clip);
            }
            put(key, clips);
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
     * ファイル名に紐付けされたClipを一つ再生します。
     * 「一時的に」Clipの音量をvolに変更し、再生します。
     *
     * @param name 再生するSEの名前
     * @param vol 再生するときの音量
    */
    public synchronized boolean play(String name, double vol) {
        final LimitedRing<Clip> clips = get(name);
        if (clips != null) {
            final Clip clip = clips.peek();
            playTempVol(clip, vol);
            return true;
        } else {
            System.out.println("Before play, initialize() and Load it : " + name);
            return false;
        }
    }

    /**
     * ファイル名に紐付けされたClipを一つ再生します。
     *
     * @param name 再生するSEの名前
    */
    public synchronized boolean play(String name) {
        final LimitedRing<Clip> clips = get(name);
        if (clips != null) {
            final Clip clip = clips.peek();
            clip.start();
            return true;
        } else {
            System.out.println("Before play, initialize() and Load it : " + name);
            return false;
        }
    }

    /*
     * 一時的にClipのボリュームを変更し再生した後ボリュームをもどす。
    */
    private void playTempVol(final Clip clip, double vol) {
        final float buf = getVolume(clip);
        setVolume(clip, vol);
        clip.start();
        new Thread(new Runnable(){
            public void run() {
                clip.drain();
                setVolume(clip, buf);
            }
        }).start();
    }

    /**
     * <a href="http://aidiary.hatenablog.com/entry/20061105/1275137770">Clip使い回し</a>
     * LineListenerを実装したので必要になった関数です。
     * 能動的に呼びださないでください。
    */
    public void update(final LineEvent event) {
        // ストップか最後まで再生された場合
        if (event.getType() == LineEvent.Type.STOP) {
            final Clip clip = (Clip) event.getSource();
            clip.stop();
            clip.setFramePosition(0); // 再生位置を最初に戻す
        }
    }

    /**
     * <a href="http://www.javadocexamples.com/java_source/de/pxlab/pxl/sound/Controls.java.html">Controls</a>
     * 名前のとおりです。
     *
     * @param line ボリュームを変動させたいLineオブジェクト
     * @param pos 正か負のfloat。ボリュームの増分(マイナスなら減分)を表す
    */
    public static void dialVolume(final Line line, float pos) {
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

    /**
     * <a href="http://www.javadocexamples.com/java_source/de/pxlab/pxl/sound/Controls.java.html">Controls</a>
     * Lineを継承した(Clipなど)何らかの音声用オブジェクトのボリュームを返します。<br>
     * 多くの場合、そのLineはopen()を呼びだされていなければなりません。 <br>
     *
     * @return volume 0-100で指定された音量の数値です。エラーなら-1が返ります。
    */
    public synchronized static float getVolume(final Line line) {
        FloatControl ctrl = null;
        try {
            ctrl = (FloatControl)(line.getControl(FloatControl.Type.MASTER_GAIN));
        } catch (IllegalArgumentException iax1) {
            try {
                ctrl = (FloatControl)(line.getControl(FloatControl.Type.VOLUME));
            } catch (IllegalArgumentException iax2) {
                System.out.println("Your Computer getVolume() not supported.");
                return -1;
            }
        }
        final float current = ctrl.getValue();
        final float minimum = ctrl.getMinimum();
        final float maximum = ctrl.getMaximum();
        return (float)( ( ( current  - minimum) / (maximum - minimum) )* 100.0F);
    }

    /**
     * <a href="http://www.javadocexamples.com/java_source/de/pxlab/pxl/sound/Controls.java.html">Controls</a>
     * Lineを継承した(Clipなど)何らかの音声用オブジェクトのボリュームを変更します。<br>
     * 多くの場合、そのLineはopen()を呼びだされていなければなりません。 <br>
     *
     * @param line 音量を変更するLineオブジェクト です。
     * @param volume 0-100で指定された音量の数値です。-1は0に、101は100に自動で変換されます。
    */
    public synchronized static void setVolume(final Line line, double volume) {
        if (volume > 100.0) volume = 100.0;
        if (volume <= 0.0) volume = 0.0;
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
        final float minimum = ctrl.getMinimum();
        final float maximum = ctrl.getMaximum();
        final float newValue = (float)(minimum + volume * (maximum - minimum) / 100.0F);
        ctrl.setValue(newValue);
    }

    /**
     * このクラスに登録され、文字列で指定されたそれぞれのClipのうち、<br>
     * LimitedRingの先頭のものに対してgetVolumeを実行します<br>
     * setVolumeAllを行った後に使うと便利です。
     *
     * @param name SEファイルのキーです。
     * @return ボリューム、1-100の数値。
    */
    public synchronized float getVolume(final String name) {
        LimitedRing<Clip> clips = get(name);
        if (clips == null) {
            return -1;
        }
        final Clip clip = clips.peekFirst();
        return getVolume(clip);
    }

    /**
     * このクラスに登録されたすべてのClipに対してsetVolumeを実行します<br>
     *
     * @param volume 1-100で指定された音量の数値です。
    */
    public synchronized void setVolumeAll(double volume) {
        for (LimitedRing<Clip> clips : values()) {
            for(Clip clip : clips) {
                setVolume(clip, volume);
            }
        }
    }

    /**
     * <a href="http://d.hatena.ne.jp/suusuke/20080127/1201417192">bytestream</a>
     * InputStreamをバイト配列に変換する
     *
     * @param is バイト列に変換したいInputStream
     * @return バイト配列（エラーなら空）
     */
    private static byte[] getBytes(final InputStream is) {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final OutputStream os = new BufferedOutputStream(b);
        int c;
        try {
            while ((c = is.read()) != -1) {
                os.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return b.toByteArray();
    }

    /**
     * <a href="http://d.hatena.ne.jp/suusuke/20080127/1201417192">bytestream</a>
     * InputStreamをバイト配列に変換する
     * バイト列をInputStreamに変換する
     *
     * @param byteData InputStreamに変換したいbyte列
     * @return InputStream （エラーなら空）
    */
    public static InputStream getInputStream(byte[] byteData) {
        return new ByteArrayInputStream(byteData);
    }

    public void debugMe(String header) {
        FloatControl ctrl = null;
        for (LimitedRing<Clip> clips : values()) {
            for(Clip clip : clips) {
                debugMe(header, clip);
            }
        }
    }

    public void debugMe(String header, Line line) {
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
        final float minimum = ctrl.getMinimum();
        final float maximum = ctrl.getMaximum();
        final float current = ctrl.getValue();
        System.out.println("-----------" + header);
        System.out.println("Current Volume (inner Value) = " + current);
        System.out.println("Max Volume (inner Value) = " + maximum);
        System.out.println("Min Volume (inner Value) = " + minimum);
    }
}
