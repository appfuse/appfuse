package org.appfuse.webapp.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.appfuse.Constants;
import org.appfuse.webapp.form.UploadForm;


/**
 * This class handles the uploading of a resume (or any file) and writing it to
 * the filesystem.  Eventually, it will also add support for persisting the
 * files information into the database.
 *
 * <p>
 * <a href="UploadAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * 
 * @struts.action name="uploadForm" path="/uploadFile" scope="request"
 *  validate="true" input="failure"
 *
 * @struts.action-forward name="failure" path="/WEB-INF/pages/uploadForm.jsp"
 * @struts.action-forward name="success" path="/WEB-INF/pages/uploadDisplay.jsp"
 */
public class UploadAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
      throws Exception {
        // Did the user click the cancel button?
        if (isCancelled(request)) {    
            request.removeAttribute(mapping.getAttribute());
            return (mapping.findForward("mainMenu"));
        }

        //this line is here for when the input page is upload-utf8.jsp,
        //it sets the correct character encoding for the response
        String encoding = request.getCharacterEncoding();

        if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
            response.setContentType("text/html; charset=utf-8");
        }

        UploadForm theForm = (UploadForm) form;

        //retrieve the name
        String name = theForm.getName();

        //retrieve the file representation
        FormFile file = theForm.getFile();

        //retrieve the file name
        String fileName = file.getFileName();

        //retrieve the content type
        String contentType = file.getContentType();

        //retrieve the file size
        String size = (file.getFileSize() + " bytes");

        String data = null;
        String location = null;

        // the directory to upload to
        String uploadDir =
            servlet.getServletContext().getRealPath("/resources") + "/"
            + request.getRemoteUser() + "/";

        //write the file to the file specified
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        location =
            "The file has been written to <br />\"" + dirPath.getAbsolutePath()
            + Constants.FILE_SEP + file.getFileName() + "\"";

        //close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        request.setAttribute("friendlyName", name);
        request.setAttribute("fileName", fileName);
        request.setAttribute("contentType", contentType);
        request.setAttribute("size", size);
        request.setAttribute("data", data);
        request.setAttribute("location", location);

        //destroy the temporary file created
        file.destroy();

        //return a forward to display.jsp
        return mapping.findForward("success");
    }
}
