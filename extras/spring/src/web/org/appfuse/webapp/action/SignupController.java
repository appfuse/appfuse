package org.appfuse.webapp.action;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailSender;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * Controller to signup new users.
 *
 * <p>
 * <a href="SignupController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class SignupController extends BaseFormController {

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        User user = (User) command;

        String algorithm =
        	(String) getConfiguration().get(Constants.ENC_ALGORITHM);

        if (algorithm == null) { // should only happen for test case
            if (log.isDebugEnabled()) {
                log.debug("assuming testcase, setting algorithm to 'SHA'");
            }
            algorithm = "SHA";
        }
        
        user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));

        // Set the default user role on this new user
        user.addRole(Constants.USER_ROLE);

        try {
            mgr.saveUser(user);
        } catch (Exception e) {
            if ((e.getMessage() != null) &&
                    (e.getMessage().indexOf("Duplicate entry") != -1)) {
                log.warn("User already exists: " + e.getMessage());

                errors.rejectValue("username", "errors.existing.user",
                                   new Object[] {
                                       user.getUsername(), user.getEmail()
                                   }, "duplicate user");

                // redisplay the unencrypted passwords
                user.setPassword(user.getConfirmPassword());
                return showForm(request, response, errors);
            }
        }

        // Set cookies for auto-magical login ;-)
        String loginCookie = mgr.createLoginCookie(user.getUsername());
        RequestUtil.setCookie(response, Constants.LOGIN_COOKIE, loginCookie,
                              request.getContextPath());

        String message =
            getMessageSourceAccessor().getMessage("user.registered", new Object[] { user.getUsername() });
        saveMessage(request, message);

        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername()
                    + "' an account information e-mail");
        }

        StringBuffer msg = new StringBuffer();
        msg.append(getMessageSourceAccessor().getMessage("signup.email.message"));
        msg.append("\n\n" + getMessageSourceAccessor().getMessage("user.username"));
        msg.append(": " + user.getUsername() + "\n");
        msg.append(getMessageSourceAccessor().getMessage("user.password") + ": ");
        msg.append(user.getConfirmPassword());
        msg.append("\n\nLogin at: " + RequestUtil.getAppURL(request) +
                       request.getContextPath());

        String subject = getMessageSourceAccessor().getMessage("signup.email.subject");

        try {
            // From,to,cc,subject,content
            MailSender.sendTextMessage(Constants.DEFAULT_FROM,
                                       user.getEmail(), null,
                                       subject, msg.toString());
        } catch (MessagingException me) {
            log.warn("Failed to send Account Information e-mail");
        }
        
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        return new User();
    }
}
