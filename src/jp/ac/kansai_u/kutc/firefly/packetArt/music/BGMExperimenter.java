package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.io.File;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


//エラー吐いていますが、後々解消されるものであり
//致命的なものでもないので無視してます。実験で動かしたい場合は
//int coefficientに0～3の数字を入れた上で実行して下さい。

/**
 * 設定画面で音量調整の確認用BGMを扱うクラスです。
 * 基本的にplayChangedBGM()を使ってもらえればいいはずです。
 * 
 * @author Lisa
 *
 */

public class BGMExperimenter{
	
	/**
	 * 音量が調整されたBGMを鳴らすメソッドです。
	 * getVolMusic()で数字が取ってこれるようになってから
	 * playChangedBGM()を呼び出してもらえたら
	 * 要求が実現できると思います
	 * 
	 * @throws Exception
	 */
	
	public static void playChangedBGM(int coefficient) throws Exception{
		Sequencer sequencer = null;
		Sequence sequence = setNewSequence(coefficient);
		try{
			sequencer = MidiSystem.getSequencer();
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
	 * 読み込んだ曲の解像度を解析して、新しいSequenceを返すメソッドです。
	 * changeVelocity()からのみ読み出されます。
	 * 
	 * @return sequence
	 * @throws Exception
	 */
	
	public static Sequence getSequenceData() throws Exception{
		Sequence sequence0 = MidiSystem.getSequence(new File("resource/se/BGMTestSound.mid"));
		float division = sequence0.getDivisionType();
		int resolution = sequence0.getResolution();
		
		Sequence sequence = new Sequence(division, resolution);
		return sequence;
	}
	
	
	/**
	 * 音量を調節した新しいSequenceを返すメソッドです。
	 * playChengedBGM()からのみ読み出されます。
	 * 
	 * @return sequence
	 * @throws Exception
	 */
	
	public static Sequence setNewSequence(int coefficient) throws Exception{
		
		//TODO: Config系統からの音量情報の受け取り(設定画面でのBGM再生)
		
		//初期velocity(basicvelocity)は100で、coefficientには0～3の間で数値が来るので
		//velocity * (coefficient / 3)とすることで
		//音量をミュートを含む4段階で表現する。
		
		//int coefficient = XXXX.getVolMusic();
		
		Sequence sequence0 = MidiSystem.getSequence(new File("resource/se/BGMTestSound.mid"));
		Sequence sequence = getSequenceData();
		Track track1 = sequence.createTrack();

		ShortMessage[] message1 = new ShortMessage[96];
		
		for(Track track : sequence0.getTracks()){
			for(int i = 0; i < track.size(); i++){
				System.out.println("");
				MidiEvent event = track.get(i);
				long time = event.getTick();
				System.out.println("Time:" + time);
				
				MidiMessage message = event.getMessage();
				if(message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;

					int channel = sm.getChannel();
					int command = sm.getCommand();
					int data1 = sm.getData1();
					int basicvelocity = sm.getData2();
					
					//このvelocityの値が確認用BGMの音量となる。
					int velocity = basicvelocity * (coefficient / 3);
					
					message1[i] = new ShortMessage();
					message1[i].setMessage(command, channel, data1, velocity);
					track1.add(new MidiEvent(message1[i], time));
				}
			}
		}
		return sequence;
	}
}
