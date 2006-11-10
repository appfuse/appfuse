package org.appfuse.webapp.listener;

import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.service.LookupManager;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 * 
 * <p>Keep in mind that this listener is executed outside of OpenSessionInViewFilter,
 * so if you're using Hibernate you'll have to explicitly initialize all loaded data at the 
 * GenericDao or service level to avoid LazyInitializationException. Hibernate.initialize() works
 * well for doing this.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class StartupListener implements ServletContextListener {
    private static final Log log = LogFactory.getLog(StartupListener.class);

    public void contextInitialized(ServletContextEvent event) {
        log.debug("initializing context...");

        ServletContext context = event.getServletContext();

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map<String, Object> config = (HashMap<String, Object>) context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap<String, Object>();
        }
        
        if (context.getInitParameter(Constants.CSS_THEME) != null) {
            config.put(Constants.CSS_THEME, context.getInitParameter(Constants.CSS_THEME));
        }

        ApplicationContext ctx =
            WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        boolean encryptPassword = false;
        try {
            ProviderManager provider = (ProviderManager) ctx.getBean("authenticationManager");
            for (Object o : provider.getProviders()) {
                AuthenticationProvider p = (AuthenticationProvider) o;
                if (p instanceof RememberMeAuthenticationProvider) {
                    config.put("rememberMeEnabled", Boolean.TRUE);
                }
            }

            if (ctx.containsBean("passwordEncoder")) {
                encryptPassword = true;
                config.put(Constants.ENCRYPT_PASSWORD, Boolean.TRUE);
                String algorithm = "SHA";
                if (ctx.getBean("passwordEncoder") instanceof Md5PasswordEncoder) {
                    algorithm = "MD5";
                }
                config.put(Constants.ENC_ALGORITHM, algorithm);
            }
        } catch (NoSuchBeanDefinitionException n) {
            // ignore, should only happen when testing
        }

        context.setAttribute(Constants.CONFIG, config);

        // output the retrieved values for the Init and Context Parameters
        if (log.isDebugEnabled()) {
            log.debug("Remember Me Enabled? " + config.get("rememberMeEnabled"));
            log.debug("Encrypt Passwords? " + encryptPassword);
            if (encryptPassword) {
                log.debug("Encryption Algorithm: " + config.get(Constants.ENC_ALGORITHM));
            }
            log.debug("Populating drop-downs...");
        }

        setupContext(context);
    }

    /**
     * This method uses the LookupManager to lookup available roles from the data layer.
     * @param context The servlet context
     */
    public static void setupContext(ServletContext context) {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");

        // get list of possible roles
        context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());
        log.debug("Drop-down initialization complete [OK]");
    }

    /**
     * This is a no-op method.
     * @param servletContextEvent The servlet context event
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
