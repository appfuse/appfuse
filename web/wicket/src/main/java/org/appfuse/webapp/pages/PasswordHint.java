package org.appfuse.webapp.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.AbstractWebPage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.wicketstuff.annotation.mount.MountPath;
import org.wicketstuff.annotation.strategy.MountIndexedParam;

/**
 * Service page used for sending password hint.
 *
 * @author Marcin Zajączkowski, 2011-02-11
 */
@MountPath(path = "passwordHint")
@MountIndexedParam
public class PasswordHint extends AbstractWebPage {

    @SpringBean(name = "userManager")
    private UserManager userManager;

    private final PageParameters parameters;

    public PasswordHint(PageParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

//        String username = parameters.getString("username");
        String username = parameters.getString("0");
        log.debug("username {}", username);

        if (username == null || "".equals(username)) {
            log.warn("Username not specified, notifying user that it's a required field.");
            getSession().error(getString("errors.required"));
//            error(new StringResourceModel("errors.required", this, model, new Object[] {
//                        new PropertyModel(model, "username")}).getString());
            return;
        }

        log.debug("Processing Password Hint for username: {}", username);

        // look up the user's information
        try {
            User user = userManager.getUserByUsername(username);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: ").append(user.getPasswordHint());
            //FIXME: MZA: urlFor doesn't work as expected - "Login at: ../login"
            msg.append("\n\nLogin at: ").append(RequestUtils.toAbsolutePath(urlFor(Login.class, new PageParameters()).toString()));
//            msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(getRequest()));

            SimpleMailMessage message = new SimpleMailMessage();    //serviceFacade.getMailMessage();
            message.setTo(user.getEmail());

            String subject = '[' + getString("webapp.name") + "] " + getString("user.passwordHint");
            message.setSubject(subject);
            message.setText(msg.toString());
            log.debug("subject: {}", subject);
            log.debug("message: {}", message);
//            serviceFacade.getMailEngine().send(message);

//            info("login.passwordHint.sent", true, username, user.getEmail());
            getSession().info(getString("login.passwordHint.sent"));
        } catch (UsernameNotFoundException e) {
            log.warn(e.getMessage());
            // If exception is expected do not rethrow
//            error("login.passwordHint.error", true, username);
            getSession().error(getString("login.passwordHint.error"));
        } catch (MailException me) {
            getSession().error(me.getCause().getLocalizedMessage());
        }

        setRedirect(true);
        throw new RestartResponseException(Login.class);

    }
}
