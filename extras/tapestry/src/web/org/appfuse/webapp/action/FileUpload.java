package org.appfuse.webapp.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.request.IUploadFile;
import org.appfuse.Constants;

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
public abstract class FileUpload extends BasePage {
    public abstract IUploadFile getFile();
    public abstract void setName(String name);
    public abstract String getName();
    
    public void cancel(IRequestCycle cycle) {
        if (log.isDebugEnabled()) {
            log.debug("entered 'cancel' method");
        }
        cycle.activate("mainMenu");
    }
    
    public void upload(IRequestCycle cycle) throws IOException {
        IUploadFile file = getFile();
        
        if (file == null) {
            return;
        }

        // write the file to the filesystem
        // the directory to upload to
        String uploadDir =
            getServletContext().getRealPath("/resources") + "/" +
            getRequest().getRemoteUser() + "/";

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getStream();

        //write the file to the file specified
        OutputStream bos =
            new FileOutputStream(uploadDir + file.getFileName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        //close the stream
        stream.close();

        // set the data for retrieval on next page
        FileDisplay next = (FileDisplay) cycle.getPage("showFile");
        next.setFile(file);
        next.setName(getName());
        next.setFilePath(dirPath.getAbsolutePath() + Constants.FILE_SEP +
                             file.getFileName());

        String url =
            getRequest().getContextPath() + "/resources" + "/" +
            getRequest().getRemoteUser() + "/";

        next.setUrl(url + file.getFileName());       

        cycle.activate(next);
    }
}
