package org.appfuse.webapp.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import org.appfuse.Constants;
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
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:16 $
 *
 * @struts.action name="userFormEx" path="/editUser" scope="request"
 *  validate="false" parameter="action" input="list" roles="admin"
 * @struts.action name="userFormEx" path="/editProfile" scope="request"
 *  validate="false" parameter="action" input="mainMenu"
 * @struts.action name="userFormEx" path="/saveUser" scope="request"
 *  validate="true" parameter="action" input="editProfile"
 *
 * @struts.action-forward name="list" path=".userList"
 * @struts.action-forward name="edit" path=".userProfile"
 */
public final class UserAction extends BaseAction {
    /** The <code>Log</code> instance for this class */
    private Log log = LogFactory.getLog(UserAction.class);

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
        mgr.removeUser(userForm);

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

        // if a user's username is passed in
        if (request.getParameter("username") != null) {
            // lookup the user using that id
            userForm = (UserForm) mgr.getUser(userForm.getUsername());
        } else {
            // look it up based on the current user's id
            userForm =
                (UserForm) mgr.getUser(getUserForm(session).getUsername());
        }

        UserFormEx ex = new UserFormEx();
        BeanUtils.copyProperties(ex, userForm);
        ex.setConfirmPassword(ex.getPassword());
        request.setAttribute(Constants.USER_EDIT_KEY, ex);
        setupRoles(userForm, request);

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
        UserFormEx theForm = (UserFormEx) form;

        String password = theForm.getPassword();
        UserForm userForm = new UserForm();

        // Exceptions are caught by ActionExceptionHandler
        // all we need to persist is the parent object
        BeanUtils.copyProperties(userForm, theForm);

        if (StringUtils.equals(request.getParameter("encryptPass"), "true")) {
            String algorithm =
                (String) getConfiguration().get(Constants.ENC_ALGORITHM);

            if (algorithm == null) { // should only happen for test case
                log.debug("assuming testcase, setting algorigthm to 'SHA'");
                algorithm = "SHA";
            }

            userForm.setPassword(StringUtil.encodePassword(password, algorithm));
        }

        UserManager mgr = (UserManager) getBean("userManager");

        try {
            userForm = (UserForm) mgr.saveUser(userForm);
            setupRoles(userForm, request);
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
            session.setAttribute(Constants.USER_KEY, userForm);

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
            if (StringUtils.isEmpty(theForm.getId())) {
                messages.add(ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage("user.added", userForm.getUsername()));
                session.setAttribute(Globals.MESSAGE_KEY, messages);

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
        List users = mgr.getUsers((UserForm) form);
        request.setAttribute(Constants.USER_LIST, users);

        // return a forward to the user list definition
        return mapping.findForward("list");
    }
    
    /**
     * Convenience method to put a user's roles in request scope.
     * @param userForm
     * @param request
     */
    private void setupRoles(UserForm userForm, HttpServletRequest request) {
        List roles = new LinkedList();
        for (Iterator it = userForm.getRoles().iterator(); it.hasNext();) {
            UserRoleForm role = (UserRoleForm) it.next();
            // convert the user's roles to LabelValueBeans
            roles.add(new LabelValueBean(role.getRoleName(), role.getRoleName()));
        }
        request.setAttribute(Constants.USER_ROLES, roles);   
    }
}
