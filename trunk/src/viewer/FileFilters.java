/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package viewer;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author kbok
 */
public class FileFilters {

    public static class ResourceFileFilter extends FileFilter {

        @Override
        public boolean accept(File f) {
            if(f.isDirectory()) return true;
            return f.getName().endsWith(".RES") || f.getName().endsWith(".res");
        }

        @Override
        public String getDescription() {
            return "M.A.X. Resource files (.RES)";
        }
    }
}
