package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


//コロブチカ風BGMのドラム生成を担当するクラス．
public class DrumMaker {
	public static void main(String[] args) {
	}
	
	public static Sequence koroDrumMaker() throws InvalidMidiDataException{
		Sequence sequence = BassMaker.koroBassMaker();
		Track track9 = sequence.createTrack();

		int channel = 9; //トラックチャンネル
		int velocity = 127; //音の強さ
		int instrument = 0; //音色の種類

		int i = 0;
		ShortMessage[] message = new ShortMessage[96];
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track9.add(new MidiEvent(message[i], 0));
		
		//ドラムの生成．同じフレーズが多く続くのでfor文多様．
		int a = 0;
		for (int c = 0; c < 6; c++) {
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, 49, velocity);
			track9.add(new MidiEvent(message[i], a));
			
			a = a + 12;

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, 49, velocity);
			track9.add(new MidiEvent(message[i], a));
			
			for (int d = 0; d < 15; d++){
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, 42, velocity);
				track9.add(new MidiEvent(message[i], a));
				
				a = a + 12;
	
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, 42, velocity);
				track9.add(new MidiEvent(message[i], a));
			}
		}
		return sequence;
	}
}
