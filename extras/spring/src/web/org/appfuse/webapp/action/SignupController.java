package org.appfuse.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;


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
    private RoleManager roleManager;

    /**
     * @param roleManager The roleManager to set.
     */
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }
    
    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        User user = (User) command;
        Locale locale = request.getLocale();

        String algorithm =
        	(String) getConfiguration().get(Constants.ENC_ALGORITHM);

        if (algorithm == null) { // should only happen for test case
            if (log.isDebugEnabled()) {
                log.debug("assuming testcase, setting algorithm to 'SHA'");
            }
            algorithm = "SHA";
        }
        
        user.setPassword(StringUtil.encodePassword(user.getPassword(), algorithm));
        user.setEnabled(Boolean.TRUE);
        
        // Set the default user role on this new user
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            mgr.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());

            errors.rejectValue("username", "errors.existing.user",
                               new Object[] {
                                   user.getUsername(), user.getEmail()
                               }, "duplicate user");

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return showForm(request, response, errors);
        }

        // Set cookies for auto-magical login ;-)
        String loginCookie = mgr.createLoginCookie(user.getUsername());
        RequestUtil.setCookie(response, Constants.LOGIN_COOKIE, loginCookie,
                              request.getContextPath());

        saveMessage(request, getText("user.registered", user.getUsername(), locale));

        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername()
                    + "' an account information e-mail");
        }

        // Send an account information e-mail
        message.setSubject(getText("signup.email.subject", locale));
        sendUserMessage(user, getText("signup.email.message", locale), 
                        RequestUtil.getAppURL(request));
        
        return new ModelAndView(getSuccessView());
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        return new User();
    }
}