package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.io.File;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiPlayer extends Thread {
	private int coefficient;
	private String filename;
	private static boolean killps;
	private static Sequencer sequencer;

	/**
	 * タイトルのBGMを鳴らすメソッドです。
	 * getVolMusic()で数字が取ってこれるようになってから
	 * playTitleMusic()を呼び出してもらえたら
	 * 要求が実現できると思います
	 * 
	 * @throws Exception
	 */

	public MidiPlayer(int _coefficient, String _filename) {
		coefficient = _coefficient;
		filename = _filename;
		killps = false;
	}

	public void run() {
		while(killps == false){
			try {
				playMidiMusic(coefficient, filename);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	
	public void stopMidi(){
		killps = true;
		if(sequencer.isRunning() == true){
			sequencer.stop();
		}
	}

	//TODO: Config系統からの音量情報の受け取り
	//		int velo = XXXX.getVolMusic();
	public static void playMidiMusic(int coefficient, String filename) throws Exception {
		Sequence sequence = setNewSequence(coefficient, filename);
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();

			sequencer.setSequence(sequence);
			sequencer.start();
			while (sequencer.isRunning())
				Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (sequencer != null && sequencer.isOpen())
				sequencer.close();
				killps = true;
		}
	}

	/**
	 * 読み込んだ曲の解像度を解析して、新しいSequenceを返すメソッドです。
	 * changeVelocity()からのみ読み出されます。
	 * 
	 * @return sequence
	 * @throws Exception
	 */

	public static Sequence getSequenceData(String filename) throws Exception {
		Sequence sequence0 = MidiSystem.getSequence(new File("resource/se/" + filename));
		float division = sequence0.getDivisionType();
		int resolution = sequence0.getResolution();

		Sequence sequence = new Sequence(division, resolution);
		return sequence;
	}

	/**
	 * 音量を調節した新しいSequenceを返すメソッドです。
	 * playTitleMusic()からのみ読み出されます。
	 * 
	 * @return sequence
	 * @throws Exception
	 */

	public static Sequence setNewSequence(int coefficient, String filename) throws Exception {

		Sequence sequence0 = MidiSystem.getSequence(new File("resource/se/" + filename));
		Sequence sequence = getSequenceData(filename);
		Track track1 = sequence.createTrack();

		ShortMessage[] message1 = new ShortMessage[25565];

		for (Track track : sequence0.getTracks()) {
			for (int i = 0; i < track.size(); i++) {
				MidiEvent event = track.get(i);
				long time = event.getTick();

				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage) {
					ShortMessage sm = (ShortMessage) message;

					int channel = sm.getChannel();
					int command = sm.getCommand();
					int data1 = sm.getData1();
					int basicvelocity = sm.getData2();
					int velocity = (int) (basicvelocity * coefficient / 100);

					if (i == 0) {
						message1[i] = new ShortMessage();
						message1[i].setMessage(command, channel, data1, basicvelocity);
						track1.add(new MidiEvent(message1[i], time));
					} else {
						message1[i] = new ShortMessage();
						message1[i].setMessage(command, channel, data1, velocity);
						track1.add(new MidiEvent(message1[i], time));
					}
				}
			}
		}
		return sequence;
	}

}
