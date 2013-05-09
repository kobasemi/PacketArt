package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * 主旋律を作成するクラスです。
 * MusicPlayerからのみ呼び出されます。
 * @author Lisa
 */
public class MelodyMaker {
	
	/**
	 * 明るい主旋律を作成するメソッドです。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	public static void setCheerfulMelody(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		Track track0 = sequence.createTrack();	// メロディラインに使用するトラックを作成。
		int channel = 0;				//メロディラインは0チャンネルを使用。
		int instrument = 80;			// 80 = Square Lead
		// MelodyAlgorithmで定義した音階表の呼び出し。
		int[] melody = MelodyAlgorithm.defCheerfulAlgorithm(length);
		int komari = melody.length;		// コードが入っている配列の長さ。
		int rin = 0;					// 使用する鍵盤の番号が入る変数。
		int kudo = 2;					// 音の差数が入る変数。2で固定。

		// メロディラインの初期設定。
		ShortMessage[] message = new ShortMessage[komari];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track0.add(new MidiEvent(message[0], 0));
		
		// ここからメロディライン作成。
		for(int i = 0; i < komari; i++){
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], rin));
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], rin + 24));
			
			// 遊びを入れるための乱数定義。
			Random rnd = new Random();
			int tmp = rnd.nextInt(4);
			
			if(tmp == 3){	//遊び無し。
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 24));
				
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 48));
			}else if((tmp == 0) || (tmp == 1) || (tmp == 2)){	// 遊び有り。
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 24));
				
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 36));
				
				// 不協和音にならないための条件分岐。触るな危険。
				if((melody[i] + kudo) == 60 || (melody[i] + kudo) == 62 || (melody[i] + kudo) == 64
				|| (melody[i] + kudo) == 65 || (melody[i] + kudo) == 67 || (melody[i] + kudo) == 69 
				|| (melody[i] + kudo) == 72 || (melody[i] + kudo) == 74){
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 36));
	
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 48));
				// 半音対策。
				}else{
					kudo = kudo + 1;
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 36));
	
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 48));
					kudo = kudo - 1;
				}
			// ありえないが、念のための例外？
			}else{
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 24));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 48));
			}
			
			// 音の差数の再定義。
			if(kudo == 2){
				kudo = kudo - 4;
			}else if(kudo == -2){
				kudo = kudo + 4;
			}
			rin = rin + 48;
		}
	}
	
	/**
	 * 暗い主旋律を作成するメソッドです。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	public static void setGloomyMelody(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		Track track0 = sequence.createTrack();
		int channel = 0;
		int instrument = 80;
		int[] melody = MelodyAlgorithm.defGloomyAlgorithm(length);
		
		int komari = melody.length;
		int rin = 0;
		int kudo = 2;

		ShortMessage[] message = new ShortMessage[komari];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track0.add(new MidiEvent(message[0], 0));
		
		for(int i = 0; i < komari; i++){
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], rin));
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], rin + 24));
			
			Random rnd = new Random();
			int tmp = rnd.nextInt(4);
			
			if(tmp == 3){
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 24));
				
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 48));
			}else if((tmp == 0) || (tmp == 1) || (tmp == 2)){
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 24));
				
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 36));
				
				if((melody[i] + kudo) == 64 || (melody[i] + kudo) == 66 || (melody[i] + kudo) == 67
				|| (melody[i] + kudo) == 69 || (melody[i] + kudo) == 72
				|| (melody[i] + kudo) == 74 || (melody[i] + kudo) == 76 || (melody[i] + kudo) == 78
				|| (melody[i] + kudo) == 79){
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 36));
	
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 48));
				}else{
					kudo = kudo + 1;
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 36));
	
					message[i] = new ShortMessage();
					message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i] + kudo, velocity);
					track0.add(new MidiEvent(message[i], rin + 48));
					kudo = kudo - 1;
				}
			}else{
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 24));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
				track0.add(new MidiEvent(message[i], rin + 48));
			}
				
			if(kudo == 2){
				kudo = kudo - 4;
			}else if(kudo == -2){
				kudo = kudo + 4;
			}
			rin = rin + 48;
		}
	}
}
	
	