package jp.ac.kansai_u.kutc.firefly.packetArt;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

//コロブチカと同じコード進行のベースラインとドラムを作成するクラス．
public class KoroRythmeMaker{
//	private static Sequence sequence;
	
	public static void main(String[] args) throws InvalidMidiDataException {
		KoroRythmeMaker.midiMaker();
	}

	public static Sequence koroBassMaker() throws InvalidMidiDataException {
		int[] EMinorBass = ScaleMaker.eMinorBassScale();

			//24tick=四分音符
//			Sequence sequence = new Sequence(Sequence.PPQ, 24, 2);	
		Sequence sequence = KoroMelodyMaker.koroMelodyMaker();
			Track track1 = sequence.createTrack();
			

			int channel = 1; //トラックチャンネル
			int velocity = 127; //音の強さ
			int instrument = 33; //音色の種類

			int i = 0;
			ShortMessage[] message = new ShortMessage[96];
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
			track1.add(new MidiEvent(message[i], 0));

			//メロディの設定
			//String[] Codeの長さは24
			String[] Code = KoroCode.koroCode();
			
			int pitch = 0;
			int a = 0;
			int b = 0;
			
			//コードの判定
			for (int c = 0; c < 24; a++) {
				if("Em".equals(Code[c])){
					pitch = 0;
					a = 2;
				}else if("Am".equals(Code[c])){
					pitch = 3;
					a = 2;
				}else if("B7".equals(Code[c])){
					pitch = 4;
					a = 4;
				}
				
				//ベースラインの生成
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, EMinorBass[pitch], velocity);
				track1.add(new MidiEvent(message[i], b));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, EMinorBass[pitch], velocity);
				track1.add(new MidiEvent(message[i], b + 12));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, EMinorBass[pitch + a], velocity);
				track1.add(new MidiEvent(message[i], b + 12));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, EMinorBass[pitch + a], velocity);
				track1.add(new MidiEvent(message[i], b + 24));
				
				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, EMinorBass[pitch], velocity);
				track1.add(new MidiEvent(message[i], b + 24));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, EMinorBass[pitch], velocity);
				track1.add(new MidiEvent(message[i], b + 36));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_ON, channel, EMinorBass[pitch + a], velocity);
				track1.add(new MidiEvent(message[i], b + 36));

				message[i] = new ShortMessage();
				message[i].setMessage(ShortMessage.NOTE_OFF, channel, EMinorBass[pitch + a], velocity);
				track1.add(new MidiEvent(message[i], b + 48));
				
				b = b + 48;
				c++;
			}
			return sequence;
		}
	
	//ドラムチャンネル
	public static Sequence koroDrumMaker() throws InvalidMidiDataException{

		//koroBassMakerで使用したsequenceに追記するので持ってくる．
		Sequence sequence = koroBassMaker();
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
		
	public static void midiMaker() throws InvalidMidiDataException{
		try{
			int[] midi = MidiSystem.getMidiFileTypes(koroDrumMaker());
			
			//書き出し．koroDrumMaker()=sequence, midi[0]を使わないと複数トラックの出力ができない．
			MidiSystem.write(koroDrumMaker(), midi[0], new java.io.File("hello.mid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


