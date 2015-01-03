package org.appfuse.webapp.server.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * Controller class to upload Files.
 * <p/>
 * <p>
 * <a href="FileUploadFormController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/fileupload*")
public class FileUploadController implements ServletContextAware {

    private MessageSourceAccessor messages;
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Autowired
    public void setMessages(MessageSource messageSource) {
        messages = new MessageSourceAccessor(messageSource);
    }

    /**
     *
     */
    public static class FileUpload {
        private String name;
        private byte[] file;

        /**
         * @return Returns the name.
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            The name to set.
         */
        public void setName(String name) {
            this.name = name;
        }

        public void setFile(byte[] file) {
            this.file = file;
        }

        public byte[] getFile() {
            return file;
        }
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(method = RequestMethod.POST)
    public void onSubmit(@ModelAttribute FileUpload fileUpload, BindingResult errors, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        JSONObject jsonObject = new JSONObject();

        if (StringUtils.isBlank(fileUpload.getName())) {
            Object[] args = new Object[] { messages.getMessage("uploadForm.name", request.getLocale()) };
            errors.rejectValue("name", "errors.required", args, "Name");
        }
        // validate a file was entered
        if (fileUpload.getFile().length == 0) {
            Object[] args = new Object[] { messages.getMessage("uploadForm.file", request.getLocale()) };
            errors.rejectValue("file", "errors.required", args, "File");
        }
        if (errors.hasErrors()) {
            List<String> errorMessages = new ArrayList<String>();
            for (ObjectError error : errors.getAllErrors()) {
                errorMessages.add(messages.getMessage(error));
            }
            jsonObject.put("errorMessages", errorMessages);
            sendResponse(response, jsonObject);
            return;
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

        // the directory to upload to
        String uploadDir = servletContext.getRealPath("/resources") + "/" + request.getRemoteUser() + "/";

        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        // retrieve the file data
        InputStream stream = file.getInputStream();

        // write the file to the file specified
        OutputStream bos = new FileOutputStream(uploadDir + file.getOriginalFilename());
        int bytesRead;
        byte[] buffer = new byte[8192];

        while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        bos.close();

        // close the stream
        stream.close();

        // place the data into the request for retrieval on next page
        jsonObject.put("name", fileUpload.getName());
        jsonObject.put("fileName", file.getOriginalFilename());
        jsonObject.put("contentType", file.getContentType());
        jsonObject.put("size", file.getSize() + " bytes");
        jsonObject.put("location", dirPath.getAbsolutePath() + Constants.FILE_SEP + file.getOriginalFilename());

        String link = request.getContextPath() + "/resources" + "/" + request.getRemoteUser() + "/";
        jsonObject.put("link", link + file.getOriginalFilename());

        sendResponse(response, jsonObject);
    }

    private void sendResponse(HttpServletResponse response, JSONObject jsonObject) throws IOException {
        response.setContentType("text/html");
        response.getWriter().write(jsonObject.toString());
        response.getWriter().flush();
    }
}
