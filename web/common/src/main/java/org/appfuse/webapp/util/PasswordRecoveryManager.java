/**
 * 
 */
package org.appfuse.webapp.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author ivangsa
 */
@Component
public class PasswordRecoveryManager {
	private final Log log = LogFactory.getLog(PasswordRecoveryManager.class);
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired(required = false)
	private SaltSource saltSource;
	@Autowired
	private MailEngine mailEngine;
	@Autowired
	private SimpleMailMessage message;

	private String passwordRecoveryTemplate = "passwordRecovery.vm";
	private String passwordUpdatedTemplate = "passwordUpdated.vm";


	/**
	 * Sends a password recovery email to username.
	 * 
	 * urlTemplate should include two placeholders (%s) for username and recovery token.
	 * 
	 * @param username
	 * @param urlTemplate Url template string with two placeholders (%s) for username and recovery token
	 * @return true if user exists and password recovery email was sent
	 */
	public boolean sendPasswordRecoveryEmail(String username, String urlTemplate) {
		User user = userManager.getUserByUsername(username);
		String token = generateRecoveryToken(user);
		if(token != null) {
			String url = String.format(urlTemplate, username, token);
			
			log.debug("Sending password recovery token url: " + url);
			
			sendUserEmail(user, passwordRecoveryTemplate, url);
			return true;
		}
		return false;
	}

	public boolean isRecoveryTokenValid(String username, String token) {
		return isRecoveryTokenValid(userManager.getUserByUsername(username), token);
	}

	public User updatePassword(String username, String token, String newPassword, String applicationUrl) throws UserExistsException {
		User user = userManager.getUserByUsername(username);
		if (isRecoveryTokenValid(user, token)) {
			user.setPassword(newPassword);
			user = userManager.saveUser(user);
			
			sendUserEmail(user, passwordUpdatedTemplate, applicationUrl);
			
			return user;
		}
		// or throw exception
		return null;
	}

	public boolean isRecoveryTokenValid(User user, String token) {
		if (user != null) {
			return passwordEncoder.isPasswordValid(token, getTokenSource(user), saltSource);
		}
		return false;
	}
	
	public String generateRecoveryToken(User user) {
		if (user != null) {
			String tokenSource = getTokenSource(user);
			return passwordEncoder.encodePassword(tokenSource, saltSource);
		}
		return null;
	}

	private String getTokenSource(User user) {
		return user.getEmail() + user.getVersion() + user.getPassword();
	}

	private void sendUserEmail(User user, String template, String url) {
		message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);
        model.put("url", url);

        mailEngine.sendMessage(message, template, model);	
	}

}
