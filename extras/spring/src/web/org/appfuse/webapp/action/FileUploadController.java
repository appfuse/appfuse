package org.appfuse.webapp.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * Controller class to upload Files.
 *
 * <p>
 * <a href="FileUploadFormController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/05/04 06:09:57 $
 */
public class FileUploadController extends BaseFormController {
    private static Log log = LogFactory.getLog(UserFormController.class);

    public ModelAndView processFormSubmission(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object command,
                                              BindException errors)
    throws Exception {
        
        if (request.getParameter("cancel") != null) {
            return new ModelAndView(new RedirectView("mainMenu.html"));
        }
        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        
    	FileUpload fileUpload = (FileUpload) command;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
        
        // the directory to upload to
        String uploadDir =
            getServletContext().getRealPath("/resources") + "/" +
            request.getRemoteUser() + "/";

        String link =
            request.getContextPath() + "/resources" + "/" +
            request.getRemoteUser() + "/";

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        //retrieve the file data
        InputStream stream = file.getInputStream();

        //write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + file.getOriginalFilename());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();
        //close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        request.setAttribute("friendlyName", fileUpload.getName());
        request.setAttribute("fileName", file.getOriginalFilename());
        request.setAttribute("contentType", file.getContentType());
        request.setAttribute("size", file.getSize() + " bytes");
        request.setAttribute("location", dirPath.getAbsolutePath()
                + Constants.FILE_SEP + file.getOriginalFilename());
        request.setAttribute("link", link + file.getOriginalFilename());

        return new ModelAndView(getSuccessView());
    }
}
