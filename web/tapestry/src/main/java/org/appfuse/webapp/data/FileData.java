package org.appfuse.webapp.data;

import org.apache.tapestry5.upload.services.UploadedFile;

import java.io.File;
import java.io.Serializable;

/**
 * Wrapper class around the attributes used by the upload process
 *
 * @author Serge Eby
 * @version $Id: FileData.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class FileData implements Serializable {

    private UploadedFile file;
    private String friendlyName;
    private String path;
    private String url;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Utility methods
    public String getFileName() {
        return file != null ? file.getFileName() : null;
    }

    public Long getSize() {
        return file != null ? Long.valueOf(file.getSize()) : null;
    }

    public String getContentType() {
        return file != null ? file.getContentType() : null;
    }

    public void write(File another) {
        file.write(another);
    }


}
