package org.appfuse.webapp.action;

import java.io.IOException;

import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
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
public abstract class PasswordHint extends BasePage implements PageBeginRenderListener {
    public abstract UserManager getUserManager();
    public abstract MailEngine getMailEngine();
    public abstract SimpleMailMessage getMailMessage();
    
    public void pageBeginRender(PageEvent event) {
        try {
            execute(getRequest().getParameter("username"));
        } catch (IOException io) {
            throw new RuntimeException(io.getMessage(), io);
        }
    }
    
    public void execute(String username) throws IOException {        
        // ensure that the username has been sent
        if (username == null || "".equals(username)) {
            log.warn("Username not specified, notifying user that it's a required field.");
            getSession().setAttribute("error", getText("errors.required", getText("user.username")));
            getResponse().sendRedirect(getRequest().getContextPath());
            return;
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint for username: " + username);
        }
        
        // look up the user's ingetTextion
        try {
            User user = getUserManager().getUser(username);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: " + user.getPasswordHint());
            msg.append("\n\nLogin at: " + RequestUtil.getAppURL(getRequest()));

            SimpleMailMessage message = getMailMessage();
            message.setTo(user.getEmail());
            
            String subject = '[' + getText("webapp.name") + "] " + getText("user.passwordHint");
            message.setSubject(subject);
            message.setText(msg.toString());
            getMailEngine().send(message);
            
            getSession().setAttribute("message", getText("login.passwordHint.sent", new Object[] {username, user.getEmail()}));
        } catch (Exception e) {
            e.printStackTrace();
            // If exception is expected do not rethrow
            getSession().setAttribute("error", getText("login.passwordHint.error", username));
        }
        
        getResponse().sendRedirect(getRequest().getContextPath());
        return;
    }
}
