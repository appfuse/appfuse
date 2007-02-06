package org.appfuse.webapp.pages;

import org.apache.tapestry.request.IUploadFile;

/**
* This class handles the uploading of a file and writing it to
* the filesystem.  Eventually, it will also add support for persisting the
* files information into the database.
*
* @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
*/
public abstract class FileDisplay extends BasePage {
    public abstract void setFile(IUploadFile file);
    public abstract void setName(String name);
    public abstract void setFilePath(String path);
    public abstract void setUrl(String url);
}
