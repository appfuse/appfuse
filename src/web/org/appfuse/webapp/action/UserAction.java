package org.appfuse.webapp.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.model.UserRole;
import org.appfuse.service.MailSender;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.form.UserForm;
import org.appfuse.webapp.form.UserFormEx;
import org.appfuse.webapp.form.UserRoleForm;
import org.appfuse.webapp.util.RequestUtil;


/**
 * Implementation of <strong>Action</strong> that interacts with the {@link
 * UserForm} and retrieves values.  It interacts with the {@link
 * UserManager} to retrieve/persist values to the database.
 *
 * <p>
 * <a href="UserAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.7 $ $Date: 2004/03/20 06:18:00 $
 *
 * @struts.action name="userFormEx" path="/editUser" scope="request"
 *  validate="false" parameter="action" input="list" roles="admin"
 * @struts.action name="userFormEx" path="/editProfile" scope="request"
 *  validate="false" parameter="action" input="mainMenu"
 * @struts.action name="userFormEx" path="/saveUser" scope="request"
 *  validate="true" parameter="action" input="edit"
 *
 * @struts.action-forward name="list" path=".userList"
 * @struts.action-forward name="edit" path=".userProfile"
 */
public final class UserAction extends BaseAction {
    /** The <code>Log</code> instance for this class */
    private static Log log = LogFactory.getLog(UserAction.class);

    public ActionForward add(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'add' method");
        }

        // get messages from the session and put them 
        // in the request
        getMessages(request);

        ArrayList roles = new ArrayList();

        // create the default user role 
        roles.add(new LabelValueBean(Constants.USER_ROLE, Constants.USER_ROLE));

        // and stuff it into the request
        request.setAttribute(Constants.USER_ROLES, roles);

        return mapping.findForward("edit");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'cancel' method");
        }

        if (!StringUtils.equals(request.getParameter("from"), "list")) {
            return mapping.findForward("mainMenu");
        } else {
            String next = mapping.findForward("viewUsers").getPath();
            return new ActionForward(next, true);
        }
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'delete' method");
        }

        // Extract attributes and parameters we will need
        ActionMessages messages = new ActionMessages();
        UserForm userForm = (UserForm) form;

        // Exceptions are caught by ActionExceptionHandler
        UserManager mgr = (UserManager) getBean("userManager");
        mgr.removeUser(convert(userForm));

        messages.add(ActionMessages.GLOBAL_MESSAGE,
                     new ActionMessage("user.deleted", userForm.getUsername()));

        saveMessages(request, messages);

        // return a forward to searching users
        return mapping.findForward("viewUsers");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'edit' method");
        }

        UserForm userForm = (UserForm) form;
        HttpSession session = request.getSession();

        // if URL is "editProfile" - make sure it's the current user
        if (request.getRequestURL().indexOf("editProfile") > -1) {
            // reject if username passed in or "list" parameter passed in
            // someone that is trying this probably knows the AppFuse code
            // but it's a legitimate bug, so I'll fix it. ;-)
            if ((request.getParameter("username") != null) ||
                    (request.getParameter("from") != null)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                log.warn("User '" + request.getRemoteUser() +
                         "' is trying to edit user '" +
                         request.getParameter("username") + "'");

                return null;
            }
        }

        // Exceptions are caught by ActionExceptionHandler
        UserManager mgr = (UserManager) getBean("userManager");
        User user = null;

        // if a user's username is passed in
        if (request.getParameter("username") != null) {
            // lookup the user using that id
            user = (User) mgr.getUser(userForm.getUsername());
        } else {
            // look it up based on the current user's id
            user =
                (User) mgr.getUser(getUser(session).getUsername());
        }

        UserFormEx ex = new UserFormEx();
        BeanUtils.copyProperties(ex, convert(user));
        ex.setConfirmPassword(ex.getPassword());
        request.setAttribute(Constants.USER_EDIT_KEY, ex);
        setupRoles(user, request);

        // if user logged in with a cookie, display a warning that they
        // can't change passwords
        if (log.isDebugEnabled()) {
            log.debug("checking for cookieLogin...");
        }

        if (session.getAttribute("cookieLogin") != null) {
            ActionMessages messages = new ActionMessages();

            // add warning messages
            messages.add(ActionMessages.GLOBAL_MESSAGE,
                         new ActionMessage("userProfile.cookieLogin"));
            saveMessages(request, messages);
        }

        // return a forward to edit forward
        return mapping.findForward("edit");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'save' method");
        }

        // Extract attributes and parameters we will need
        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        HttpSession session = request.getSession();
        UserFormEx userForm = (UserFormEx) form;

        String password = userForm.getPassword();
        User user = new User();

        // Exceptions are caught by ActionExceptionHandler
        // all we need to persist is the parent object
        BeanUtils.copyProperties(user, userForm);

        if (StringUtils.equals(request.getParameter("encryptPass"), "true")) {
            String algorithm =
                (String) getConfiguration().get(Constants.ENC_ALGORITHM);

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            user.setPassword(StringUtil.encodePassword(password, algorithm));
        }

        UserManager mgr = (UserManager) getBean("userManager");

        try {
            user = (User) mgr.saveUser(user);
            setupRoles(user, request);
        } catch (Exception e) {
            if ((e.getMessage() != null) &&
                    (e.getMessage().indexOf("Duplicate entry") != -1)) {
                // user already exists!
                log.warn("User already exists: " + e.getMessage());
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.existing.user",
                                             userForm.getUsername(),
                                             userForm.getEmail()));
                saveErrors(request, errors);

                return mapping.findForward("edit");
            }

            e.printStackTrace();
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("errors.general"));

            while (e != null) {
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.detail", e.getMessage()));
                e = (Exception) e.getCause();
            }

            saveErrors(request, errors);

            return mapping.findForward("edit");
        }

        if (!StringUtils.equals(request.getParameter("from"), "list")) {
            session.setAttribute(Constants.USER_KEY, user);

            // update the user's remember me cookie if they didn't login
            // with a cookie
            if ((RequestUtil.getCookie(request, Constants.LOGIN_COOKIE) != null) &&
                    (session.getAttribute("cookieLogin") == null)) {
                // delete all user cookies and add a new one
                mgr.removeLoginCookies(userForm.getUsername());

                String autoLogin =
                    mgr.createLoginCookie(userForm.getUsername());
                RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                                      autoLogin, request.getContextPath());
            }

            // add success messages
            messages.add(ActionMessages.GLOBAL_MESSAGE,
                         new ActionMessage("user.updated"));
            saveMessages(request, messages);

            // return a forward to main Menu
            return mapping.findForward("mainMenu");
        } else {
            // add success messages
            if (StringUtils.isEmpty(userForm.getId())) {
                messages.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("user.added", userForm.getUsername()));
                session.setAttribute(Globals.MESSAGE_KEY, messages);
                sendNewUserEmail(request, userForm);
                return mapping.findForward("addUser");
            } else {
                messages.add(ActionMessages.GLOBAL_MESSAGE,
                             new ActionMessage("user.updated.byAdmin",
                        userForm.getUsername()));
                saveMessages(request, messages);

                return mapping.findForward("edit");
            }
        }
    }

    public ActionForward search(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'search' method");
        }

        UserForm userForm = (UserForm) form;
        
        // Exceptions are caught by ActionExceptionHandler
        UserManager mgr = (UserManager) getBean("userManager");
        List users = mgr.getUsers(convert(userForm));
        request.setAttribute(Constants.USER_LIST, users);

        // return a forward to the user list definition
        return mapping.findForward("list");
    }
    
    /**
     * Convenience method to put a user's roles in request scope.
     * @param user
     * @param request
     */
    private void setupRoles(User user, HttpServletRequest request) {
        List roles = new ArrayList();
        if (user.getRoles() != null) {
            for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                UserRole role = (UserRole) it.next();
                // convert the user's roles to LabelValueBeans
                roles.add(new LabelValueBean(role.getRoleName(),
                          role.getRoleName()));
            }
        }
        request.setAttribute(Constants.USER_ROLES, roles);
    }
    
    private void sendNewUserEmail(HttpServletRequest request, UserForm userForm)
        throws Exception {
        MessageResources resources = getResources(request);
        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + userForm.getUsername()
                    + "' an account information e-mail");
        }

        User user = getUser(request.getSession());
        String fullName = user.getFirstName() + " " + user.getLastName();
        StringBuffer msg = new StringBuffer();
        msg.append(resources.getMessage("newuser.email.message", fullName));
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
    }
}
