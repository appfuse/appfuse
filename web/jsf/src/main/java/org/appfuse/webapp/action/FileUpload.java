package org.appfuse.webapp.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import org.appfuse.Constants;

public class FileUpload extends BasePage implements Serializable {
    private static final long serialVersionUID = 6932775516007291334L;
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
        String uploadDir = getServletContext().getRealPath("/resources") + "/" + request.getRemoteUser() + "/";

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        // APF-758: Fix for Internet Explorer's shortcomings
        String filename = file.getName();
        if (filename.indexOf("\\")!= -1) {
            int slash = filename.lastIndexOf("\\");
            if (slash != -1) {
                filename = filename.substring(slash + 1);
            }
            // Windows doesn't like /'s either
            int slash2 = filename.lastIndexOf("/");
            if (slash2 != -1) {
                filename = filename.substring(slash2 + 1);
            }
            // In case the name is C:foo.txt
            int slash3 = filename.lastIndexOf(":");
            if (slash3 != -1) {
                filename = filename.substring(slash3 + 1);
            }
        }

        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + filename);
        int bytesRead;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        request.setAttribute("friendlyName", name);
        request.setAttribute("fileName", filename);
        request.setAttribute("contentType", file.getContentType());
        request.setAttribute("size", file.getSize() + " bytes");
        request.setAttribute("location", dirPath.getAbsolutePath() + Constants.FILE_SEP + filename);

        String link = request.getContextPath() + "/resources" + "/" + request.getRemoteUser() + "/";
        request.setAttribute("link", link + filename);
        
        return "success";
    }
}
