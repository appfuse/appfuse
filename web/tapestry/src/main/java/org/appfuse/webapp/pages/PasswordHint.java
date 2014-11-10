package org.appfuse.webapp.pages;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.services.EmailService;
import org.appfuse.webapp.util.RequestUtil;
import org.slf4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Managed Bean to send password hints to registered users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @author Serge Eby
 * @version $Id: PasswordHint.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class PasswordHint {
    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @Inject
    private UserManager userManager;

    @Inject
    private EmailService emailService;

    @Inject
    private AlertManager alertManager;

    @Inject
    private HttpServletRequest request;

    private String username;

    Object onActivate(EventContext ctx) {
        // ensure that the username has been set
        if (ctx == null || ctx.getCount() == 0) {
            logger.warn("Username not specified, notifying user that it's a required field.");
            alertManager.alert(Duration.TRANSIENT,
                    Severity.ERROR,
                    messages.format("errors.required", messages.get("user.username")));

            return Login.class;
        }

        // Expect username is the first item in the context
        int userIdx = 0;
        this.username = ctx.get(String.class, userIdx).trim();
        logger.debug("Processing Password Hint for username: " + username);

        // look up the user's information
        try {
            User user = userManager.getUserByUsername(username);

            StringBuilder msg = new StringBuilder();
            msg.append("Your password hint is: ").append(user.getPasswordHint());
            String subject = '[' + messages.get("webapp.name") + "] " + messages.get("user.passwordHint");

            emailService.send(user, subject, msg.toString(), RequestUtil.getAppURL(request), true);

            alertManager.alert(Duration.TRANSIENT,
                    Severity.SUCCESS,
                    messages.format("login.passwordHint.sent", username, user.getEmail()));
        } catch (UsernameNotFoundException e) {
            logger.warn(e.getMessage());
            // If exception is expected do not rethrow
            alertManager.error(messages.format("login.passwordHint.error", username));

        } catch (MailException me) {
            alertManager.error(me.getCause().getLocalizedMessage());
        }


        return Login.class;
    }


}
