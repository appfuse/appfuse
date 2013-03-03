package org.appfuse.webapp.server.requests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.listener.UserCounterListener;
import org.appfuse.webapp.proxies.RoleProxy;
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

	
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    
    /**
     * 
     * @return
     */
    public User getCurrentUser() {
    	String username = getCurrentUsername();
    	if(username != null) {
    		User user = userManager.getUserByUsername(username);
    		if(isFullyAuthenticated()) {
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
    public User signUp() {
    	return new User();
    }

    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public User signUp(User user) throws Exception {
        user.setEnabled(true);
        // Set the default user role on this new user
        user.getRoles().clear();
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            userManager.saveUser(user);
        } catch (UserExistsException e) {
        	log.debug(String.format("Trying to duplicate user username=%s, email=%d", user.getUsername(), user.getEmail()), e);
//            errors.rejectValue("username", "errors.existing.user",
//                    new Object[]{user.getUsername(), user.getEmail()}, "duplicate user");
        	throw e;
        }


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
        Locale locale = LocaleContextHolder.getLocale();
        message.setSubject(getText("signup.email.subject", locale));

        try {
            sendUserMessage(user, "accountCreated.vm", getText("signup.email.message", locale), RequestUtil.getAppURL(getServletRequest()));
        } catch (MailException me) {
            //saveError(request, me.getMostSpecificCause().getMessage());
        	throw me;
        }
        
        return user;
    }

    
    /**
     * 
     * @return
     */
    public User editProfile() {
    	String username = getCurrentUsername();
    	return userManager.getUserByUsername(username);
    }
    
    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public User editProfile(User user) throws Exception {
    	String username = getCurrentUsername();
    	if(!username.equals(user.getUsername())) {
    		throw new AccessDeniedException("Trying to edit another users profile");
    	}
    	return userManager.saveUser(user);
    }
    
    /**
     * 
     * @param userId
     * @return
     */
    public User getUser(Long userId) {
    	return userManager.get(userId);
    }
    
    /**
     * 
     * @param user
     * @return
     * @throws Exception
     */
    public User saveUser(User user) throws Exception {
    	return userManager.saveUser(user);
    }
    
    /**
     * 
     * @param searchCriteria
     * @return
     */
    public long countUsers(UsersSearchCriteria searchCriteria) {
    	String searchTerm = searchCriteria != null? searchCriteria.getSearchTerm() : null;
   		return userManager.search(searchTerm).size();
    }

    /**
     * 
     * @param searchCriteria
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<User> searchUsers(UsersSearchCriteria searchCriteria, int firstResult, int maxResults){
    	return searchUsers(searchCriteria, firstResult, maxResults, null, true);
    }

    /**
     * 
     * @param searchCriteria
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<User> searchUsers(UsersSearchCriteria searchCriteria, int firstResult, int maxResults, String sortProperty, boolean ascending){
    	String searchTerm = searchCriteria != null? searchCriteria.getSearchTerm() : null;
    	List<User> users = userManager.search(searchTerm);
    	if(StringUtils.isNotEmpty(sortProperty)) {
    		log.debug(String.format("Sorting usersList by property='%s', ascending='%s'", sortProperty, ascending));
    		Collections.sort(users, new PropertyComparator(sortProperty, true, ascending));
    	}
    	int fromIndex = Math.min(firstResult, users.size());
    	int toIndex = Math.min(fromIndex + maxResults, users.size());
    	log.debug(String.format("searchUsers(%d,%d) %d-%d [%d]", firstResult, maxResults, fromIndex, toIndex, users.size()));
    	return users.subList(fromIndex, toIndex);
    }

    /**
     * 
     * @param user
     */
    public void removeUser(Long userId) {
    	userManager.removeUser(userId.toString());
    }

    /**
     * 
     * @param username
     * @return
     */
    public String sendPasswordHint(String username) {
    	Locale locale = getServletRequest().getLocale();
    	
        // ensure that the username has been sent
        if (username == null) {
            log.warn("Username not specified, notifying user that it's a required field.");
            return null;
        }

        log.debug("Processing Password Hint...");

        // look up the user's information
        try {
            User user = userManager.getUserByUsername(username);

            StringBuffer msg = new StringBuffer();
            msg.append("Your password hint is: ").append(user.getPasswordHint());
            msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(getServletRequest()));

            message.setTo(user.getEmail());
            String subject = 
            		'[' +getText("webapp.name", locale) + "] " + 
            		getText("user.passwordHint", locale);
            message.setSubject(subject);
            message.setText(msg.toString());
            mailEngine.send(message);
            return user.getFullName();//XXX disabling returning user.getEmail();
        } catch (UsernameNotFoundException e) {
            log.warn(e.getMessage());
        } catch (MailException me) {
            log.warn(me.getMessage());
        }
        return null;
    }
    
    /**
     * 
     * @return
     */
    public List<User> getActiveUsers(){
    	return new ArrayList((Set) getServletContext().getAttribute(UserCounterListener.USERS_KEY));    	
    }
    
    
    /**
     * 
     */
    public boolean logout() {
    	HttpServletRequest request = getServletRequest();
    	HttpServletResponse response = getServletResponse();
    	if (request.getSession(false) != null) {
    	    request.getSession(false).invalidate();
    	}
    	Cookie terminate = new Cookie(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null);
    	String contextPath = request.getContextPath();
    	terminate.setPath(contextPath != null && contextPath.length() > 0 ? contextPath : "/");
    	terminate.setMaxAge(0);
    	response.addCookie(terminate);
    	return true;
    }
}
