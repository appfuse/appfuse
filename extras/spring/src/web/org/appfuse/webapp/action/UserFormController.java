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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.model.UserRole;
import org.appfuse.service.MailSender;
import org.appfuse.service.UserManager;
import org.appfuse.util.StringUtil;
import org.appfuse.webapp.util.RequestUtil;

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
 * @version $Revision: 1.4 $ $Date: 2004/05/25 09:06:25 $
 */
public class UserFormController extends BaseFormController {
    
    private static Log log = LogFactory.getLog(UserFormController.class);

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

            String message =
                    getMessageSourceAccessor().getMessage("user.deleted",
                            new Object[]{
                                user.getUsername()
                            });
            saveMessage(request, message);

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
            } catch (Exception e) {
                if ((e.getMessage() != null) &&
                        (e.getMessage().indexOf("Duplicate entry") != -1)) {
                    // user already exists!
                    log.warn("User already exists: " + e.getMessage());

                    errors.rejectValue("username", "errors.existing.user",
                            new Object[]{
                                user.getUsername(), user.getEmail()
                            }, "duplicate user");

                    // redisplay the unencrypted passwords
                    user.setPassword(user.getConfirmPassword());

                    return showForm(request, response, errors);
                }
            }

            String message = null;

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

                message =
                        getMessageSourceAccessor().getMessage("user.updated",
                                new Object[]{
                                    user.getFullName()
                                });
                saveMessage(request, message);

                // return to main Menu
                return new ModelAndView(new RedirectView("mainMenu.html"));
            } else {
                // add success messages
                String msg = null;

                if (StringUtils.isEmpty(request.getParameter("id"))) {
                    message =
                            getMessageSourceAccessor().getMessage("user.added",
                                    new Object[]{
                                        user.getFullName()
                                    });
                    saveMessage(request, message);
                    sendNewUserEmail(user);
                    return showNewForm(request, response);
                    //return new ModelAndView(new RedirectView("editUser.html?method=Add&from=list"));
                } else {
                    message =
                            getMessageSourceAccessor().getMessage("user.updated.byAdmin",
                                    new Object[]{
                                        user.getFullName()
                                    });
                    saveMessage(request, message);
                }
            }
        }

        return showForm(request, response, errors);
    }

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#showForm(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.validation.BindException)
     */
    protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse response, BindException errors) throws Exception {
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
        if (request.getRequestURL().indexOf("editUser") > -1  &&
                !request.isUserInRole(Constants.ADMIN_ROLE)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
        return super.showForm(request, response, errors);
    }
    
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String username = request.getParameter("username");

        if (request.getSession().getAttribute("cookieLogin") != null) {
            request.setAttribute("message",
                    getMessageSourceAccessor().getMessage("userProfile.cookieLogin"));
        }

        User user = null;

        if (request.getRequestURL().indexOf("editProfile") > -1) {
            user = (User) mgr.getUser(getUser(request).getUsername());
        } else if (!StringUtils.isEmpty(username) &&
                !"".equals(request.getParameter("id"))) {
            user = (User) mgr.getUser(username);
        } else {
            user = new User();
        }

        user.setConfirmPassword(user.getPassword());

        return user;
    }

    
    /**
     * This method is used to load up data for the roles pick list
     */
    protected Map referenceData(HttpServletRequest request)
            throws Exception {
            
        if (!isFormSubmission(request)) {
            List roles = new ArrayList();
    
            if ("Add".equals(request.getParameter("method"))) {
                // create the default user role
                roles.add(new LabelValueBean(Constants.USER_ROLE,
                        Constants.USER_ROLE));
            } else {
                String username = request.getParameter("username");
    
                if (StringUtils.isEmpty(username)) {
                    username = getUser(request).getUsername();
                }
    
                User user = (User) mgr.getUser(username);
    
                if (user.getRoles() != null) {
                    for (Iterator it = user.getRoles().iterator(); it.hasNext();) {
                        UserRole role = (UserRole) it.next();
    
                        // convert the user's roles to LabelValueBeans
                        roles.add(new LabelValueBean(role.getRoleName(),
                                role.getRoleName()));
                    }
                }
            }
    
            Map model = new HashMap();
            model.put(Constants.USER_ROLES, roles);
            return model;
        }
        return super.referenceData(request);
    }

    private void sendNewUserEmail(User user) throws Exception {
        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername() +
                    "' an account information e-mail");
        }

        String fullName = user.getFirstName() + " " + user.getLastName();
        StringBuffer msg = new StringBuffer();
        msg.append(getMessageSourceAccessor().getMessage("newuser.email.message",
                fullName));
        msg.append("\n\n" +
                getMessageSourceAccessor().getMessage("user.username"));
        msg.append(": " + user.getUsername() + "\n");
        msg.append(getMessageSourceAccessor().getMessage("user.password") +
                ": ");
        msg.append(user.getPassword());

        msg.append("\n\nLogin at: " + RequestUtil.getAppURL(request) +
        		request.getContextPath());
        String subject =
                getMessageSourceAccessor().getMessage("signup.email.subject");

        // TODO: Change to use Spring's Mail Support
        try {
            // From,to,cc,subject,content
            MailSender.sendTextMessage(Constants.DEFAULT_FROM, user.getEmail(),
                    null, subject, msg.toString());
        } catch (MessagingException me) {
            log.warn("Failed to send Account Information e-mail");
        }
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
        // doh - seems to kill validation on save as well
        if (request.getParameter("delete") != null) {
        	super.setValidateOnBinding(false);
        } else {
        	super.setValidateOnBinding(true);
        }
    }
}
