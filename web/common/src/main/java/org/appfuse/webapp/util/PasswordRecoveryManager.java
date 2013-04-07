/**
 * 
 */
package org.appfuse.webapp.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
	
	private SimpleDateFormat expirationTimeFormat = new SimpleDateFormat("yyyyMMddHHmm");
	private int expirationTimeTokenLength = expirationTimeFormat.toPattern().length();


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

	/**
	 * 
	 * @param username
	 * @param token
	 * @return
	 */
	public boolean isRecoveryTokenValid(String username, String token) {
		return isRecoveryTokenValid(userManager.getUserByUsername(username), token);
	}

	/**
	 * 
	 * @param username
	 * @param token
	 * @param newPassword
	 * @param applicationUrl
	 * @return
	 * @throws UserExistsException
	 */
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

	/**
	 * 
	 * @param user
	 * @param token
	 * @return
	 */
	public boolean isRecoveryTokenValid(User user, String token) {
		if (user != null && token != null) {
			String expirationTimeStamp = getTimestamp(token);
			String tokenWithoutTimestamp = getTokenWithoutTimestamp(token);
			String tokenSource = expirationTimeStamp + getTokenSource(user);
			Date expirationTime = parseTimestamp(expirationTimeStamp);
			
			return expirationTime != null && expirationTime.after(new Date()) 
					&& passwordEncoder.isPasswordValid(tokenWithoutTimestamp, tokenSource, saltSource);
		}
		return false;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public String generateRecoveryToken(User user) {
		if (user != null) {
			String tokenSource = getTokenSource(user);
			String expirationTimeStamp = expirationTimeFormat.format(getExpirationTime());
			return expirationTimeStamp + passwordEncoder.encodePassword(
						expirationTimeStamp + tokenSource, saltSource);
		}
		return null;
	}

	/**
	 * Return tokens expiration time, now + 1 day.
	 * @return
	 */
	private Date getExpirationTime() {
		return DateUtils.addDays(new Date(), 1);
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	private String getTokenSource(User user) {
		return user.getEmail() + user.getVersion() + user.getPassword();
	}
	
	
	private String getTimestamp(String token) {
		return StringUtils.substring(token, 0, expirationTimeTokenLength);
	}

	private String getTokenWithoutTimestamp(String token) {
		return StringUtils.substring(token, expirationTimeTokenLength, token.length());
	}
	
	private Date parseTimestamp(String timestamp) {
		try {
			return expirationTimeFormat.parse(timestamp);
		} catch (ParseException e) {
			return null;
		}
	}
	
	private void sendUserEmail(User user, String template, String url) {
		message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);
        model.put("url", url);

        mailEngine.sendMessage(message, template, model);	
	}

}
