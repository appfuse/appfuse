package org.appfuse.webapp.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.slf4j.Logger;

/**
 * This class handles the uploading of a file and writing it to
 * the filesystem.  Eventually, it will also add support for persisting the
 * files information into the database.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @author Serge Eby
 * @version $Id: FileDisplay.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class FileDisplay extends BasePage {

    @Inject
    private Logger logger;

    @Property
    @Persist
    private String name;

    @Property
    @Persist
    private String fileName;

    @Property
    @Persist
    private String contentType;

    @Property
    @Persist
    private long size;

    @Property
    @Persist
    private String path;

    @Property
    @Persist
    private String url;

    Object initialize(UploadedFile file, String name, String path, String url) {
        this.name = name;
        this.fileName = file.getFileName();
        this.contentType = file.getContentType();
        this.size = file.getSize();
        this.path = path;
        this.url = url;
        return this;
    }

    Object onDone() {
        return MainMenu.class;
    }

    Object onAnotherUpload() {
        return FileUpload.class;
    }

}
