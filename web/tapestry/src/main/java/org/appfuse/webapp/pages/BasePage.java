package org.appfuse.webapp.pages;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;
import org.appfuse.Constants;
import org.appfuse.webapp.util.MessageUtil;
import org.slf4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * Base page containing common functionalities
 *
 * @author Serge Eby
 * @version $Id: BasePage.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class BasePage {

    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @Persist(PersistenceConstants.FLASH)
    private String message;

    @Persist(PersistenceConstants.FLASH)
    private String type;

    @Inject
    private RequestGlobals globals;

    @Inject
    private Request request;

    @Inject
    private Context context;

    protected void addError(Form form, Field field, String message, boolean flag, Object... args) {
        if (field == null || form == null) {
            return;
        }
        String errorMessage = buildMessage(message, flag, args);
        setMessage(errorMessage);
        form.recordError(field, errorMessage);
        setType("error");
    }

    protected void addError(String message, boolean flag, Object... args) {
        setMessage(buildMessage(message, flag, args));
        setType("error");
    }

    public void addInfo(String message, boolean flag, Object... args) {
        setMessage(buildMessage(message, flag, args));
        setType("success");
    }


    private String buildMessage(String key, boolean flag, Object... args) {
        return (flag ? getMessageText(key, args) : String.format(key, args));
    }

    // TODO: Direct access to servlet API should be avoided whenever possible
    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     *
     * @return the user's populated form from the session
     */
    protected Map getConfiguration() {
        Map config = (HashMap) context.getAttribute(Constants.CONFIG);

        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }

        return config;
    }

    public HttpServletRequest getRequest() {
        return globals.getHTTPServletRequest();
    }

    public HttpServletResponse getResponse() {
        return globals.getHTTPServletResponse();
    }

    /**
     * Convenience method for unit tests.
     *
     * @return boolean
     */
    public boolean hasErrors() {
        return (request.getSession(true).getAttribute("errors") != null);
    }

    public ServletContext getServletContext() {
        return getRequest().getSession().getServletContext();
    }

    protected Session getSession() {
        return request.getSession(true);
    }

    // Message Catalog Utilities
    protected String getText(String key) {
        return getMessages().get(key);
    }

    protected String getText(String key, Object arg) {
        if (arg == null) {
            return getText(key);
        }

        if (arg instanceof String) {
            return MessageFormat.format(getMessages().get(key), arg);
            // return getMessages().format(key, arg);
        } else if (arg instanceof Object[]) {
            return MessageFormat.format(getMessages().get(key), (Object[]) arg);
        } else {
            logger.error("argument '" + arg + "' not String or Object[]");
            return "";
        }
    }

    protected String getMessageText(String key, Object... args) {
        if (args == null) {
            return getMessages().get(key);
        }

        String msg = MessageUtil.convert(messages.get(key));
        return String.format(msg, args);
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
