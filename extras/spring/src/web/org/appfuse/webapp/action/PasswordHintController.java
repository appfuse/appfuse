package org.appfuse.webapp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailSender;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.util.RequestUtil;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.RedirectView;


/**
 * Simple class to retrieve and send a password hint to users.
 *
 * <p>
 * <a href="PasswordHintController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class PasswordHintController implements Controller {
    private transient final Log log = LogFactory.getLog(PasswordHintController.class);
    private UserManager mgr = null;

    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        String userId = request.getParameter("username");
        RequestContext rc = new RequestContext(request);
        
        // ensure that the username has been sent
        if (userId == null) {
            log.warn("Username not specified, notifying user that it's a required field.");

            request.setAttribute("error",
                    rc.getMessage("errors.required",
                                  new Object[] {
                                      rc.getMessage("user.username")
                                  }));
            
            return new ModelAndView("login");
        }

        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint...");
        }

        // look up the user's information
        try {
            User user = (User) mgr.getUser(userId);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: " + user.getPasswordHint());
            msg.append("\n\nLogin at: " + RequestUtil.getAppURL(request));

            // From,to,cc,subject,content
            MailSender.sendTextMessage(Constants.DEFAULT_FROM,
                                       user.getEmail(), null,
                                       "Password Hint", msg.toString());

            saveMessage(request, rc.getMessage("login.passwordHint.sent",
                                 new Object[] {userId, user.getEmail()}));
        } catch (Exception e) {
            saveError(request, rc.getMessage("login.passwordHint.error",
                               new Object[] {userId}));
        }

        return new ModelAndView(new RedirectView(rc.getContextPath()));        
    }

    public void saveError(HttpServletRequest request, String error) {
        List errors = (List) request.getSession().getAttribute("errors");
    	if (errors == null) {
    		errors = new ArrayList();
        }
        errors.add(error);
        request.getSession().setAttribute("errors", errors);
    }

    // this method is also in BaseForm Controller
    public void saveMessage(HttpServletRequest request, String msg) {
        List messages = (List) request.getSession().getAttribute("messages");
    	if (messages == null) {
    		messages = new ArrayList();
        }
        messages.add(msg);
        request.getSession().setAttribute("messages", messages);
    }
}
