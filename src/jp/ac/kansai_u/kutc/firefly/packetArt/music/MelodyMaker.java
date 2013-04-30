package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

//BGMのメロディを作成するクラス
public class MelodyMaker {

	public static void main(String[] args) {
	}

	public static Sequence setMelodyLine(int velo) throws InvalidMidiDataException, MidiUnavailableException{

		//使用するSequenceを定義．以降すべてに於いてこれを使いまわす．
		Sequence sequence = new Sequence(Sequence.PPQ, 24, 3);
		
		//メロディはtrack0を使用．
		Track track0 = sequence.createTrack();

		int channel = 0; //トラックチャンネル
		int velocity = VelocityModulator.setVelocity(velo);; //音の強さ
		int instrument = 80; //音色の種類：リード
		
		int[] melody = MelodyAlgorithm.melodyAlgorithm();
		
		int i = 0;
		ShortMessage[] message = new ShortMessage[24];
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track0.add(new MidiEvent(message[i], 0));
		
		int a = 0;
		int count = 2;
		for (i = 0; i < 24; i++) {	
			//4分音符
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], a));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], a + 24));
			
			//再抽選
			melody = MelodyAlgorithm.melodyAlgorithm();
			Random rnd = new Random();
			int tmp = rnd.nextInt(3);
			
			if(tmp == 2){
				//8分音符
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], a + 24));
	
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], a + 36));
				
				if((melody[i] + count) == 64 || (melody[i] + count) == 66 || (melody[i] + count) == 67
					|| (melody[i] + count) == 69 || (melody[i] + count) == 72
					|| (melody[i] + count) == 74 || (melody[i] + count) == 76 || (melody[i] + count) == 78
					|| (melody[i] + count) == 79){
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i] + count, velocity);
					track0.add(new MidiEvent(message[i], a + 36));
	
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i] + count, velocity);
					track0.add(new MidiEvent(message[i], a + 48));
				}else{
					count = count + 1;
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i] + count, velocity);
					track0.add(new MidiEvent(message[i], a + 36));
	
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i] + count, velocity);
					track0.add(new MidiEvent(message[i], a + 48));
					count = count - 1;
				}
			}else if((tmp == 0) || (tmp == 1) ){
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], a + 24));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], a + 48));
			}
			
			if(count == 2){
				count = count - 4;
			}else if(count == -2){
				count = count + 4;
			}
			a = a + 48;
		}
		return sequence;
	}
}
