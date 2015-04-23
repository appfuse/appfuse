package org.appfuse.dao;

import org.appfuse.model.File;
import java.util.List;

/**
 * Class description goes here ...
 *
 * @author <a href="mailto:david@citechnical.com">David L. Whitehurst</a>
 *         created: 4/22/15
 *         time: 11:56 PM
 * @version CHANGEME
 */

public interface FileDao extends GenericDao<File, Long>{

    /**
     * Method returns a list of file objects
     * @return
     */
    public List<File> getFiles();

    /**
     * Method saves a file and returns a file object with a newly generated primary key
     * @param file
     * @return
     */
    public File saveFile(File file);

    /**
     * Method obtains the file object from the database using just the filename
     * @param filename the name of the object to get
     * @return
     */
    public File getFileByFilename(String filename);

    /**
     * Method deletes the file object from the database
     * @param file
     */
    public void deleteFile(File file);
}
