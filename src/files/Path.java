package files;

import java.io.File;

public class Path {
	public static final String defaultPath = "/home/kbok/jmax/";
	public static final String[] lookupPaths = { "/usr/share/jmax/", "/usr/local/share/jmax", "/usr/local/jmax", 
													".", "./jmax/" };
	
	public String locateFile(String filename)
	{
		File f = new File(defaultPath.concat(filename));
		if(f.exists()) return defaultPath.concat(filename);
		
		for(int i=0; i<lookupPaths.length; i++);
		return filename;
	}
}
