/**
 *
 */
package org.appfuse.service.impl;

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
import org.appfuse.service.PasswordRecoveryManager;
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author ivangsa
 */
@Service("passwordRecoveryManager")
public class PasswordRecoveryManagerImpl implements PasswordRecoveryManager {
	private final Log log = LogFactory.getLog(PasswordRecoveryManagerImpl.class);

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

	private final String passwordRecoveryTemplate = "passwordRecovery.vm";
	private final String passwordUpdatedTemplate = "passwordUpdated.vm";

	private final SimpleDateFormat expirationTimeFormat = new SimpleDateFormat("yyyyMMddHHmm");
	private final int expirationTimeTokenLength = this.expirationTimeFormat.toPattern().length();


	/**
     * {@inheritDoc}
     */
	@Override
    public boolean sendPasswordRecoveryEmail(final String username, final String urlTemplate) {
		final User user = this.userManager.getUserByUsername(username);
		final String token = generateRecoveryToken(user);
		if(token != null) {
			final String url = String.format(urlTemplate, username, token);

			this.log.debug("Sending password recovery token url: " + url);

			sendUserEmail(user, this.passwordRecoveryTemplate, url);
			return true;
		}
		return false;
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public boolean isRecoveryTokenValid(final String username, final String token) {
		return isRecoveryTokenValid(this.userManager.getUserByUsername(username), token);
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public User updatePassword(final String username, final String token, final String newPassword, final String applicationUrl) throws UserExistsException {
		User user = this.userManager.getUserByUsername(username);
		if (isRecoveryTokenValid(user, token)) {
			user.setPassword(newPassword);
			user = this.userManager.saveUser(user);

			sendUserEmail(user, this.passwordUpdatedTemplate, applicationUrl);

			return user;
		}
		// or throw exception
		return null;
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public boolean isRecoveryTokenValid(final User user, final String token) {
		if ((user != null) && (token != null)) {
			final String expirationTimeStamp = getTimestamp(token);
			final String tokenWithoutTimestamp = getTokenWithoutTimestamp(token);
			final String tokenSource = expirationTimeStamp + getTokenSource(user);
			final Date expirationTime = parseTimestamp(expirationTimeStamp);

			return (expirationTime != null) && expirationTime.after(new Date())
					&& this.passwordEncoder.isPasswordValid(tokenWithoutTimestamp, tokenSource, this.saltSource);
		}
		return false;
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public String generateRecoveryToken(final User user) {
		if (user != null) {
			final String tokenSource = getTokenSource(user);
			final String expirationTimeStamp = this.expirationTimeFormat.format(getExpirationTime());
			return expirationTimeStamp + this.passwordEncoder.encodePassword(
						expirationTimeStamp + tokenSource, this.saltSource);
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
	private String getTokenSource(final User user) {
		return user.getEmail() + user.getVersion() + user.getPassword();
	}


	private String getTimestamp(final String token) {
		return StringUtils.substring(token, 0, this.expirationTimeTokenLength);
	}

	private String getTokenWithoutTimestamp(final String token) {
		return StringUtils.substring(token, this.expirationTimeTokenLength, token.length());
	}

	private Date parseTimestamp(final String timestamp) {
		try {
			return this.expirationTimeFormat.parse(timestamp);
		} catch (final ParseException e) {
			return null;
		}
	}

	private void sendUserEmail(final User user, final String template, final String url) {
		this.message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

		final Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);
        model.put("url", url);

        this.mailEngine.sendMessage(this.message, template, model);
	}

}
