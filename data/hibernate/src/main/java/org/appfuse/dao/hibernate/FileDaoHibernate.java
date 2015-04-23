package org.appfuse.dao.hibernate;

import org.appfuse.dao.FileDao;
import org.appfuse.model.File;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class implements the file attachment/upload DAO interface.
 *
 * @author <a href="mailto:david@citechnical.com">David L. Whitehurst</a>
 *         created: 4/23/15
 *         time: 12:36 AM
 * @version CHANGEME
 */
@Repository("fileDao")
public class FileDaoHibernate extends GenericDaoHibernate<File, Long> implements FileDao {
    /**
     * @Todo implement
     * Method returns a list of file objects
     *
     * @return
     */
    public List<File> getFiles() {
        return null;
    }

    /**
     * @Todo implement
     * Method saves a file and returns a file object with a newly generated primary key
     *
     * @param file
     * @return
     */
    public File saveFile(File file) {
        return null;
    }

    /**
     * @Todo implement
     * Method obtains the file object from the database using just the filename
     *
     * @param filename the name of the object to get
     * @return
     */
    public File getFileByFilename(String filename) {
        return null;
    }

    /**
     * @Todo implement
     * Method deletes the file object from the database
     *
     * @param file
     */
    public void deleteFile(File file) {

    }
}
