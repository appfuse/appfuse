package org.appfuse.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.form.UserForm;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;

/**
 * Action class to allow users to self-register.
 *
 * <p/>
 * <a href="SignupAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * 
 * @struts.action name="userFormEx" path="/signup" scope="request"
 *  validate="false" input="failure"
 * 
 * @struts.action-forward name="failure" path="/WEB-INF/pages/signup.jsp"
 * @struts.action-forward name="success" path="/mainMenu.html" redirect="true"
 */
public final class SignupAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {
        
        // if it's an HTTP GET, simply forward to jsp
        if (request.getMethod().equals("GET")) {
            return mapping.findForward("failure");
        // user clicked cancel button
        } else if (isCancelled(request)) {
            return new ActionForward("/");
        // run validation
        } else {
            // run validation rules on this form
            ActionMessages errors = form.validate(mapping, request);
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("failure");
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("registering user...");
        }

        ActionMessages errors = new ActionMessages();
        UserForm userForm = (UserForm) form;
        User user = (User) convert(form);
        String algorithm =
                (String) getConfiguration().get(Constants.ENC_ALGORITHM);

        // Set the default user role on this new user
        RoleManager roleMgr = (RoleManager) getBean("roleManager");
        user.addRole(roleMgr.getRole(Constants.USER_ROLE));

        try {
            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(user.getPassword(),
                    algorithm));
            UserManager mgr = (UserManager) getBean("userManager");
            mgr.saveUser(user);

            // Set cookies for auto-magical login ;-)
            String loginCookie = mgr.createLoginCookie(user.getUsername());
            RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                    loginCookie, request.getContextPath());
        } catch (DataIntegrityViolationException e) {
            log.warn("User already exists: " + e.getMessage());
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("errors.existing.user",
                            userForm.getUsername(),
                            userForm.getEmail()));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        ActionMessages messages = new ActionMessages();
        MessageResources resources = getResources(request);

        messages.add(ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("user.registered", userForm.getUsername()));

        saveMessages(request.getSession(), messages);
        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + userForm.getUsername()
                    + "' an account information e-mail");
        }

        SimpleMailMessage message = (SimpleMailMessage) getBean("mailMessage");
        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");
        
        StringBuffer msg = new StringBuffer();
        msg.append(resources.getMessage("signup.email.message"));
        msg.append("\n\n" + resources.getMessage("userFormEx.username"));
        msg.append(": " + userForm.getUsername() + "\n");
        msg.append(resources.getMessage("userFormEx.password") + ": ");
        msg.append(userForm.getPassword());
        msg.append("\n\nLogin at: " + RequestUtil.getAppURL(request));
        message.setText(msg.toString());
        
        message.setSubject(resources.getMessage("signup.email.subject"));
        
        MailEngine engine = (MailEngine) getBean("mailEngine");
        engine.send(message);

        return mapping.findForward("success");
    }

}
