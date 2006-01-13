package org.appfuse.webapp.listener;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.acegisecurity.context.HttpSessionContextIntegrationFilter;
import org.acegisecurity.context.SecurityContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;


/**
 * UserCounterListener class used to count the current number
 * of active users for the applications.  Does this by counting
 * how many user objects are stuffed into the session.  It Also grabs
 * these users and exposes them in the servlet context.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *
 * @web.listener
 */
public class UserCounterListener implements ServletContextListener,
                                            HttpSessionAttributeListener {
    public static final String COUNT_KEY = "userCounter";
    public static final String USERS_KEY = "userNames";
    public static final String EVENT_KEY = HttpSessionContextIntegrationFilter.ACEGI_SECURITY_CONTEXT_KEY;
    private final transient Log log = LogFactory.getLog(UserCounterListener.class);
    private transient ServletContext servletContext;
    private int counter;
    private Set users;

    public synchronized void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.setAttribute((COUNT_KEY), Integer.toString(counter));
    }

    public synchronized void contextDestroyed(ServletContextEvent event) {
        servletContext = null;
        users = null;
        counter = 0;
    }

    synchronized void incrementUserCounter() {
        counter =
            Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter++;
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));

        if (log.isDebugEnabled()) {
            log.debug("User Count: " + counter);
        }
    }

    synchronized void decrementUserCounter() {
        int counter =
            Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter--;

        if (counter < 0) {
            counter = 0;
        }

        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));

        if (log.isDebugEnabled()) {
            log.debug("User Count: " + counter);
        }
    }

    synchronized void addUsername(Object user) {
        users = (Set) servletContext.getAttribute(USERS_KEY);

        if (users == null) {
            users = new HashSet();
        }

        if (log.isDebugEnabled()) {
            if (users.contains(user)) {
                log.debug("User already logged in, adding anyway...");
            }
        }

        users.add(user);
        servletContext.setAttribute(USERS_KEY, users);
        incrementUserCounter();
    }

    synchronized void removeUsername(Object user) {
        users = (Set) servletContext.getAttribute(USERS_KEY);

        if (users != null) {
            users.remove(user);
        }

        servletContext.setAttribute(USERS_KEY, users);
        decrementUserCounter();
    }

    /**
    * This method is designed to catch when user's login and record their name
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent event) {
        log.debug("event.name: " + event.getName());
        if (event.getName().equals(EVENT_KEY)) {
            SecurityContext securityContext = (SecurityContext) event.getValue();
            User user = (User) securityContext.getAuthentication().getPrincipal();
            addUsername(user);
        }
    }

    /**
    * When user's logout, remove their name from the hashMap
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY)) {
            SecurityContext securityContext = (SecurityContext) event.getValue();
            User user = (User) securityContext.getAuthentication().getPrincipal();
            removeUsername(user);
        }
    }

    /**
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        // I don't really care if the user changes their information
    }
}
