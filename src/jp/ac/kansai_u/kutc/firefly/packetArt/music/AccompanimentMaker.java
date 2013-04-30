package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
/**
 * 
 * ゲーム中のBGMの固定部分（ベースとドラム）を生成するクラスです。
 * @author Lisa
 * 
 */

public class AccompanimentMaker {
	
	/**
	 * 
	 * ゲーム中のBGMの固定部分（ベースとドラム）をsequenceに書き込み、そのsequenceを返すメソッドです。
	 * MusicMakerからのみ呼び出されます。
	 *  
	 * @param velo
	 * @return sequence
	 * @throws InvalidMidiDataException
	 * @throws MidiUnavailableException
	 */
	public static Sequence setAccompaniment(int velo) throws InvalidMidiDataException, MidiUnavailableException{
		
		//***まずはベースラインの作成***//
		int[] bassscale = ScaleMaker.setBassScale();
		
		Sequence sequence = MelodyMaker.setMelodyLine(velo);
		Track track1 = sequence.createTrack();

		int basechannel = 1; //トラックチャンネル
		int velocity = VelocityModulator.setVelocity(velo); //音の強さ
		int baseinstrument = 33; //音色の種類

		int i = 0;
		ShortMessage[] basemessage = new ShortMessage[96];
		basemessage[i] = new ShortMessage();
		basemessage[i].setMessage(ShortMessage.PROGRAM_CHANGE, basechannel, baseinstrument, 0);
		track1.add(new MidiEvent(basemessage[i], 0));

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
			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_ON, basechannel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(basemessage[i], b));

			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_OFF, basechannel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(basemessage[i], b + 12));

			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_ON, basechannel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(basemessage[i], b + 12));

			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_OFF, basechannel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(basemessage[i], b + 24));
			
			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_ON, basechannel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(basemessage[i], b + 24));

			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_OFF, basechannel, bassscale[pitch], velocity);
			track1.add(new MidiEvent(basemessage[i], b + 36));

			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_ON, basechannel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(basemessage[i], b + 36));

			basemessage[i] = new ShortMessage();
			basemessage[i].setMessage(ShortMessage.NOTE_OFF, basechannel, bassscale[pitch + a], velocity);
			track1.add(new MidiEvent(basemessage[i], b + 48));
			
			b = b + 48;
			c++;
		}
		
		
		//***ドラムラインの作成***//
		Track track9 = sequence.createTrack();

		int drumchannel = 9; //トラックチャンネル
		int druminstrument = 0; //音色の種類

		int j = 0;
		ShortMessage[] drummessage = new ShortMessage[96];
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.PROGRAM_CHANGE, drumchannel, druminstrument, 0);
		track9.add(new MidiEvent(drummessage[i], 0));
		
		//クラッシュシンバルとハイハット
		a = 0;
		for (int c = 0; c < 6; c++) {
			drummessage[j] = new ShortMessage();
			drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 49, velocity);
			track9.add(new MidiEvent(drummessage[j], a));
			
			a = a + 12;

			drummessage[j] = new ShortMessage();
			drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 49, velocity);
			track9.add(new MidiEvent(drummessage[j], a));
			
			for (int e = 0; e < 15; e++){
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 42, velocity);
				track9.add(new MidiEvent(drummessage[j], a));
				
				a = a + 12;
	
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 42, velocity);
				track9.add(new MidiEvent(drummessage[j], a));
			}
		}
		
		//スネアとドラム
		int e = 0;
		for(int h = 0; h < 3; h++){
			for(int f = 0; f < 3; f++){
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 35, velocity);
				track9.add(new MidiEvent(drummessage[j], e));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 35, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 24));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 38, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 24));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 38, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 48));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 35, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 48));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 35, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 60));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 35, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 60));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 35, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 72));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 38, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 72));
				
				drummessage[j] = new ShortMessage();
				drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 38, velocity);
				track9.add(new MidiEvent(drummessage[j], e + 96));
				e = e + 96;
			}
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 24));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 38, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 24));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 38, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 48));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 48));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 60));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 60));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 72));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 38, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 72));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 38, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 84));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_ON, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 84));
		
		drummessage[j] = new ShortMessage();
		drummessage[j].setMessage(ShortMessage.NOTE_OFF, drumchannel, 35, velocity);
		track9.add(new MidiEvent(drummessage[j], e + 96));
		e = e + 96;
		}
		
		return sequence;
	}
}
