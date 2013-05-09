package jp.ac.kansai_u.kutc.firefly.packetArt.music;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * 音量の例外処理を行うクラスです。
 * @author Lisa
 *
 */
public class VelocityModulator {
	
	/**
	 * 音量の例外処理を行うメソッドです。
	 * Midiの音量の下限と上限は0～127なので
	 * それを超えたものは強制的に数値を変更します。
	 *
	 * @param velo
	 * @return velo
	 * @throws InvalidMidiDataException
	 * @throws MidiUnavailableException
	 */
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