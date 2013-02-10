package org.appfuse.webapp.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.AbstractWebPage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * Service page used for sending password hint.
 *
 * @author Marcin Zajączkowski, 2011-02-11
 */
@MountPath("passwordHint/${username}")
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

        String username = parameters.get("username").toString();
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

            StringBuilder msg = new StringBuilder();
            msg.append("Your password hint is: ").append(user.getPasswordHint());
            msg.append("\n\nLogin at: ")
                    .append(RequestCycle.get().getUrlRenderer().renderFullUrl(
                            Url.parse(urlFor(Login.class, null).toString())));

            SimpleMailMessage message = new SimpleMailMessage();    //serviceFacade.getMailMessage();
            message.setTo(user.getEmail());

            String subject = '[' + getString("webapp.name") + "] " + getString("user.passwordHint");
            message.setSubject(subject);
            message.setText(msg.toString());
            log.debug("subject: {}", subject);
            log.debug("message: {}", message);
//            serviceFacade.getMailEngine().send(message);

            getSession().info(new StringResourceModel(
                    "login.passwordHint.sent", this, null, new Object[] {username, user.getEmail()}).getString());
        } catch (UsernameNotFoundException e) {
            log.warn(e.getMessage());
            // This exception is expected to not be rethrown
            getSession().error(new StringResourceModel(
                    "login.passwordHint.error", this, null, new Object[] {username}).getString());
        } catch (MailException me) {
            getSession().error(me.getCause().getLocalizedMessage());
        }

        //TODO: MZA: It generated http://.../passwordHint/../login which works in Firefox, but breaks acceptance tests
        throw new RestartResponseException(Login.class);
    }
}
