package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

//BGMの音量を調節するクラス
public class VelocityModulator {
	public static void main(String[] args) {
	}
	
	public static int setVelocity (int velo) throws InvalidMidiDataException, MidiUnavailableException{
		if(velo > 128){
			velo = 127;
		}else if(velo < 0){
			velo = 0;
		}else{
		}
		return velo;
	}
}