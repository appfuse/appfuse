package org.appfuse.webapp.action;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailSender;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.form.UserForm;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Action class to allow users to self-register.
 *
 * <p/>
 * <a href="SignupAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @struts.action name="userFormEx" path="/signup" scope="request"
 *  validate="false" input="failure"
 * @struts.action-forward name="failure" path="/WEB-INF/pages/signup.jsp"
 * @struts.action-forward name="success" path="/mainMenu.html" redirect="true"
 */
public final class SignupAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
    throws Exception {
        
        // if it's an HTTP GET, simply forward to jsp
    	// test for Cactus is workaround until I figure how to send a post
    	// with StrutsTestCase: http://sourceforge.net/forum/message.php?msg_id=2726171
        if (request.getMethod().equals("GET") && request.getParameter("Cactus_TestClass") == null) {
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
        user.addRole(Constants.USER_ROLE);

        try {

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(user.getPassword(),
                    algorithm));
            UserManager mgr = (UserManager) getBean("userManager");
            user = mgr.saveUser(user);

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

        HttpSession session = request.getSession();
        session.setAttribute(Globals.MESSAGE_KEY, messages);
        session.setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + userForm.getUsername()
                    + "' an account information e-mail");
        }

        StringBuffer msg = new StringBuffer();
        msg.append(resources.getMessage("signup.email.message"));
        msg.append("\n\n" + resources.getMessage("userFormEx.username"));
        msg.append(": " + userForm.getUsername() + "\n");
        msg.append(resources.getMessage("userFormEx.password") + ": ");
        msg.append(userForm.getPassword());
        msg.append("\n\nLogin at: " + RequestUtils.serverURL(request) +
                request.getContextPath());

        String subject = resources.getMessage("signup.email.subject");

        try {
            // From,to,cc,subject,content
            MailSender.sendTextMessage(Constants.DEFAULT_FROM,
                    userForm.getEmail(), null,
                    subject, msg.toString());
        } catch (MessagingException me) {
            log.warn("Failed to send Account Information e-mail");
        }

        return mapping.findForward("success");
    }

}
