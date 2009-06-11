package files;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Provides a way to find the resource folder and to access it in a easy way.
 * @author kbok
 */
public class Path {
	public static final String[] lookupPaths = { "/home/kbok/srswar/res/", "/home/kbok/srswar/res_moved/", "/usr/share/srswar/", "/usr/local/share/srswar/",  
												 "C:/srswar/"};
	public static String path;
	
	/**
	 * Returns the path of the resource file.
	 * @return the path of the file.
	 * @throws FileNotFoundException if the path cannot be found.
	 */
	public static String getPath() throws FileNotFoundException
	{
		if(path == null)
			locatePath();
		return path;
	}
	
	/**
	 * Locates the path of the resource folder.
	 * @throws FileNotFoundException if the path cannot be found.
	 */
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
