package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

//形だけ作った感じです。改良必須。

/**
 * コンフィグ画面でBGM音量調節にて、確認用のBGMを再生、生成するときに使用するクラスです。<br>
 * 実行時は<br>
 * <code>BGMExperimenter.playExperimentBGM();</code>
 * などと指定していただければ動くと思います。
 * 
 * @author Lisa
 *
 */
public class BGMExperimenter{
	
	public static void playExperimentBGM() throws Exception{
		Sequencer sequencer = null;
		Sequence sequence = makeExperimentBGM();
		try{
			sequencer  = MidiSystem.getSequencer();
			sequencer.open();
			
			sequencer.setSequence(sequence);
			sequencer.start();
			while(sequencer.isRunning()) Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if (sequencer != null && sequencer.isOpen()) sequencer.close();
		}
	}

	
	/**
	 * 音量調整テストの時に流れるBGMを生成します。
	 * この関数はplayExperiment()BGMからのみ呼び出されます。
	 * 
	 * @return sequence
	 * @throws Exception
	 */
	
	public static Sequence makeExperimentBGM() throws Exception{
		
		//本当はやりたくないけど音量確認用のBGMを指定された音量で、『ここで』作成。
		//TODO: 予め用意しておいたMidiファイルを読み込み、音量を変換した上で再出力できるように。
		Sequence sequence = new Sequence(Sequence.PPQ, 24);
		Track track = sequence.createTrack();
		
		int channel = 0; //トラックチャンネル
		
		//暫定的に音の強さは100固定になっています。
		//TODOが達成できるようになれば、コメントアウトをはずしてそっちにして下さい。
		//TODO: Config系統からの音量情報の受け取り(設定画面でのBGM再生)
		//int velocity = XXXX.getVolMusic();
		int velocity = 100; //音の強さ
		
		int instrument = 0; //音色の種類：リード
		
		int i = 0;
		ShortMessage[] message = new ShortMessage[24];
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track.add(new MidiEvent(message[i], 0));
		
		//C
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 60, velocity);
		track.add(new MidiEvent(message[i], 0));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 60, velocity);
		track.add(new MidiEvent(message[i], 12));
		
		//E
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 64, velocity);
		track.add(new MidiEvent(message[i], 12));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 64, velocity);
		track.add(new MidiEvent(message[i], 24));
		
		
		//G
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 67, velocity);
		track.add(new MidiEvent(message[i], 24));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 67, velocity);
		track.add(new MidiEvent(message[i], 36));
		
		//C 8va
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 72, velocity);
		track.add(new MidiEvent(message[i], 36));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 72, velocity);
		track.add(new MidiEvent(message[i], 48));
		
		//C
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 60, velocity);
		track.add(new MidiEvent(message[i], 48));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 60, velocity);
		track.add(new MidiEvent(message[i], 72));
		
		//最後の和音
		//C
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 60, velocity);
		track.add(new MidiEvent(message[i], 72));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 60, velocity);
		track.add(new MidiEvent(message[i], 120));
		
		//E
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 64, velocity);
		track.add(new MidiEvent(message[i], 72));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 64, velocity);
		track.add(new MidiEvent(message[i], 120));
		
		//G
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 67, velocity);
		track.add(new MidiEvent(message[i], 72));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 67, velocity);
		track.add(new MidiEvent(message[i], 120));
		
		//C8va
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_ON, channel, 72, velocity);
		track.add(new MidiEvent(message[i], 72));

		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.NOTE_OFF, channel, 72, velocity);
		track.add(new MidiEvent(message[i], 120));
		return sequence;
		
	}

}