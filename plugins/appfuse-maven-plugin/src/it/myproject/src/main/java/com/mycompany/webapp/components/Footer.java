package com.mycompany.webapp.components;

import org.apache.tapestry5.ioc.annotations.Inject;
import com.mycompany.webapp.services.SecurityContext;

/**
 * Footer component
 *
 * @author Serge Eby
 */
public class Footer {

    @Inject
    private SecurityContext securityContext;

    public boolean isLoggedIn() {
        return securityContext.isLoggedIn();
    }

    public String getUsername() {
        return securityContext.getUsername();
    }

}
