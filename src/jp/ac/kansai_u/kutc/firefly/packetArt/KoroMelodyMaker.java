import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

//コロブチカのコード進行からメロディを作成するクラス．
public class KoroMelodyMaker{
	
	public static void main(String[] args) throws InvalidMidiDataException {
		KoroMelodyMaker.koroMelodyMaker();
	}

	public static Sequence koroMelodyMaker() throws InvalidMidiDataException {
		int[] EMinorBass = ScaleMaker.eMinorBassScale();

		//24tick=四分音符
		Sequence sequence = new Sequence(Sequence.PPQ, 24, 3);	
		Track track0 = sequence.createTrack();

		int channel = 0; //トラックチャンネル
		int velocity = 127; //音の強さ
		int instrument = 80; //音色の種類
		String[] Code = KoroCode.koroCode();
		int[] melody = new int[24];
		
		// E G B
		int[] emmel = {EMinorBass[0] + 24, EMinorBass[2] + 24, EMinorBass[4] + 24};
		
		// A C E
		int[] ammel = {EMinorBass[3] + 24, EMinorBass[5] + 24, EMinorBass[7] + 24};
		
		// F# A B D#
		int[] b7mel = {EMinorBass[1] + 24, EMinorBass[3] + 24, EMinorBass[4] + 24, EMinorBass[6] + 25};

		int i = 0;
		ShortMessage[] message = new ShortMessage[24];
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track0.add(new MidiEvent(message[i], 0));

		//メロディの設定
		//実験的にメロディはランダムで生成．
		

/*
		for(int a = 0; a < 24; a++){
			if("Em".equals(Code[a])){
				melody[a] = emmel[tmp1];
			}else if("Am".equals(Code[a])){
				melody[a] = ammel[tmp1];
			}else if("B7".equals(Code[a])){
				melody[a] = b7mel[tmp2];
			}
		}
		*/
			
		int b = 0;
		for (i = 0; i < 24; i++) {	
			
			Random rnd1 = new Random();
			Random rnd2 = new Random();
			int tmp1 = rnd1.nextInt(3);
			int tmp2 = rnd2.nextInt(4);
			
			if("Em".equals(Code[i])){
				melody[i] = emmel[tmp1];
			}else if("Am".equals(Code[i])){
				melody[i] = ammel[tmp1];
			}else if("B7".equals(Code[i])){
				melody[i] = b7mel[tmp2];
			}
			
			System.out.println(melody[i]);
		
			//メロディの生成
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], b));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], b + 24));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], b + 24));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, melody[i], velocity);
			track0.add(new MidiEvent(message[i], b + 48));
			
			b = b + 48;
		}
		return sequence;
	}

/*
	public static void midiMaker() throws InvalidMidiDataException{
		try{
//			int[] midi = MidiSystem.getMidiFileTypes(koroMelodyMaker());
			
			//書き出し．koroDrumMaker()=sequence, midi[0]を使わないと複数トラックの出力ができない．
			MidiSystem.write(koroMelodyMaker(), 0, new java.io.File("hello.mid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}


