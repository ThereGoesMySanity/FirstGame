package Audio;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.*;

public class AudioPlayer {
	private Clip clip;

	public AudioPlayer(String s){
		try{
			InputStream is = getClass().getResourceAsStream(s);
			InputStream bufferedIn = new BufferedInputStream(is);
			AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels()*2,
					baseFormat.getSampleRate(),
					false);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		}catch(Exception e){e.printStackTrace();}
	}
	public void play(){
		if(clip == null||clip.isRunning())return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	public void stop(){
		if(clip.isRunning())clip.stop();
	}
	public void close(){
		stop();
		clip.close();
	}
	public boolean isRunning(){
		return clip.isRunning();
	}
}






