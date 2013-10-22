package org.appfuse.webapp.action;

import org.appfuse.model.User;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Managed Bean to send password hints to registered users.
 *
 * <p>
 * <a href="PasswordHint.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class PasswordHint extends BasePage {
    private String username;
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String execute() {
        getFacesContext().getViewRoot().setViewId("/passwordHint.xhtml");

        // ensure that the username has been sent
        if (username == null || "".equals(username)) {
            log.warn("Username not specified, notifying user that it's a required field.");

            addError("errors.required", getText("user.username"));
            return null;
        } else if (username.endsWith(".xhtml")) {
            username = username.substring(0, username.indexOf(".xhtml"));
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint...");
        }
        
        // look up the user's information
        try {
            User user = userManager.getUserByUsername(username);

            StringBuilder msg = new StringBuilder();
            msg.append("Your password hint is: ").append(user.getPasswordHint());
            msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(getRequest()));

            message.setTo(user.getEmail());
            String subject = '[' + getText("webapp.name") + "] " + getText("user.passwordHint");
            message.setSubject(subject);
            message.setText(msg.toString());
            mailEngine.send(message);
            
            addMessage("login.passwordHint.sent", 
                       new Object[] { username, user.getEmail() });
            
        } catch (UsernameNotFoundException e) {
            log.warn(e.getMessage());
            // If exception is expected do not rethrow
            addError("login.passwordHint.error", username);
        } catch (MailException me) {
            addError(me.getCause().getLocalizedMessage());
        }

        return "success";
    }
}
