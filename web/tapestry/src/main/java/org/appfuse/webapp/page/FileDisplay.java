package org.appfuse.webapp.page;

import org.apache.tapestry.request.IUploadFile;

/**
* This class handles the uploading of a file and writing it to
* the filesystem.  Eventually, it will also add support for persisting the
* files information into the database.
*
* <p>
* <a href="UploadAction.java.html"><i>View Source</i></a>
* </p>
*
* @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
*/
public abstract class FileDisplay extends BasePage {
    public abstract void setFile(IUploadFile file);
    public abstract IUploadFile getFile();
    
    public abstract void setName(String name);
    public abstract String getName();
    
    public abstract void setFilePath(String path);
    public abstract String getFilePath();
    
    public abstract void setUrl(String url);
    public abstract String getUrl();
}
