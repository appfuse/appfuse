package org.appfuse.webapp.pages;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.webapp.components.UserForm;
import org.appfuse.webapp.services.ServiceFacade;
import org.appfuse.webapp.util.RequestUtil;
import org.slf4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Self-registration page for new users
 *
 * @author Serge Eby
 * @version $Id: Signup.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class Signup extends BasePage {
    @Inject
    private Logger logger;

    @Inject
    private ServiceFacade serviceFacade;

    @Property
    @Persist
    private User user;

    @Inject
    private Request request;

    @Inject
    private Response response;

    @Inject
    private Messages messages;

    @Component(id = "signup")
    private UserForm form;

    @Property
    private Boolean cookieLogin;

    void beginRender() {
        if (user == null) {
            user = new User();
        }
    }

    // ~ Event Handlers

    Object onCancel() {
        if (logger.isDebugEnabled()) {
            logger.debug("entered cancel method");
        }
        return Login.class;
    }

    void onValidateForm() {
        // make sure the password fields match
        if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {
            addError(form.getForm(), form.getConfirmPasswordField(),
                    "errors.twofields", true,
                    getMessageText("user.confirmPassword"),
                    getMessageText("user.password"));
        }
    }

    Object onSuccess() throws IOException {
        logger.debug("entered save method");

        // Enable user;
        user.setEnabled(true);

        // Set the default user role on this new user
        user.addRole(serviceFacade.getRoleManager().getRole(Constants.USER_ROLE));

        try {
            user = serviceFacade.getUserManager().saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor
            // userManagerSecurity
            logger.warn(ade.getMessage());
            getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null; // FIXME
        } catch (UserExistsException e) {
            // addError("usernameField",
            // getMessages().format("errors.existing.user", user.getUsername(),
            // user.getEmail()), ValidationConstraint.CONSISTENCY);
            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return null; // FIXME
        }

        getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // log user in automatically
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getConfirmPassword(), user
                        .getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Send user an e-mail
        if (logger.isDebugEnabled()) {
            logger.debug("Sending user '" + user.getUsername()
                    + "' an account information e-mail");
        }

        SimpleMailMessage message = serviceFacade.getMailMessage();
        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        StringBuffer msg = new StringBuffer();
        msg.append(getText("signup.email.message"));
        msg.append("\n\n").append(getText("user.username"));
        msg.append(": ").append(user.getUsername()).append("\n");
        msg.append(getText("user.password")).append(": ");
        msg.append(user.getPassword());
        msg.append("\n\nLogin at: ")
                .append(RequestUtil.getAppURL(getRequest()));
        message.setText(msg.toString());
        message.setSubject(getText("signup.email.subject"));

        try {
            serviceFacade.getMailEngine().send(message);
        } catch (MailException me) {
            getSession().setAttribute("error",
                    me.getMostSpecificCause().getMessage());
        }

        getSession().setAttribute("message", getText("user.registered"));
        if (getRequest() != null) { // needed for testing
            response.sendRedirect(getRequest().getContextPath());
        }
        return null;
    }
}
