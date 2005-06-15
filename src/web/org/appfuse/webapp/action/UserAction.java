package org.appfuse.webapp.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.form.UserForm;
import org.appfuse.webapp.util.RequestUtil;

import org.springframework.mail.SimpleMailMessage;

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
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *
 * @struts.action name="userForm" path="/users" scope="request"
 *  validate="false" parameter="method" input="mainMenu" roles="admin"
 * @struts.action name="userForm" path="/editUser" scope="request"
 *  validate="false" parameter="method" input="list" roles="admin"
 * @struts.action name="userForm" path="/editProfile" scope="request"
 *  validate="false" parameter="method" input="mainMenu"
 * @struts.action name="userForm" path="/saveUser" scope="request"
 *  validate="false" parameter="method" input="edit"
 *
 * @struts.action-forward name="list" path="/WEB-INF/pages/userList.jsp"
 * @struts.action-forward name="edit" path="/WEB-INF/pages/userProfile.jsp"
 */
public final class UserAction extends BaseAction {
    
    public ActionForward add(ActionMapping mapping, ActionForm form,
                             HttpServletRequest request,
                             HttpServletResponse response)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'add' method");
        }

        User user = new User();
        user.addRole(new Role(Constants.USER_ROLE));
        UserForm userForm = (UserForm) convert(user);
        updateFormBean(mapping, request, userForm);

        checkForCookieLogin(request);

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
            return mapping.findForward("viewUsers");
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
        mgr.removeUser(userForm.getUsername());

        messages.add(ActionMessages.GLOBAL_MESSAGE,
                     new ActionMessage("user.deleted", userForm.getFirstName()
                                       + ' ' + userForm.getLastName()));

        saveMessages(request.getSession(), messages);

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
        if (request.getRequestURI().indexOf("editProfile") > -1) {
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
            user = mgr.getUser(userForm.getUsername());
        } else {
            // look it up based on the current user's id
            user = mgr.getUser(getUser(session).getUsername());
        }

        BeanUtils.copyProperties(userForm, convert(user));
        userForm.setConfirmPassword(userForm.getPassword());
        updateFormBean(mapping, request, userForm);

        checkForCookieLogin(request);

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
        
        // run validation rules on this form
        // See https://appfuse.dev.java.net/issues/show_bug.cgi?id=128
        ActionMessages errors = form.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("edit");
        }

        // Extract attributes and parameters we will need
        ActionMessages messages = new ActionMessages();
        HttpSession session = request.getSession();
        UserForm userForm = (UserForm) form;
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
        RoleManager roleMgr = (RoleManager) getBean("roleManager");
        String[] userRoles = request.getParameterValues("userRoles");

        for (int i = 0; userRoles != null &&  i < userRoles.length; i++) {
            String roleName = userRoles[i];
            user.addRole(roleMgr.getRole(roleName));
        }

        try {
            mgr.saveUser(user);
        } catch (UserExistsException e) {
            log.warn(e.getMessage());
            errors.add(ActionMessages.GLOBAL_MESSAGE,
                       new ActionMessage("errors.existing.user",
                                         userForm.getUsername(),
                                         userForm.getEmail()));
            saveErrors(request, errors);

            return mapping.findForward("edit");
        }

        BeanUtils.copyProperties(userForm, convert(user));
        userForm.setConfirmPassword(userForm.getPassword());
        updateFormBean(mapping, request, userForm);
        
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
                         new ActionMessage("user.saved"));
            saveMessages(request.getSession(), messages);

            // return a forward to main Menu
            return mapping.findForward("mainMenu");
        } else {
            // add success messages
            if ("".equals(request.getParameter("version"))) {
                messages.add(ActionMessages.GLOBAL_MESSAGE,
                             new ActionMessage("user.added", user.getFullName()));
                saveMessages(request.getSession(), messages);
                sendNewUserEmail(request, userForm);

                return mapping.findForward("addUser");
            } else {
                messages.add(ActionMessages.GLOBAL_MESSAGE,
                             new ActionMessage("user.updated.byAdmin",
                                               user.getFullName()));
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
        User user = (User) convert(userForm);
        List users = mgr.getUsers(user);
        request.setAttribute(Constants.USER_LIST, users);

        // return a forward to the user list definition
        return mapping.findForward("list");
    }
    
    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
    throws Exception {
        
        return search(mapping, form, request, response);
    }

    private void sendNewUserEmail(HttpServletRequest request, UserForm userForm)
    throws Exception {
        MessageResources resources = getResources(request);

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + userForm.getUsername() +
                      "' an account information e-mail");
        }

        SimpleMailMessage message = (SimpleMailMessage) getBean("mailMessage");
        message.setTo(userForm.getFullName() + "<" + userForm.getEmail() + ">");

        StringBuffer msg = new StringBuffer();
        msg.append(resources.getMessage("newuser.email.message",
                                        userForm.getFullName()));
        msg.append("\n\n" + resources.getMessage("userForm.username"));
        msg.append(": " + userForm.getUsername() + "\n");
        msg.append(resources.getMessage("userForm.password") + ": ");
        msg.append(userForm.getPassword());
        msg.append("\n\nLogin at: " + RequestUtil.getAppURL(request));
        message.setText(msg.toString());

        message.setSubject(resources.getMessage("signup.email.subject"));

        MailEngine engine = (MailEngine) getBean("mailEngine");
        engine.send(message);
    }

    private void checkForCookieLogin(HttpServletRequest request) {
        // if user logged in with a cookie, display a warning that they
        // can't change passwords
        if (log.isDebugEnabled()) {
            log.debug("checking for cookieLogin...");
        }

        if (request.getSession().getAttribute("cookieLogin") != null) {
            ActionMessages messages = new ActionMessages();

            // add warning messages
            messages.add(ActionMessages.GLOBAL_MESSAGE,
                         new ActionMessage("userProfile.cookieLogin"));
            saveMessages(request, messages);
        }
    }
}
