package org.appfuse.webapp.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.mail.MessagingException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.appfuse.Constants;
import org.appfuse.model.UserRole;
import org.appfuse.model.User;
import org.appfuse.service.MailSender;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.form.UserForm;
import org.appfuse.webapp.form.UserFormEx;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.web.context.WebApplicationContext;


/**
 * Implementation of <strong>HttpServlet</strong> that is used
 * to allow users to self-register.
 * <p/>
 * <p><a href="RegistrationServlet.java.html"><i>View Source</i></a></p>
 *
 * @author Matt Raible
 * @version $Revision: 1.2 $ $Date: 2004/03/18 20:33:06 $
 * @web.servlet display-name="Registration Servlet"
 * name="register"
 * load-on-startup="4"
 * @web.servlet-mapping url-pattern="/register/*"
 * @web.servlet-mapping url-pattern="/passwordHint/*"
 */
public final class RegistrationServlet extends HttpServlet {
    private Log log = LogFactory.getLog(RegistrationServlet.class);

    /**
     * Route the user to the execute method
     *
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Route the user to the execute method
     *
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        execute(request, response);
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     *
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("processing servletPath '" + request.getServletPath() +
                      "'");
        }

        MessageResources resources =
            ((MessageResources) getServletContext().getAttribute(Globals.MESSAGES_KEY));
        request.setAttribute(Globals.MESSAGES_KEY, resources);

        // check the servletPath to see if they user is requesting a passwordHint
        if (StringUtils.equalsIgnoreCase(request.getServletPath(),
                                             "/passwordHint")) {
            sendPasswordHint(request, response);

            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("registering user...");
        }

        User user = new User();

        // populate the user object with request parameters
        RequestUtils.populate(user, request);
        UserFormEx userForm = new UserFormEx();
        ActionErrors errors = new ActionErrors();
        try {
            BeanUtils.copyProperties(userForm, user);
        } catch (Exception e) {
            log.error("error converting user to userForm: " + e.getMessage());
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.conversion"));
        }
        request.setAttribute(Constants.USER_EDIT_KEY, userForm);

        errors = userForm.validate(null, request);

        if (!errors.isEmpty()) {
            request.setAttribute(Globals.ERROR_KEY, errors);
            dispatch(request, response, "signup.jsp");

            return;
        }

        String algorithm =
            (String) getServletContext().getAttribute(Constants.ENC_ALGORITHM);

        // Set the default user role on this new user
        user.addRole(Constants.USER_ROLE);

        try {

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(user.getPassword(),
                                                       algorithm));

            WebApplicationContext ctx =
                (WebApplicationContext) getServletContext()
                    .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            UserManager mgr = (UserManager) ctx.getBean("userManager");
            user = (User) mgr.saveUser(user);

            // Set cookies for auto-magical login ;-)
            String loginCookie = mgr.createLoginCookie(user.getUsername());
            RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                    loginCookie, request.getContextPath());
        } catch (Exception e) {
            if ((e.getMessage() != null) &&
                    (e.getMessage().indexOf("Duplicate entry") != -1)) {
                // user already exists!
                log.warn("User already exists: " + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.existing.user",
                                             userForm.getUsername(),
                                             userForm.getEmail()));
                request.setAttribute(Globals.ERROR_KEY, errors);

                dispatch(request, response, "signup.jsp");
            }

            e.printStackTrace();
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("errors.general"));

            while (e != null) {
                // catch duplicate entry exceptions
                String msg = e.getMessage();

                if ((msg.indexOf("Duplicate entry") != -1) ||
                        (msg.indexOf("could not insert") != -1)) {
                    log.warn("User already exists: " + e.getMessage());
                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                               new ActionMessage("errors.existing.user",
                                                 userForm.getUsername(),
                                                 userForm.getEmail()));

                    break;
                }

                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.detail", e.getMessage()));

                e = (Exception) e.getCause();
            }

            request.setAttribute(Globals.ERROR_KEY, errors);

            dispatch(request, response, "signup.jsp");

            return;
        }

        // Removed logic to set cookies for auto-login - why complicate things?
        ActionMessages messages = new ActionMessages();

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

        // send to welcome page
        String route = request.getContextPath() + "/mainMenu.do";

        if (log.isDebugEnabled()) {
            log.debug("routing user to: " + route);
        }

        response.sendRedirect(response.encodeRedirectURL(route));
    }

    /**
     * Process the user's request to get their password e-mailed to them.
     *
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet exception occurs
     */
    public void sendPasswordHint(HttpServletRequest request,
                                 HttpServletResponse response)
    throws IOException, ServletException {
        MessageResources resources =
            (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);
        ActionMessages messages = new ActionMessages();
        String userId = request.getParameter("username");
        String failure = "/login.jsp";
        
        // ensure that the username has been sent
        if (userId == null) {
            log.warn("Username not specified, notifying user that it's a required field.");

            ActionErrors errors = new ActionErrors();
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("errors.required",
                                         resources.getMessage("userFormEx.username")));
            request.setAttribute(Globals.ERROR_KEY, errors);
            dispatch(request, response, failure);

            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Processing Password Hint...");
        }

        // look up the user's information
        try {
            WebApplicationContext ctx =
                (WebApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            UserManager userMgr = (UserManager) ctx.getBean("userManager");

            User user = (User) userMgr.getUser(userId);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: " + user.getPasswordHint());
            msg.append("\n\nLogin at: " + RequestUtils.serverURL(request) +
                       request.getContextPath());

            // From,to,cc,subject,content
            MailSender.sendTextMessage(Constants.DEFAULT_FROM,
                                       user.getEmail(), null,
                                       "Password Hint", msg.toString());

            messages.add(ActionMessages.GLOBAL_MESSAGE,
                         new ActionMessage("login.passwordHint.sent", userId,
                                           user.getEmail()));
        } catch (Exception e) {
            //If exception is expected do not rethrow
            ActionErrors errors = new ActionErrors();
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("login.passwordHint.error", userId));

            request.setAttribute(Globals.ERROR_KEY, errors);
            dispatch(request, response, failure);

            return;
        }

        // report messages back to the user
        if (!messages.isEmpty()) {
            request.setAttribute(Globals.MESSAGE_KEY, messages);
        }

        dispatch(request, response, failure);

        return;
    }

    /**
     * Dispatch request to common JSP
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void dispatch(HttpServletRequest request,
                         HttpServletResponse response, String jsp)
    throws IOException, ServletException {
        // forward back to the register.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/" + jsp);
        dispatcher.forward(request, response);
    }
}
