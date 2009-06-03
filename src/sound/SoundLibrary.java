package sound;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

class SoundElement
{
	SoundElement(File f, String n){
		file = f; name = n;
	}
	
	File file;
	String name;
}

public class SoundLibrary {
	protected static SoundLibrary singleton;
	protected Vector<SoundElement> library;
	
	protected SoundLibrary()
	{
		library = new Vector<SoundElement>();
		
		String[] units = {"tank", "assault", "scout"};
		String[] sounds = {"attack", "start", "stop", "drive"};
		
		try {
			for(int i=0;i<units.length; i++)
			 for(int j=0;j<sounds.length; j++)
				loadStream(units[i].concat(".").concat(sounds[j]),
						new String("/home/kbok/jmax_res/vehicles/").concat(units[i])
						.concat("/").concat(sounds[j]).concat(".wav"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void checkSingleton()
	{
		if(singleton == null)
			singleton = new SoundLibrary();
	}
	
	protected void loadStream(String name, String url) throws UnsupportedAudioFileException, IOException
	{
		File soundFile = new File(url);
		library.add(new SoundElement(soundFile, name));
	}
	
	public static AudioInputStream getStreamByName(String name)
	{
		checkSingleton();
		
		for(int i=0; i<singleton.library.size(); i++)
			if(singleton.library.get(i).name.equals(name))
				try {
					return AudioSystem.getAudioInputStream(singleton.library.get(i).file);
				} catch (Exception e) {
					e.printStackTrace();
				}
		
		return null;
	}
	
	public static AudioInputStream getStreamByUnitAction(String unit, String action)
	{
		String name = new String(unit);
		name = name.concat(".");
		name = name.concat(action);
		
		return getStreamByName(name);
	}
	
	public static void load()
	{
		checkSingleton();
	}
}
