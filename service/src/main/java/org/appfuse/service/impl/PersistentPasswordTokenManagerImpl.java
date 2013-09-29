/**
 * 
 */
package org.appfuse.service.impl;

import org.appfuse.model.User;

/**
 * 
 *
 */
public class PersistentPasswordTokenManagerImpl implements PasswordTokenManager {

    /**
     * @see org.appfuse.service.impl.PasswordTokenManager#generateRecoveryToken(org.appfuse.model.User)
     */
    @Override
    public String generateRecoveryToken(final User user) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * @see org.appfuse.service.impl.PasswordTokenManager#isRecoveryTokenValid(org.appfuse.model.User, java.lang.String)
     */
    @Override
    public boolean isRecoveryTokenValid(final User user, final String token) {
	// TODO Auto-generated method stub
	return false;
    }

}
