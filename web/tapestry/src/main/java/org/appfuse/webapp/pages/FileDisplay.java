package org.appfuse.webapp.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.appfuse.webapp.data.FileData;

/**
 * This class handles the uploading of a file and writing it to
 * the filesystem.  Eventually, it will also addChild support for persisting the
 * files information into the database.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @author Serge Eby
 * @version $Id: FileDisplay.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class FileDisplay {


    @Persist(PersistenceConstants.FLASH)
    @Property(write = false)
    private FileData fileData;


    public void setFileData(FileData fileData) {
        this.fileData = fileData;
    }

    Object onDone() {
        return Home.class;
    }

    Object onAnotherUpload() {
        return FileUpload.class;
    }

}
