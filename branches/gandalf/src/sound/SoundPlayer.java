package sound;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class SoundPlayer implements Runnable{
	protected AudioInputStream sound;
	protected boolean mustStop = false;
	protected SourceDataLine auline;
	
	public void run() {
		while(true) try
		{
			if(sound != null) 
				playSound();
			
			Thread.sleep(10);
		} 
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	private void playSound() throws LineUnavailableException, IOException
	{
		AudioFormat format = sound.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		auline = (SourceDataLine) AudioSystem.getLine(info);
		auline.open(format);
		auline.start();
		
		int nBytesRead = 0;
		byte[] abData = new byte[524288];
 
		while (nBytesRead != -1 && !mustStop) {
			nBytesRead = sound.read(abData, 0, abData.length);
			if (nBytesRead >= 0)
				auline.write(abData, 0, nBytesRead);
		}
		
		auline.drain();
		auline.close();

		mustStop = false;
		sound = null;
	}
	
	public void play(AudioInputStream sound)
	{
		//this.sound = sound;
	}
	
	public void stop()
	{
		//auline.stop();
		mustStop = true;
	}
	
	public boolean playing()
	{
		return sound != null;
	}

	public void enQueuePlay(AudioInputStream streamByUnitAction) {
		// TODO Auto-generated method stub
		
	}
}
