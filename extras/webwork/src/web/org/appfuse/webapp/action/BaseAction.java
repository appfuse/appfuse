package org.appfuse.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;
import org.springframework.mail.SimpleMailMessage;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionSupport;


/**
 * Implementation of <strong>ActionSupport</strong> that contains 
 * convenience methods for subclasses.  For example, getting the current
 * user and saving messages/errors. This class is intended to
 * be a base class for all Action classes.
 *
 * <p>
 * <a href="BaseAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseAction extends ActionSupport {
    public static final String CANCEL = "cancel";
    protected transient final Log log = LogFactory.getLog(getClass());
    protected UserManager userManager = null;
    protected String from = null;
    protected String cancel = null;
    protected String delete = null;
    protected String save = null;
    protected MailEngine mailEngine = null;
    protected SimpleMailMessage message = null;
    protected String templateName = null; 

    public void saveMessage(String msg) {
        List messages = (List) getRequest().getSession().getAttribute("messages");
        if (messages == null) {
            messages = new ArrayList();
        }
        messages.add(msg);
        getRequest().getSession().setAttribute("messages", messages);
    }
    
    /**
     * Convenience method to get the user object from the session
     *
     * @param request the current request
     * @return the user's populated object from the session
     */
    protected User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(Constants.USER_KEY);
    }
    
    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     *
     * @return the user's populated form from the session
     */
    public Map getConfiguration() {
        Map config = (HashMap) getRequest().getSession().getServletContext()
                               .getAttribute(Constants.CONFIG);
        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }
        return config;
    }
    
    /**
     * Convenience method to get the request
     * @return current request
     */
    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();  
    }
    
    /**
     * Convenience method to get the session
     */
    public HttpSession getSession() {
    	return getRequest().getSession();
    }
    
    protected void sendUserMessage(User user, String msg, String url) {
        if (log.isDebugEnabled()) {
            log.debug("sending e-mail to user [" + user.getEmail() + "]...");
        }

        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        Map model = new HashMap();
        model.put("user", user);
        // TODO: figure out how to get bundle specified in webwork.properties
        // model.put("bundle", getTexts());
        model.put("message", msg);
        model.put("applicationURL", url);
        mailEngine.sendMessage(message, templateName, model);   
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }
    
    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }
    
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    
    /**
     * Convenience method for setting a "from" parameter to indicate the last
     * page.
     * @param from
     */
	public void setFrom(String from) {
		this.from = from;
	}
    
    public void setCancel(String cancel) {
    	this.cancel = cancel;
    }
    
    public void setDelete(String delete) {
        this.delete = delete;
    }

    public void setSave(String save) {
        this.save = save;
    }
}