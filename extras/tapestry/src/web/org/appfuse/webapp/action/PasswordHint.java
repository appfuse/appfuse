package org.appfuse.webapp.action;

import java.io.IOException;

import org.apache.tapestry.IRequestCycle;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.mail.SimpleMailMessage;

/**
 * Managed Bean to send password hints to registered users.
 *
 * <p>
 * <a href="PasswordHint.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public abstract class PasswordHint extends BasePage {
    
    public abstract UserManager getUserManager();
    public abstract void setUserManager(UserManager userManager);
    public abstract MailEngine getMailEngine();
    public abstract void setMailEngine(MailEngine mailEngine);
    public abstract SimpleMailMessage getMailMessage();
    public abstract void setMailMessage(SimpleMailMessage message);
    
    public void execute(IRequestCycle cycle) throws IOException {
        String username = getRequest().getParameter("username");
        
        // ensure that the username has been sent
        if (username == null || "".equals(username)) {
            log.warn("Username not specified, notifying user that it's a required field.");
            getSession().setAttribute("error", format("errors.required", getMessage("user.username")));
            getResponse().sendRedirect(getRequest().getContextPath());
            return;
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint for username: " + username);
        }
        
        // look up the user's information
        try {
            User user = getUserManager().getUser(username);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: " + user.getPasswordHint());
            msg.append("\n\nLogin at: " + RequestUtil.getAppURL(getRequest()));

            SimpleMailMessage message = getMailMessage();
            message.setTo(user.getEmail());
            
            String subject = getMessage("webapp.prefix") + getMessage("user.passwordHint");
            message.setSubject("foo");
            message.setText(msg.toString());
            getMailEngine().send(message);
            
            getSession().setAttribute("message", format("login.passwordHint.sent", username, user.getEmail()));
        } catch (Exception e) {
            e.printStackTrace();
            // If exception is expected do not rethrow
            getSession().setAttribute("error", format("login.passwordHint.error", username));
        }
        
        getResponse().sendRedirect(getRequest().getContextPath());
        return;
    }
}
