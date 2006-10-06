package org.appfuse.webapp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.appfuse.Constants;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ValidationAware;

public class FileUploadAction extends BaseAction implements ValidationAware {
    private static final long serialVersionUID = -9208910183310010569L;
    private File file;
    private String fileContentType;
    private String fileFileName;
    private String name;

    public String execute() throws Exception {
        if (this.cancel != null) {
            return "cancel";
        }
        
        if (file == null || file.length() > 2097152) {
            addActionError(getText("maxLengthExceeded"));
            return INPUT;
        }

        // the directory to upload to
        String uploadDir =
            ServletActionContext.getServletContext().getRealPath("/resources") +
            "/" + getRequest().getRemoteUser() + "/";

        // write the file to the file specified
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = new FileInputStream(file);

        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + fileFileName);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();        
        stream.close();

        
        // place the data into the request for retrieval on next page
        getRequest().setAttribute("location", dirPath.getAbsolutePath()
                + Constants.FILE_SEP + fileFileName);
        
        String link =
            getRequest().getContextPath() + "/resources" + "/" +
            getRequest().getRemoteUser() + "/";
        
        getRequest().setAttribute("link", link + fileFileName);
        
        return SUCCESS;
    }

    public String start() {
        return INPUT;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }
}
