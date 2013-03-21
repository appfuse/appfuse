package org.appfuse.webapp.controller;

import org.apache.commons.lang.StringUtils;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Controller
@RequestMapping("/signup*")
public class SignupController extends BaseFormController {
    private RoleManager roleManager;

    @Autowired
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public SignupController() {
        setCancelView("redirect:login");
        setSuccessView("redirect:mainMenu");
    }

    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    public User showForm() {
        return new User();  
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(User user, BindingResult errors, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }

        if (validator != null) { // validator is null during testing
            validator.validate(user, errors);

            if (errors.hasErrors()) {
                return "signup";
            }
        }

        Locale locale = request.getLocale();
        
        user.setEnabled(true);

        // Set the default user role on this new user
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        String password = user.getPassword();
        
        try {
            this.getUserManager().saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(ade.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null; 
        } catch (UserExistsException e) {
            errors.rejectValue("username", "errors.existing.user",
                    new Object[]{user.getUsername(), user.getEmail()}, "duplicate user");

            // redisplay the unencrypted passwords
            user.setPassword(password);
            user.setConfirmPassword(password);
            return "signup";
        }

        saveMessage(request, getText("user.registered", user.getUsername(), locale));
        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // log user in automatically
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");
        }

        // Send an account information e-mail
        message.setSubject(getText("signup.email.subject", locale));

        try {
            sendUserMessage(user, getText("signup.email.message", locale), 
            		RequestUtil.getAppURL(request), password);
        } catch (MailException me) {
            saveError(request, me.getMostSpecificCause().getMessage());
        }
        
        return getSuccessView();
    }
}
