package org.appfuse.webapp.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import org.appfuse.Constants;
import org.appfuse.model.LabelValue;
import org.appfuse.model.User;
import org.appfuse.model.UserRole;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Implementation of <strong>SimpleFormController</strong> that interacts with
 * the {@link UserManager} to retrieve/persist values to the database.
 *
 * <p/><a href="UserFormController.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserFormController extends BaseFormController {
    
    public ModelAndView processFormSubmission(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object command,
                                              BindException errors)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                return new ModelAndView(new RedirectView("mainMenu.html"));
            } else {
                return new ModelAndView(new RedirectView("users.html"));
            }
        }

        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        User user = (User) command;

        if (request.getParameter("delete") != null) {
            mgr.removeUser(user.getUsername());
            saveMessage(request, getText("user.deleted", user.getUsername()));

            return new ModelAndView(new RedirectView("users.html"));
        } else {
            if ("true".equals(request.getParameter("encryptPass"))) {
                String algorithm =
                    (String) getConfiguration().get(Constants.ENC_ALGORITHM);

                if (algorithm == null) { // should only happen for test case

                    if (log.isDebugEnabled()) {
                        log.debug("assuming testcase, setting algorithm to 'SHA'");
                    }

                    algorithm = "SHA";
                }

                user.setPassword(StringUtil.encodePassword(user.getPassword(),
                                                           algorithm));
            }

            try {
                mgr.saveUser(user);
            } catch (DataIntegrityViolationException e) {
                log.warn("User already exists: " + e.getMessage());

                errors.rejectValue("username", "errors.existing.user",
                                   new Object[] {
                                       user.getUsername(), user.getEmail()
                                   }, "duplicate user");

                // redisplay the unencrypted passwords
                user.setPassword(user.getConfirmPassword());

                return showForm(request, response, errors);
            }

            if (!StringUtils.equals(request.getParameter("from"), "list")) {
                HttpSession session = request.getSession();
                session.setAttribute(Constants.USER_KEY, user);

                // update the user's remember me cookie if they didn't login
                // with a cookie
                if ((RequestUtil.getCookie(request, Constants.LOGIN_COOKIE) != null) &&
                        (session.getAttribute("cookieLogin") == null)) {
                    // delete all user cookies and add a new one
                    mgr.removeLoginCookies(user.getUsername());

                    String autoLogin =
                        mgr.createLoginCookie(user.getUsername());
                    RequestUtil.setCookie(response, Constants.LOGIN_COOKIE,
                                          autoLogin, request.getContextPath());
                }

                saveMessage(request, getText("user.updated", user.getFullName()));

                // return to main Menu
                return new ModelAndView(new RedirectView("mainMenu.html"));
            } else {
                if (StringUtils.isEmpty(request.getParameter("id"))) {
                    saveMessage(request,
                                getText("user.added", user.getFullName()));

                    // Send an account information e-mail
                    message.setSubject(getText("signup.email.subject"));
                    sendUserMessage(user,
                                    getText("newuser.email.message", user.getFullName()),
                                    RequestUtil.getAppURL(request));

                    return showNewForm(request, response);
                } else {
                    saveMessage(request,
                                getText("user.updated.byAdmin",
                                        user.getFullName()));
                }
            }
        }

        return showForm(request, response, errors);
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)
     */
    protected ModelAndView showForm(HttpServletRequest request,
                                    HttpServletResponse response,
                                    BindException errors)
    throws Exception {
        if (request.getRequestURL().indexOf("editProfile") > -1) {
            // if URL is "editProfile" - make sure it's the current user
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

        // prevent ordinary users from calling a GET on editUser.html
        if ((request.getRequestURL().indexOf("editUser") > -1) &&
                (!request.isUserInRole(Constants.ADMIN_ROLE) &&
                (request.getRemoteUser() != null))) { // be nice to unit tests
            response.sendError(HttpServletResponse.SC_FORBIDDEN);

            return null;
        }

        return super.showForm(request, response, errors);
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String username = request.getParameter("username");

        if (request.getSession().getAttribute("cookieLogin") != null) {
            saveMessage(request, getText("userProfile.cookieLogin"));
        }

        User user = null;

        if (request.getRequestURL().indexOf("editProfile") > -1) {
            user = mgr.getUser(getUser(request).getUsername());
        } else if (!StringUtils.isEmpty(username) &&
                       !"".equals(request.getParameter("id"))) {
            user = mgr.getUser(username);
        } else {
            user = new User();
            user.addRole(Constants.USER_ROLE);
        }

        user.setConfirmPassword(user.getPassword());

        return user;
    }

    protected void onBind(HttpServletRequest request, Object command)
    throws Exception {
        // this method is used to nullify the list of roles on the user.  If you
        // don't do this, Spring seems to remember the last roles this User had
        // on it - as if it were a session form or something, but I know its not.
        User user = (User) command;
        user.getRoles().clear();

        String[] roles = request.getParameterValues("userRoles");

        if (roles != null) {
            user.setUserRoles(roles);
        }

        // if the user is being deleted, turn off validation
        if (request.getParameter("delete") != null) {
            super.setValidateOnBinding(false);
        } else {
            super.setValidateOnBinding(true);
        }
    }
}
