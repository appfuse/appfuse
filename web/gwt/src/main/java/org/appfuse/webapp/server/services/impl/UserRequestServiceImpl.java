package org.appfuse.webapp.server.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.client.proxies.RoleProxy;
import org.appfuse.webapp.listener.UserCounterListener;
import org.appfuse.webapp.server.services.UserRequestService;
import org.appfuse.webapp.server.services.UsersSearchCriteria;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

@Component("userRequestService")
public class UserRequestServiceImpl extends AbstractBaseRequest implements UserRequestService {

    public static final String RECOVERY_PASSWORD_TEMPLATE = "/#updatePassword:username={username}!token={token}";

    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;

    /**
     * 
     * @return
     */
    @Override
    public User getCurrentUser() {
        final String username = getCurrentUsername();
        if (username != null) {
            final User user = userManager.getUserByUsername(username);
            if (isFullyAuthenticated()) {
                user.getRoles().add(new Role(RoleProxy.FULLY_AUTHENTICATED));
            }
            return user;
        }
        return null;
    }

    /**
     * 
     * @return
     */
    @Override
    public User signUp() {
        return new User();
    }

    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public User signUp(final User user) throws Exception {
        user.setEnabled(true);
        // Set the default user role on this new user
        user.getRoles().clear();
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            userManager.saveUser(user);
        } catch (final UserExistsException e) {
            log.debug(String.format("Trying to duplicate user username=%s, email=%d", user.getUsername(), user.getEmail()), e);
            // errors.rejectValue("username", "errors.existing.user",
            // new Object[]{user.getUsername(), user.getEmail()},
            // "duplicate user");
            throw e;
        }

        // log user in automatically
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");
        }

        // Send an account information e-mail
        final Locale locale = LocaleContextHolder.getLocale();
        message.setSubject(getText("signup.email.subject", locale));

        try {
            sendUserMessage(user, "accountCreated.vm", getText("signup.email.message", locale), RequestUtil.getAppURL(getServletRequest()));
        } catch (final MailException me) {
            // saveError(request, me.getMostSpecificCause().getMessage());
            throw me;
        }

        return user;
    }

    /**
     * 
     * @return
     */
    @Override
    public User editProfile() {
        final String username = getCurrentUsername();
        final User user = userManager.getUserByUsername(username);
        user.setConfirmPassword(user.getPassword());
        return user;
    }

    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public User editProfile(final User user) throws Exception {
        final String username = getCurrentUsername();
        if (!username.equals(user.getUsername())) {
            throw new AccessDeniedException("Trying to edit another users profile");
        }
        return userManager.saveUser(user);
    }

    /**
     * 
     * @param userId
     * @return
     */
    @Override
    public User getUser(final Long userId) {
        final User user = userManager.get(userId);
        user.setConfirmPassword(user.getPassword());
        return user;
    }

    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public User saveUser(final User user) throws Exception {
        if (user.getVersion() == null && user.getPassword() == null) {
            final int length = RandomUtils.nextInt(8) + 8;
            user.setPassword(RandomStringUtils.randomAlphanumeric(length));
        }
        return userManager.saveUser(user);
    }

    /**
     * 
     * @param searchCriteria
     * @return
     */
    @Override
    public long countUsers(final UsersSearchCriteria searchCriteria) {
        final String searchTerm = searchCriteria != null ? searchCriteria.getSearchTerm() : null;
        return userManager.search(searchTerm).size();
    }

    /**
     * 
     * @param searchCriteria
     * @param firstResult
     * @param maxResults
     * @return
     */
    @Override
    public List<User> searchUsers(final UsersSearchCriteria searchCriteria, final int firstResult, final int maxResults) {
        return searchUsers(searchCriteria, firstResult, maxResults, null, true);
    }

    /**
     * 
     * @param searchCriteria
     * @param firstResult
     * @param maxResults
     * @return
     */
    @Override
    public List<User> searchUsers(final UsersSearchCriteria searchCriteria, final int firstResult, final int maxResults, final String sortProperty, final boolean ascending) {
        final String searchTerm = searchCriteria != null ? searchCriteria.getSearchTerm() : null;
        final List<User> users = userManager.search(searchTerm);
        if (StringUtils.isNotEmpty(sortProperty)) {
            log.debug(String.format("Sorting usersList by property='%s', ascending='%s'", sortProperty, ascending));
            Collections.sort(users, new PropertyComparator(sortProperty, true, ascending));
        }
        final int fromIndex = Math.min(firstResult, users.size());
        final int toIndex = Math.min(fromIndex + maxResults, users.size());
        log.debug(String.format("searchUsers(%d,%d) %d-%d [%d]", firstResult, maxResults, fromIndex, toIndex, users.size()));
        return users.subList(fromIndex, toIndex);
    }

    /**
     * 
     * @param user
     */
    @Override
    public void removeUser(final Long userId) {
        userManager.removeUser(userId.toString());
    }

    /**
     * 
     * @param username
     * @return
     */
    @Override
    public String sendPasswordHint(final String username) {
        final Locale locale = LocaleContextHolder.getLocale();

        // ensure that the username has been sent
        if (username == null) {
            log.warn("Username not specified, notifying user that it's a required field.");
            return null;
        }

        log.debug("Processing Password Hint...");

        // look up the user's information
        try {
            final User user = userManager.getUserByUsername(username);

            final StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: ").append(user.getPasswordHint());
            msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(getServletRequest()));

            message.setTo(user.getEmail());
            final String subject =
                    '[' + getText("webapp.name", locale) + "] " +
                            getText("user.passwordHint", locale);
            message.setSubject(subject);
            message.setText(msg.toString());
            mailEngine.send(message);
            return user.getFullName();// XXX disabling returning
                                      // user.getEmail();
        } catch (final UsernameNotFoundException e) {
            log.warn(e.getMessage());
        } catch (final MailException me) {
            log.warn(me.getMessage());
        }
        return null;
    }

    /**
     * 
     * @param username
     * @return
     */
    @Override
    public String requestRecoveryToken(final String username) {
        final Locale locale = LocaleContextHolder.getLocale();

        log.debug("Sending recovery token to user " + username);
        try {
            userManager.sendPasswordRecoveryEmail(username, RequestUtil.getAppURL(getServletRequest()) + RECOVERY_PASSWORD_TEMPLATE);
        } catch (final UsernameNotFoundException ignored) {
            // lets ignore this
        }
        return getText("updatePassword.recoveryToken.sent", locale);
    }

    /**
     * 
     * @param username
     * @param token
     * @param currentPassword
     * @param password
     * @return
     * @throws UserExistsException
     */
    @Override
    public User updatePassword(
            final String username,
            final String token,
            final String currentPassword,
            final String password)
            throws UserExistsException {

        final HttpServletRequest request = getServletRequest();

        if (StringUtils.isEmpty(password)) {
            return null;
        }

        User user = null;
        final boolean usingToken = StringUtils.isNotBlank(token);
        if (usingToken) {
            log.debug("Updating Password for username " + username + ", using reset token");
            user = userManager.updatePassword(username, null, token, password, RequestUtil.getAppURL(request));

        } else {
            log.debug("Updating Password for username " + username + ", using current password");
            if (!username.equals(getCurrentUser().getUsername())) {
                throw new AccessDeniedException("You do not have permission to modify other users password.");
            }
            user = userManager.updatePassword(username, currentPassword, null, password,
                    RequestUtil.getAppURL(request));
        }

        return user;
    }

    /**
     * 
     * @return
     */
    @Override
    public List<User> getActiveUsers() {
        return new ArrayList((Set) getServletContext().getAttribute(UserCounterListener.USERS_KEY));
    }

    /**
     * 
     */
    @Override
    public boolean logout() {
        final HttpServletRequest request = getServletRequest();
        final HttpServletResponse response = getServletResponse();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        final Cookie terminate = new Cookie(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null);
        final String contextPath = request.getContextPath();
        terminate.setPath(contextPath != null && contextPath.length() > 0 ? contextPath : "/");
        terminate.setMaxAge(0);
        response.addCookie(terminate);
        return true;
    }
}
