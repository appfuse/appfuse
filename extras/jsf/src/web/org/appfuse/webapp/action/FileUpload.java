package org.appfuse.webapp.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.myfaces.custom.fileupload.UploadedFile;

import org.appfuse.Constants;

public class FileUpload extends BasePage implements Serializable {
    private UploadedFile file;
    private String name;

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

    public String upload() throws IOException {
        HttpServletRequest request = getRequest();
        
        // write the file to the filesystem
        // the directory to upload to
        String uploadDir =
            getServletContext().getRealPath("/resources") + "/" +
            request.getRemoteUser() + "/";

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        OutputStream bos =
            new FileOutputStream(uploadDir + file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        request.setAttribute("friendlyName", name);
        request.setAttribute("fileName", file.getName());
        request.setAttribute("contentType", file.getContentType());
        request.setAttribute("size", file.getSize() + " bytes");
        request.setAttribute("location",
                             dirPath.getAbsolutePath() + Constants.FILE_SEP +
                             file.getName());

        String link =
            request.getContextPath() + "/resources" + "/" +
            request.getRemoteUser() + "/";

        request.setAttribute("link", link + file.getName());
        
        return "success";
    }
}
