package files;

import java.io.File;
import java.io.FileNotFoundException;

public class Path {
	public static final String[] lookupPaths = { "/home/kbok/srswar/res/", "/home/kbok/srswar/res_moved/", "/usr/share/srswar/", "/usr/local/share/srswar/",  
												 };
	public static String path;
	
	public static String getPath() throws FileNotFoundException
	{
		if(path == null)
			locatePath();
		return path;
	}
	
	public static void locatePath() throws FileNotFoundException
	{	
		for(int i=0; i<lookupPaths.length; i++)
		{
			File f = new File(lookupPaths[i].concat("srswar.manifest"));
			if(f.exists()){
				path = lookupPaths[i];
				return;
			}
		}
		
		throw new FileNotFoundException("Unable to find Serious War path");
	}
}
