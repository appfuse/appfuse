package org.appfuse.webapp.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;
import org.appfuse.service.LookupManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;


/**
 * StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.4 $ $Date: 2004/04/28 04:45:07 $
 *
 * @web.listener
 */
public class StartupListener implements ServletContextListener {
    private static Log log = LogFactory.getLog(StartupListener.class);
    private ServletContext context;

    public void contextInitialized(ServletContextEvent sce) {
        context = sce.getServletContext();

        if (log.isDebugEnabled()) {
            log.debug("contextInitialized...");
        }

        String daoType = context.getInitParameter(Constants.DAO_TYPE);

        // if daoType is not specified, use DAO as default
        if (daoType == null) {
            log.warn("No 'daoType' Context Parameter supplied in web.xml, using default (hibernate)");
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
        
        // output the retrieved values for the Init and Context Parameters
        if (log.isDebugEnabled()) {
            log.debug("Initialization complete [OK]");
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (log.isDebugEnabled()) {
            log.debug("contextDestroyed...");
        }

        context = null;
    }
    
    public static void setupContext(ServletContext context) {
        // populate drop-downs and stuff in servlet context
        try {
            // check to see if ctx has already been set
            XmlWebApplicationContext ctx =
                (XmlWebApplicationContext) context
                    .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            if (ctx == null) {
                // if the context is null, it's likely because the listeners
                // aren't initialized in the same order they're specified in
                // web.xml.  This happens in Tomcat 5.
                ctx = new XmlWebApplicationContext();
                // get the config locations from context parameters
                String configLocations =
                        context.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM);
                String[] files = configLocations.split(",");
                for (int i=0; i < files.length; i++) {
                    files[i] = files[i].trim();
                }
                ctx.setConfigLocations(files);
                ctx.setServletContext(context);
                ctx.refresh();
            }
            
            LookupManager mgr =
                (LookupManager) ctx.getBean("lookupManager");

            // get list of possible roles
            context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());
        } catch (Exception e) {
            log.error("Error populating drop-downs failed!" + e.getMessage());
            e.printStackTrace();
        }   
    }
}
