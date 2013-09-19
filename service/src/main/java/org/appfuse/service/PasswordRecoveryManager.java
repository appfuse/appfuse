package org.appfuse.service;

import org.appfuse.model.User;

public interface PasswordRecoveryManager {

    /**
     * Sends a password recovery email to username.
     *
     * urlTemplate should include two placeholders (%s) for username and recovery token.
     *
     * @param username
     * @param urlTemplate Url template string with two placeholders (%s) for username and recovery token
     * @return true if user exists and password recovery email was sent
     */
    boolean sendPasswordRecoveryEmail(String username, String urlTemplate);

    /**
     *
     * @param username
     * @param token
     * @return
     */
    boolean isRecoveryTokenValid(String username, String token);

    /**
     *
     * @param username
     * @param token
     * @param newPassword
     * @param applicationUrl
     * @return
     * @throws UserExistsException
     */
    User updatePassword(String username, String token, String newPassword, String applicationUrl) throws UserExistsException;

    /**
     *
     * @param user
     * @param token
     * @return
     */
    boolean isRecoveryTokenValid(User user, String token);

    /**
     *
     * @param user
     * @return
     */
    String generateRecoveryToken(User user);

}