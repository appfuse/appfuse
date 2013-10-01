/**
 * PersistentPasswordTokenManagerTest.java 01/10/2013
 *
 * Copyright 2013 INDITEX.
 * Departamento de Sistemas
 */
package org.appfuse.service.impl;

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
    @Qualifier("persistentPasswordTokenManager")
    public void setPasswordTokenManager(PasswordTokenManager passwordTokenManager) {
        super.setPasswordTokenManager(passwordTokenManager);
    };
}
