package org.appfuse.utility;

import java.io.*;

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

    /**
     * Work method that deletes any build directories that linger in the
     * AppFuse application.  This method can be modified to suit the project
     * as AppFuse target directories change.
     *
     * @return
     */
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

    /**
     * Deletes a directory and it's contents
     * @param dir
     * @return
     */
    public static boolean deleteDirectory(File dir) {
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

    /**
     * Copies a source directory to a destination directory.  If the directories
     * are files, the source file will be copied to the destination file.
     */
    public static void copyDirectory(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] children = source.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(source, children[i]),
                                     new File(destination, children[i]));
            }
        } else {
            copyFile(source, destination);
        }
    }

     /**
      *  Copies a source file to a destination file by copying 1024 bytes
      *  at a time until there are no more bytes.
      */
     public static void copyFile(File source, File destination) throws IOException {
        // create in and out streams
        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(destination);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Creates a directory by pathname or relative to execution
     * @param name
     */
    public static void createDirectory(String name) {
        boolean success = (new File(name)).mkdirs();

        if (!success) {
            System.err.println("ERROR: directory creation failed.");
        }
    }

    /**
     * Lists files (children) in Directory (File)
     * @param name
     */
    public static void listFilesInDirectory(String name) {
        File dir = new File(name);

        String[] children = dir.list();
        if (children == null) {
            System.err.println("Directory or file does not exist");
        } else {
            for (int i=0; i<children.length; i++) {
                String filename = children[i];
                System.out.println(filename);
            }
        }
    }
}
