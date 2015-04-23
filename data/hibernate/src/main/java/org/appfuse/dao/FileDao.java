package org.appfuse.dao;

import org.appfuse.model.File;
import java.util.List;

/**
 * This interface defines the method signatures for the File upload/attachnment meta-data
 * DAO object.
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
     * @Todo - method needs analysis - incomplete!
     * This method needs to override the similar method in GenericDao. The implementation
     * will use @Transactional(readOnly = true) and thereby protect the file from update
     * or revision. The delete method may also be removed. Unsure at this time.
     * @param id
     * @return
     */
    public File getFile(Long id);

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
