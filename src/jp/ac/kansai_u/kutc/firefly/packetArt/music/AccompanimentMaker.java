package jp.ac.kansai_u.kutc.firefly.packetArt.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class AccompanimentMaker {
	public static void makeCheerfulAccompaniment(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		setCheerfulBass(sequence, length, velocity);
		setCheerfulGuitar(sequence, length, velocity);
		setDrums(sequence, length, velocity);
	}
	
	public static void makeGloomyAccompaniment(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		setGloomyBass(sequence, length, velocity);
		setGloomyGuitar(sequence, length, velocity);
		setDrums(sequence, length, velocity);
	}
	
	private static void setCheerfulBass(Sequence sequence, int length, int velocity) throws InvalidMidiDataException {
		Track track1 = sequence.createTrack();
		int channel = 1;
		int instrument = 33;
		int[] scale = ScaleMaker.setCheerfulBassScale();
		String[] code = CodeMaker.setCheerfulCode(length);
		int komari = code.length;
		int haruka = 0;
		int kanata = 0;
		int yuiko = 0;
		int i = 0;
		
		ShortMessage[] message = new ShortMessage[(komari * 4) + 1];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track1.add(new MidiEvent(message[0], 0));
		
		for(i = 0; i < komari; i++){
			if("C".equals(code[i])){
				haruka = 0;
				kanata = 2;
			}else if("F".equals(code[i])){
				haruka = 3;
				kanata = 2;
			}else if("G7".equals(code[i])){
				haruka = 4;
				kanata = 4;
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
	
	private static void setCheerfulGuitar(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		Track track2 = sequence.createTrack();
		int channel = 2;
		int instrument = 24;
		int[] scale = ScaleMaker.setCheerfulGuitarScale();
		String[] code = CodeMaker.setCheerfulCode(length);
		int komari = code.length;
		int haruka = 0;
		int kanata = 2;
		int yuiko = 0;
		int i = 0;
		
		ShortMessage[] message = new ShortMessage[(komari * 4) + 1];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track2.add(new MidiEvent(message[0], 0));
		
		for(i = 0; i < komari; i++){
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
	
	private static void setGloomyBass(Sequence sequence, int length, int velocity) throws InvalidMidiDataException {
		Track track1 = sequence.createTrack();
		int channel = 1;
		int instrument = 33;
		int[] scale = ScaleMaker.setGloomyBassScale();
		String[] code = CodeMaker.setGloomyCode(length);
		int komari = code.length;
		int haruka = 0;
		int kanata = 0;
		int yuiko = 0;
		int i = 0;
		
		ShortMessage[] message = new ShortMessage[(komari * 4) + 1];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track1.add(new MidiEvent(message[0], 0));
		
		for(i = 0; i < komari; i++){
			if("Em".equals(code[i])){
				haruka = 0;
				kanata = 2;
			}else if("Am".equals(code[i])){
				haruka = 3;
				kanata = 2;
			}else if("B7".equals(code[i])){
				haruka = 4;
				kanata = 4;
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
		int i = 0;
		
		ShortMessage[] message = new ShortMessage[(komari * 4) + 1];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track2.add(new MidiEvent(message[0], 0));
		
		for(i = 0; i < komari; i++){
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
	
	
	private static void setDrums(Sequence sequence, int length, int velocity) throws InvalidMidiDataException{
		Track track9 = sequence.createTrack();
		int channel = 9;
		int instrument = 0;
		String[] code = CodeMaker.setCheerfulCode(length);
		int komari = code.length;
		int riki = 0;
		int kyosuke = 0;
		
		ShortMessage[] message = new ShortMessage[(komari * 4) + 1];
		message[0] = new ShortMessage();
		message[0].setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		track9.add(new MidiEvent(message[0], 0));
		
		//Crush Symbal and Hi-hat Symbal
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
			
			//Snare and Bassdrum
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

