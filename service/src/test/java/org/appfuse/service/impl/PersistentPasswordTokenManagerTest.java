package org.appfuse.service.impl;

import org.appfuse.service.UserManager;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 
 * @author ivangsa
 * 
 */
@Ignore("create table password_reset_token before running this test")
public class PersistentPasswordTokenManagerTest extends PasswordTokenManagerTest {

    @Autowired
    @Qualifier("persistentPasswordTokenManager.userManager")
    public void setUserManager(UserManager userManager) {
	    super.setUserManager(userManager);
    }

    @Autowired
    @Qualifier("persistentPasswordTokenManager")
    public void setPasswordTokenManager(PasswordTokenManager passwordTokenManager) {
        super.setPasswordTokenManager(passwordTokenManager);
    }
}
