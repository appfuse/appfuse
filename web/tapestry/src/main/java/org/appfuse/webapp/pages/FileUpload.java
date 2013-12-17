package org.appfuse.webapp.pages;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.appfuse.Constants;
import org.appfuse.webapp.data.FileData;
import org.appfuse.webapp.services.SecurityContext;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * This class handles the uploading of a file and writing it to
 * the filesystem.  Eventually, it will also addChild support for persisting the
 * files information into the database.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @author Serge Eby
 * @version $Id: FileUpload.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class FileUpload {

    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @Inject
    private HttpServletRequest request;

    @Inject
    private AlertManager alertManager;

    @Inject
    private Context context;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ComponentResources resources;

    @InjectPage
    private FileDisplay fileDisplay;

    @Property
    private FileData fileData;


    @Log
    void onPrepare() {
        fileData = new FileData();
    }

    @Log
    void onValidate() {
        if (fileData.getFile() == null) {
            return;
        }
    }

    @Log
    Object onCancel() {
        return Home.class;
    }

    @DiscardAfter
    Object onSuccess() {

        File uploadPath = buildUploadPath();

        File copied = new File(uploadPath, fileData.getFileName());
        fileData.write(copied);

        // Populate fileData object
        String path = uploadPath.getAbsolutePath() + Constants.FILE_SEP + fileData.getFileName();
        String url =
                request.getContextPath() + "/resources/" +
                        securityContext.getUsername() + "/" + fileData.getFileName();


        fileData.setPath(path);
        fileData.setUrl(url);

        fileDisplay.setFileData(fileData);

        return fileDisplay;
    }


    Object onUploadException(FileUploadException exception) {
        logger.error(String.format("Upload exception: %s ", exception.getMessage()));
        alertManager.alert(Duration.TRANSIENT,
                           Severity.ERROR,
                           messages.get("maxLengthExceeded"));
        return this;
    }


    private File buildUploadPath() {

        // the directory to upload to
        String uploadDir = request.getServletContext().getRealPath("/resources");

        // The following seems to happen when running jetty:run
        if (uploadDir == null) {
            uploadDir = new File("src/main/webapp/resources").getAbsolutePath();
        }
        uploadDir += Constants.FILE_SEP + securityContext.getUsername() + Constants.FILE_SEP;

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        return dirPath;
    }

}
