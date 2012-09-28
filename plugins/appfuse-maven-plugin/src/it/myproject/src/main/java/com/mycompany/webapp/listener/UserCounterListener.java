package com.mycompany.webapp.listener;

import com.mycompany.model.User;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * UserCounterListener class used to count the current number
 * of active users for the applications.  Does this by counting
 * how many user objects are stuffed into the session.  It also grabs
 * these users and exposes them in the servlet context.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserCounterListener implements ServletContextListener, HttpSessionAttributeListener, HttpSessionListener {
    /**
     * Name of user counter variable
     */
    public static final String COUNT_KEY = "userCounter";
    /**
     * Name of users Set in the ServletContext
     */
    public static final String USERS_KEY = "userNames";
    /**
     * The default event we're looking to trap.
     */
    public static final String EVENT_KEY = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
    private transient ServletContext servletContext;
    private int counter;
    private Set<User> users;

    /**
     * Initialize the context
     *
     * @param sce the event
     */
    public synchronized void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.setAttribute((COUNT_KEY), Integer.toString(counter));
    }

    /**
     * Set the servletContext, users and counter to null
     *
     * @param event The servletContextEvent
     */
    public synchronized void contextDestroyed(ServletContextEvent event) {
        servletContext = null;
        users = null;
        counter = 0;
    }

    synchronized void incrementUserCounter() {
        counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter++;
        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
    }

    synchronized void decrementUserCounter() {
        int counter = Integer.parseInt((String) servletContext.getAttribute(COUNT_KEY));
        counter--;

        if (counter < 0) {
            counter = 0;
        }

        servletContext.setAttribute(COUNT_KEY, Integer.toString(counter));
    }

    @SuppressWarnings("unchecked")
    synchronized void addUsername(User user) {
        users = (Set<User>) servletContext.getAttribute(USERS_KEY);

        if (users == null) {
            users = new LinkedHashSet<User>();
        }

        if (!users.contains(user)) {
            users.add(user);
            servletContext.setAttribute(USERS_KEY, users);
            incrementUserCounter();
        }
    }

    @SuppressWarnings("unchecked")
    synchronized void removeUsername(User user) {
        users = (Set<User>) servletContext.getAttribute(USERS_KEY);

        if (users != null) {
            users.remove(user);
        }

        servletContext.setAttribute(USERS_KEY, users);
        decrementUserCounter();
    }

    /**
     * This method is designed to catch when user's login and record their name
     *
     * @param event the event to process
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeAdded(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            SecurityContext securityContext = (SecurityContext) event.getValue();
            if (securityContext != null && securityContext.getAuthentication().getPrincipal() instanceof User) {
                User user = (User) securityContext.getAuthentication().getPrincipal();
                addUsername(user);
            }
        }
    }

    private boolean isAnonymous() {
        AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            return resolver.isAnonymous(auth);
        }
        return true;
    }

    /**
     * When user's logout, remove their name from the hashMap
     *
     * @param event the session binding event
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            SecurityContext securityContext = (SecurityContext) event.getValue();
            Authentication auth = securityContext.getAuthentication();
            if (auth != null && (auth.getPrincipal() instanceof User)) {
                User user = (User) auth.getPrincipal();
                removeUsername(user);
            }
        }
    }

    /**
     * Needed for Acegi Security 1.0, as it adds an anonymous user to the session and
     * then replaces it after authentication. http://forum.springframework.org/showthread.php?p=63593
     *
     * @param event the session binding event
     * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if (event.getName().equals(EVENT_KEY) && !isAnonymous()) {
            final SecurityContext securityContext = (SecurityContext) event.getValue();
            if (securityContext.getAuthentication() != null
                    && securityContext.getAuthentication().getPrincipal() instanceof User) {
                final User user = (User) securityContext.getAuthentication().getPrincipal();
                addUsername(user);
            }
        }
    }
    
    public void sessionCreated(HttpSessionEvent se) { 
    }
    
    public void sessionDestroyed(HttpSessionEvent se) {
        Object obj = se.getSession().getAttribute(EVENT_KEY);
        if (obj != null) {
            se.getSession().removeAttribute(EVENT_KEY);
        }
    }
}
