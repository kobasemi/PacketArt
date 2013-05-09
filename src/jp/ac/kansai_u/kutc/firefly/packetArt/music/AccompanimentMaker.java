package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * 伴奏を作成します。
 * MusicPlayerからのみ呼び出されます。
 * @author Lisa
 */
public class AccompanimentMaker {

	/**
	 * 明るい伴奏を作成します。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	public static void makeCheerfulAccompaniment(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		setCheerfulBass(sequence, length, velocity);
		setCheerfulGuitar(sequence, length, velocity);
		setDrums(sequence, length, velocity);
	}
	
	/**
	 * 暗い伴奏を作成します。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	public static void makeGloomyAccompaniment(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		setGloomyBass(sequence, length, velocity);
		setGloomyGuitar(sequence, length, velocity);
		setDrums(sequence, length, velocity);
	}
	
	/**
	 * 明るいベースラインを作成します。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	private static void setCheerfulBass(Sequence sequence, int length, int velocity) throws InvalidMidiDataException {
		Track track1 = sequence.createTrack();	// ベースラインに使用するトラックを作成。
		int channel = 1;			// ベースラインは1チャンネルを使用。
		int instrument = 33;		// 33 = Electoric Finger Bass
		int[] scale = ScaleMaker.setCheerfulBassScale();	// 使用する鍵盤を定義。
		String[] code = CodeMaker.setCheerfulCode(length);	// 使用するコード進行を定義。
		int komari = code.length;	// コードが入っている配列の長さ。
		int haruka = 0;				// 使用する鍵盤の番号が入る変数。
		int kanata = 2;				// 音の差数が入る変数。2で固定。
		int yuiko = 0;				// 音が入る時間の変数。
		
		// ベースラインの初期設定。
		ShortMessage[] message = new ShortMessage[komari];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track1.add(new MidiEvent(message[0], 0));
		
		// コードと音などの対応定義。 
		for(int i = 0; i < komari; i++){
			if("C".equals(code[i])){
				haruka = 0;		// C - E
			}else if("F".equals(code[i])){
				haruka = 3;		// F - A
			}else if("G7".equals(code[i])){
				haruka = 4;		// G - B
			}
			
			// ここからベースライン作成。
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 24));
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 24));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 48));
			
			// 48 = 1/2小節。
			yuiko = yuiko + 48;
		}
	}
	
	/**
	 * 明るいギターアルペジオを作成します。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	private static void setCheerfulGuitar(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		Track track2 = sequence.createTrack(); // ギターアルペジオに使用するトラックを作成。
		int channel = 2;		// ギターアルペジオは2チャンネルに作成。
		int instrument = 10;	// 10 = Music Box
		int[] scale = ScaleMaker.setCheerfulGuitarScale();
		String[] code = CodeMaker.setCheerfulCode(length);
		int komari = code.length;
		int haruka = 0;
		int kanata = 2;
		int yuiko = 0;
		
		ShortMessage[] message = new ShortMessage[komari];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track2.add(new MidiEvent(message[0], 0));
		
		for(int i = 0; i < komari; i++){
			if("C".equals(code[i])){
				haruka = 0;
			}else if("F".equals(code[i])){
				haruka = 3;
			}else if("G7".equals(code[i])){
				haruka = 4;
			}
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka], velocity);
			track2.add(new MidiEvent(message[i], yuiko));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 24));
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 24));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 48));
			
			yuiko = yuiko + 48;
		}
	}
	
	/**
	 * 暗いベースラインを作成します。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	private static void setGloomyBass(Sequence sequence, int length, int velocity) throws InvalidMidiDataException {
		Track track1 = sequence.createTrack();
		int channel = 1;
		int instrument = 33;
		int[] scale = ScaleMaker.setGloomyBassScale();
		String[] code = CodeMaker.setGloomyCode(length);
		int komari = code.length;
		int haruka = 0;
		int kanata = 2;
		int yuiko = 0;
		
		ShortMessage[] message = new ShortMessage[(komari * 4) + 1];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track1.add(new MidiEvent(message[0], 0));
		
		for(int i = 0; i < komari; i++){
			if("Em".equals(code[i])){
				haruka = 0;		// E - G
			}else if("Am".equals(code[i])){
				haruka = 3;		// A - C
			}else if("B7".equals(code[i])){
				haruka = 4;		// B - D
			}
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 24));
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 24));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track1.add(new MidiEvent(message[i], yuiko + 48));
			
			yuiko = yuiko + 48;
		}
	}
	
	/**
	 * 暗いギターアルペジオを作成します。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	private static void setGloomyGuitar(Sequence sequence, int length ,int velocity) throws InvalidMidiDataException{
		Track track2 = sequence.createTrack();
		int channel = 2;
		int instrument = 24;
		int[] scale = ScaleMaker.setGloomyGuitarScale();
		String[] code = CodeMaker.setGloomyCode(length);
		int komari = code.length;
		int haruka = 0;
		int kanata = 2;
		int yuiko = 0;
		
		ShortMessage[] message = new ShortMessage[komari];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track2.add(new MidiEvent(message[0], 0));
		
		for(int i = 0; i < komari; i++){
			if("Em".equals(code[i])){
				haruka = 0;
			}else if("Am".equals(code[i])){
				haruka = 3;
			}else if("B7".equals(code[i])){
				haruka = 4;
			}
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka], velocity);
			track2.add(new MidiEvent(message[i], yuiko));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 12));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 24));
			
			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 24));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_ON, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 36));

			message[i] = new ShortMessage();
			message[i].setMessage(ShortMessage.NOTE_OFF, channel, scale[haruka + kanata], velocity);
			track2.add(new MidiEvent(message[i], yuiko + 48));
			
			yuiko = yuiko + 48;
		}
	}	
	
	/**
	 * ドラムラインを作成します。
	 * これは共通して使用します。
	 * @param sequence
	 * @param length
	 * @param velocity
	 * @throws InvalidMidiDataException
	 */
	private static void setDrums(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		Track track9 = sequence.createTrack();	// ドラムラインで使用するトラックを作成。
		int channel = 9;			// ドラムラインには9チャンネル(実質10)を使用。
		int instrument = 0;			// 0 = STANDARD
		String[] code = CodeMaker.setCheerfulCode(length);	// 長さだけを知りたいので定義。
		int komari = code.length;
		int riki = 0;				// シンバル系の音が入る時間の変数。
		int kyosuke = 0;			// ドラム系の音が入る時間の変数。
		
		ShortMessage[] message = new ShortMessage[komari];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track9.add(new MidiEvent(message[0], 0));
		
		// クラッシュシンバル(49)とハイハット(42)の作成。
		for(int i = 0; i < (komari / 24); i++){
			for(int j = 0; j < 6; j++){
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_ON, channel, 49, velocity);
				track9.add(new MidiEvent(message[j], riki));
				
				riki = riki + 12;
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_OFF, channel, 49, velocity);
				track9.add(new MidiEvent(message[j], riki));
				
				for (int k = 0; k < 15; k++){
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_ON, channel, 42, velocity);
					track9.add(new MidiEvent(message[k], riki));
					
					riki = riki + 12;
		
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_OFF, channel, 42, velocity);
					track9.add(new MidiEvent(message[k], riki));
				}			
			}
			
			//バスドラム(35)とスネア(38)の生成。
			for(int j = 0; j < 3; j++){
				for(int k = 0; k < 3; k++){
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_ON, channel, 35, velocity);
					track9.add(new MidiEvent(message[k], kyosuke));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_OFF, channel, 35, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 24));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_ON, channel, 38, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 24));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_OFF, channel, 38, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 48));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_ON, channel, 35, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 48));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_OFF, channel, 35, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 60));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_ON, channel, 35, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 60));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_OFF, channel, 35, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 72));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_ON, channel, 38, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 72));
					
					message[k] = new ShortMessage();
					message[k].setMessage(ShortMessage.NOTE_OFF, channel, 38, velocity);
					track9.add(new MidiEvent(message[k], kyosuke + 96));
					kyosuke = kyosuke + 96;
				}
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_ON, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_OFF, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 24));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_ON, channel, 38, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 24));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_OFF, channel, 38, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 48));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_ON, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 48));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_OFF, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 60));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_ON, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 60));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_OFF, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 72));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_ON, channel, 38, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 72));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_OFF, channel, 38, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 84));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_ON, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 84));
				
				message[j] = new ShortMessage();
				message[j].setMessage(ShortMessage.NOTE_OFF, channel, 35, velocity);
				track9.add(new MidiEvent(message[j], kyosuke + 96));
				kyosuke = kyosuke + 96;
			}
		}
	}
}

