package org.appfuse.webapp.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.service.LookupManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;


/**
 * StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.8 $ $Date: 2004/07/13 05:04:21 $
 *
 * @web.listener
 */
public class StartupListener extends ContextLoaderListener
        implements ServletContextListener {
    private static Log log = LogFactory.getLog(StartupListener.class);

    public void contextInitialized(ServletContextEvent event) {
        if (log.isDebugEnabled()) {
            log.debug("initializing context...");
        }
        // call Spring's context ContextLoaderListener to initialize
        // all the context files specified in web.xml
        super.contextInitialized(event);

        ServletContext context = event.getServletContext();
        String daoType = context.getInitParameter(Constants.DAO_TYPE);

        // if daoType is not specified, use DAO as default
        if (daoType == null) {
            log.warn("No 'daoType' context carameter, using hibernate");
            daoType = Constants.DAO_TYPE_HIBERNATE;
        }

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map config = (HashMap) context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap();
        }

        // Create a config object to hold all the app config values
        config.put(Constants.DAO_TYPE, daoType);
        context.setAttribute(Constants.CONFIG, config);

        // output the retrieved values for the Init and Context Parameters
        if (log.isDebugEnabled()) {
            log.debug("daoType: " + daoType);
            log.debug("populating drop-downs...");
        }

        setupContext(context);
    }

    public static void setupContext(ServletContext context) {
        // populate drop-downs and stuff in servlet context
        try {
            // check to see if ctx has already been set
            ApplicationContext ctx =
                (ApplicationContext) context
                    .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

            LookupManager mgr =
                (LookupManager) ctx.getBean("lookupManager");

            // get list of possible roles
            context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());

            if (log.isDebugEnabled()) {
                log.debug("drop-down initialization complete [OK]");
            }
        } catch (Exception e) {
            log.error("Error populating drop-downs failed!" + e.getMessage());
            e.printStackTrace();
        }   
    }
}
