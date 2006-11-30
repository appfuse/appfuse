package org.appfuse.utility;

import java.io.File;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public class FileUtility {

    public static boolean deleteTargetDirectories() {
        boolean result = true;
        // delete all production target directories
        String[] dirs = {
            "/home/piratepete/Java/appfuse/data/common/target",
            "/home/piratepete/Java/appfuse/data/hibernate/target",
            "/home/piratepete/Java/appfuse/data/ibatis/target",
            "/home/piratepete/Java/appfuse/service/target",
            "/home/piratepete/Java/appfuse/web/common/target",
            "/home/piratepete/Java/appfuse/web/jsf/target",
            "/home/piratepete/Java/appfuse/web/spring/target",
            "/home/piratepete/Java/appfuse/web/struts/target",
            "/home/piratepete/Java/appfuse/web/tapestry/target"
        };

        int size = dirs.length;
        for (int i=0; i<size; i++) {
            File tmpFile = new File(dirs[i]);
            result = deleteDirectory(tmpFile);
            if (!result) {
                break;
            }
        }

        return result;
        
    }


    private static boolean deleteDirectory(File dir) {
        boolean result = false;

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDirectory(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();

    }
}
