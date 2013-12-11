package org.appfuse.webapp.pages;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.UserExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.wicketstuff.annotation.mount.MountPath;

import javax.servlet.http.Cookie;

/**
 * Page for a new user signup.
 *
 * @author Marcin Zajączkowski, 2010-09-03
 */
@MountPath("signup")
public class Signup extends AbstractUserEdit {

    private final static Logger log = LoggerFactory.getLogger(Signup.class);

    private static final String SIGNUP_PROPERTY_PREFIX = "signup";

    @SpringBean
    private MailEngine mailEngine;

    public Signup() {
        super(NO_RESPONSE_PAGE, SIGNUP_PROPERTY_PREFIX, new Model<User>(new User()));
    }

    @Override
    protected void onInitialize() {
        //TODO: MZA: quite odd, before super
        setUser(new User());
        
        super.onInitialize();
    }

    //TODO: MZA: Maybe use a visitor pattern?

    @Override
    protected void onSaveButtonSubmit() {
        User user = prepareNewUser();
        user = saveUser(user);
        prepareAndSendNewUserEmail(user);
        getSession().info(createDefaultInfoNotificationMessage(new ResourceModel("user.registered")));
        setUserNameCookieAndSetResponsePage(user.getUsername());
    }

    private User prepareNewUser() {
        User user = getUser();
        user.setEnabled(true);
        user.addRole(getRoleManager().getRole(Constants.USER_ROLE));
        return user;
    }

    private User saveUser(User user) {
        try {
            user = getUserManager().saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor
            log.warn(ade.getMessage());
            throw new SecurityException("AccessDenied on saving new user", ade);
        } catch (UserExistsException e) {
            error(new StringResourceModel("errors.existing.user", this, null, new Object[] {
                    user.getUsername(), user.getEmail()}).getString());
            throw new RestartResponseException(getPage());
        }
        return user;
    }

    private void prepareAndSendNewUserEmail(User user) {
        SimpleMailMessage messageToSend = prepareMailMessage(user);
        sendMessage(messageToSend);
    }

    //TODO: MZA: Should be moved to business layer
    private SimpleMailMessage prepareMailMessage(User user) {
        log.debug("Preparing message for user '{}' with an account information", user.getUsername());
        SimpleMailMessage message = new SimpleMailMessage();    //serviceFacade.getMailMessage();
        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        StringBuilder msg = new StringBuilder();
        msg.append(getString("signup.email.message"));
        msg.append("\n\n").append(getString("user.username"));
        msg.append(": ").append(user.getUsername()).append("\n");
        msg.append(getString("user.password")).append(": ");
        msg.append(user.getPassword());
        msg.append("\n\nLogin at: ")
                .append(RequestCycle.get().getUrlRenderer().renderFullUrl(
                        Url.parse(urlFor(Login.class, null).toString())));
        message.setText(msg.toString());
        message.setSubject(getString("signup.email.subject"));

        return message;
    }

    private void sendMessage(SimpleMailMessage message) {
        try {
            mailEngine.send(message);
        } catch (MailException me) {
            log.error(me.getMostSpecificCause().getMessage(), me);
            getSession().warn(new NotificationMessage(new ResourceModel("errors.sending.email")));
        }
    }

    private void setUserNameCookieAndSetResponsePage(String userName) {
        ((WebResponse)getResponse()).addCookie(new Cookie("username", userName));
        setResponsePage(Login.class);
    }

    @Override
    protected void onDeleteButtonSubmit() {
        throw new IllegalStateException("Delete button should not be able to submit on signup");
    }

    @Override
    protected void onCancelButtonSubmit() {
        //MZA: getSignInPage would be better, but it has protected visibility modifier.
        //MZA: getHomePage should be ok - not authorized user should be redirected to login page
        setResponsePage(Login.class);
    }

    @Override
    protected boolean getDisplayRolesGroupVisibility() {
        return false;
    }

    @Override
    protected boolean getAccountSettingsGroupVisibility() {
        return false;
    }

    @Override
    protected boolean getDeleteButtonVisibility() {
        return false;
    }

    @Override
    protected boolean isCollapsedAddressTab() {
        return false;
    }
}
