package org.appfuse.webapp.action;

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


/**
 * Simple class to retrieve and send a password hint to users.
 *
 * <p>
 * <a href="PasswordHintController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2004/05/16 02:15:02 $
 */
public class PasswordHintController implements Controller {
    private static Log log = LogFactory.getLog(PasswordHintController.class);
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

            request.setAttribute("message",
                    rc.getMessage("login.passwordHint.sent",
                                  new Object[] {userId, user.getEmail()}));
        } catch (Exception e) {
            request.setAttribute("error",
                    rc.getMessage("login.passwordHint.error",
                                  new Object[] {userId}));
        }

        return new ModelAndView("login");        
    }
}
