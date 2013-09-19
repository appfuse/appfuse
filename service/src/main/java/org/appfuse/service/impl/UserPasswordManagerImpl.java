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
import org.appfuse.service.UserExistsException;
import org.appfuse.service.UserManager;
import org.appfuse.service.UserPasswordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author ivangsa
 */
@Service("userPasswordManager")
public class UserPasswordManagerImpl implements UserPasswordManager {
	private final Log log = LogFactory.getLog(UserPasswordManagerImpl.class);

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

	private final SimpleDateFormat expirationTimeFormat = new SimpleDateFormat("yyyyMMddHHmm");
	private final int expirationTimeTokenLength = expirationTimeFormat.toPattern().length();

    /**
     * Velocity template name to send users a password recovery mail (default passwordRecovery.vm).
     *
     * @param passwordRecoveryTemplate
     *            the Velocity template to use (relative to classpath)
     * @see MailEngine#sendMessage(SimpleMailMessage, String, Map)
     */
    public void setPasswordRecoveryTemplate(final String passwordRecoveryTemplate) {
        this.passwordRecoveryTemplate = passwordRecoveryTemplate;
    }

    /**
     * Velocity template name to inform users their password was updated (default passwordUpdated.vm).
     *
     * @param passwordUpdatedTemplate
     *            the Velocity template to use (relative to classpath)
     * @see MailEngine#sendMessage(SimpleMailMessage, String, Map)
     */
    public void setPasswordUpdatedTemplate(final String passwordUpdatedTemplate) {
        this.passwordUpdatedTemplate = passwordUpdatedTemplate;
    }

    @Override
    public String buildRecoveryPasswordUrl(final User user, final String urlTemplate) {
        final String token = generateRecoveryToken(user);
        final String username = user.getUsername();
        return StringUtils.replaceEach(urlTemplate,
                new String[] { "{username}", "{token}" },
                new String[] { username, token });
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void sendPasswordRecoveryEmail(final String username, final String urlTemplate) {
        log.debug("Sending password recovery token to user: " + username);

        final User user = userManager.getUserByUsername(username);
        final String url = buildRecoveryPasswordUrl(user, urlTemplate);

        sendUserEmail(user, passwordRecoveryTemplate, url);
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public boolean isRecoveryTokenValid(final String username, final String token) {
		return isRecoveryTokenValid(userManager.getUserByUsername(username), token);
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public User updatePassword(final String username, final String currentPassword, final String recoveryToken, final String newPassword, final String applicationUrl) throws UserExistsException {
		User user = userManager.getUserByUsername(username);
        if (isRecoveryTokenValid(user, recoveryToken)) {
            log.debug("Updating password from recovery token for user:" + username);
			user.setPassword(newPassword);
			user = userManager.saveUser(user);

			sendUserEmail(user, passwordUpdatedTemplate, applicationUrl);

			return user;
        } else if (StringUtils.isNotBlank(currentPassword)) {
            Object salt = saltSource != null ? saltSource.getSalt(user) : null;
            if (passwordEncoder.isPasswordValid(user.getPassword(), currentPassword, salt)) {
                log.debug("Updating password (providing current password) for user:" + username);
                user.setPassword(newPassword);
                user = userManager.saveUser(user);
                return user;
            }
		}
		// or throw exception
		return null;
	}

	/**
     * {@inheritDoc}
     */
	@Override
    public boolean isRecoveryTokenValid(final User user, final String token) {
		if (user != null && token != null) {
			final String expirationTimeStamp = getTimestamp(token);
			final String tokenWithoutTimestamp = getTokenWithoutTimestamp(token);
			final String tokenSource = expirationTimeStamp + getTokenSource(user);
            final Object salt = saltSource != null ? saltSource.getSalt(user) : null;
			final Date expirationTime = parseTimestamp(expirationTimeStamp);

			return expirationTime != null && expirationTime.after(new Date())
                    && passwordEncoder.isPasswordValid(tokenWithoutTimestamp, tokenSource, salt);
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
			final String expirationTimeStamp = expirationTimeFormat.format(getExpirationTime());
            final Object salt = saltSource != null ? saltSource.getSalt(user) : null;
            return expirationTimeStamp + passwordEncoder.encodePassword(expirationTimeStamp + tokenSource, salt);
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
		return StringUtils.substring(token, 0, expirationTimeTokenLength);
	}

	private String getTokenWithoutTimestamp(final String token) {
		return StringUtils.substring(token, expirationTimeTokenLength, token.length());
	}

	private Date parseTimestamp(final String timestamp) {
		try {
			return expirationTimeFormat.parse(timestamp);
		} catch (final ParseException e) {
			return null;
		}
	}

	private void sendUserEmail(final User user, final String template, final String url) {
		message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

		final Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);
        model.put("applicationURL", url);

        mailEngine.sendMessage(message, template, model);
	}

}
