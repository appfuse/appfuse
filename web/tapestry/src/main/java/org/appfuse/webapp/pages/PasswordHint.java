package org.appfuse.webapp.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.appfuse.model.User;
import org.appfuse.webapp.services.ServiceFacade;
import org.appfuse.webapp.util.RequestUtil;
import org.slf4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.userdetails.UsernameNotFoundException;

import java.io.IOException;

/**
 * Managed Bean to send password hints to registered users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @author Serge Eby
 * @version $Id: PasswordHint.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class PasswordHint extends BasePage {

    @Inject
    private Logger logger;

    @Inject
    private ServiceFacade serviceFacade;

    @Inject
    private Request request;

    @Inject
    private Response response;

    @Inject
    private ComponentResources resources;

    private String username;

    void beginRender() {
    }

    void onActivate(String username) throws IOException {
        // ensure that the username has been sent
        if (username == null || "".equals(username)) {
            logger.warn("Username not specified, notifying user that it's a required field.");
            getSession().setAttribute("error", getText("errors.required", getText("user.username")));
            response.sendRedirect(request.getContextPath());
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Processing Password Hint for username: " + username);
        }

        // look up the user's information
        try {
            User user = serviceFacade.getUserManager().getUserByUsername(username);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: ").append(user.getPasswordHint());
            msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(getRequest()));

            SimpleMailMessage message = serviceFacade.getMailMessage();
            message.setTo(user.getEmail());

            String subject = '[' + getText("webapp.name") + "] " + getText("user.passwordHint");
            message.setSubject(subject);
            message.setText(msg.toString());
            serviceFacade.getMailEngine().send(message);

            getSession().setAttribute("message", getText("login.passwordHint.sent", new Object[]{username, user.getEmail()}));
        } catch (UsernameNotFoundException e) {
            logger.warn(e.getMessage());
            // If exception is expected do not rethrow
            getSession().setAttribute("error", getText("login.passwordHint.error", username));
        } catch (MailException me) {
            getSession().setAttribute("error", me.getCause().getLocalizedMessage());
        }

        response.sendRedirect(request.getContextPath());
        return;
    }

    String onPassivate() {
        return username;
    }
}
