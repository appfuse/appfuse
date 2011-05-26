package org.appfuse.webapp.services;

import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;
import java.util.Map;

/**
 * This interface wraps most services to minimize repeated injections
 * 
 * @version $Id: ServiceFacade.java 5 2008-08-30 09:59:21Z serge.eby $
 * @author Serge Eby 
 * 
 */
public interface ServiceFacade {

    UserManager getUserManager();
    RoleManager getRoleManager();
    MailEngine getMailEngine();
    SimpleMailMessage getMailMessage();
    List<String> getAvailableRoles();
    Map<String, String> getAvailableCountries();
}
