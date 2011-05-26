package org.appfuse.webapp.data;

import org.apache.tapestry5.upload.services.UploadedFile;

/**
 * Wrapper class around the attributes used by the updload process
 *
 * @author Serge Eby
 * @version $Id: FileData.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class FileData {

    private UploadedFile file;
    private String name;
    private String path;
    private String url;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
