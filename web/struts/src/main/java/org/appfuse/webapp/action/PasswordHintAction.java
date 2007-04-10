package org.appfuse.webapp.action;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.webapp.util.RequestUtil;

/**
 * Action class to send password hints to registered users.
 *
 * <p>
 * <a href="PasswordHintAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class PasswordHintAction extends BaseAction {
    private static final long serialVersionUID = -4037514607101222025L;
    private String username;
    
    /**
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String execute() {
        List<String> args = new ArrayList<String>();
        
        // ensure that the username has been sent
        if (username == null) {
            log.warn("Username not specified, notifying user that it's a required field.");

            args.add(getText("user.username"));
            addActionError(getText("errors.required", args));
            return INPUT;
        }
        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint...");
        }
        
        // look up the user's information
        try {
            User user = userManager.getUserByUsername(username);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: " + user.getPasswordHint());
            msg.append("\n\nLogin at: " + RequestUtil.getAppURL(getRequest()));

            mailMessage.setTo(user.getEmail());
            String subject = '[' + getText("webapp.name") + "] " + getText("user.passwordHint");
            mailMessage.setSubject(subject);
            mailMessage.setText(msg.toString());
            mailEngine.send(mailMessage);
            
            args.add(username);
            args.add(user.getEmail());
            
            saveMessage(getText("login.passwordHint.sent", args));
            
        } catch (Exception e) {
            e.printStackTrace();
            // If exception is expected do not rethrow
            addActionError(getText("login.passwordHint.error", args));
            return INPUT;
        }

        return SUCCESS;
    }
}
