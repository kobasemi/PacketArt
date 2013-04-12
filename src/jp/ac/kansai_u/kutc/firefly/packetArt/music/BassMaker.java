package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


//BGMのベースライン生成を担当するクラス．
public class BassMaker {
	public static void main(String[] args) {
	}
	
	public static Sequence setBassLine(int velo) throws InvalidMidiDataException, MidiUnavailableException{
		
		//ベースラインはコードを基にして生成．固定．
		int[] bassscale = ScaleMaker.setBassScale();
		
		Sequence sequence = MelodyMaker.setMelodyLine(velo);
		Track track1 = sequence.createTrack();

		int channel = 1; //トラックチャンネル
		int velocity = VelocityModulator.setVelocity(velo); //音の強さ
		int instrument = 33; //音色の種類

		int i = 0;
		ShortMessage[] message = new ShortMessage[96];
		message[i] = new ShortMessage();
		message[i].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track1.add(new MidiEvent(message[i], 0));

		//メロディの設定
		//String[] Codeの長さは24
		String[] Code = CodeMaker.codeMaker();
		
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
			message[i].setMessage(ShortMessage.NOTE_ON, channel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(message[i], b));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(message[i], b + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(message[i], b + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(message[i], b + 24));
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(message[i], b + 24));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(message[i], b + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(message[i], b + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(message[i], b + 48));
			
			b = b + 48;
			c++;
		}
		//次はドラムへ
		return sequence;
	}
}