/**
 * 
 */
package org.appfuse.webapp.server.services.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.ServletContextAware;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;

/**
 * @author ivangsa
 *
 */
public abstract class AbstractBaseRequest implements ServletContextAware, CxfMessageContextAware {

    protected final transient Log log = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("messageSource")
    protected MessageSource messages;
    @Autowired
    protected MailEngine mailEngine;
    @Autowired
    protected SimpleMailMessage message;

    private ServletContext servletContext;
    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    /**
     * CXF JAX-RS MessageContext
     */
    public MessageContext messageContext;

    @Override
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    @Context
    public void setMessageContext(final MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    /**
     * Convenience method for getting a i18n key's value. Calling
     * getMessageSourceAccessor() is used because the RequestContext variable is
     * not set in unit tests b/c there's no DispatchServlet Request.
     *
     * @param msgKey
     * @param locale
     *            the current locale
     * @return
     */
    public String getText(final String msgKey, final Locale locale) {
        return messages.getMessage(msgKey, null, locale);
    }

    /**
     * Convenient method for getting a i18n key's value with a single string
     * argument.
     *
     * @param msgKey
     * @param arg
     * @param locale
     *            the current locale
     * @return
     */
    public String getText(final String msgKey, final String arg, final Locale locale) {
        return getText(msgKey, new Object[] { arg }, locale);
    }

    /**
     * Convenience method for getting a i18n key's value with arguments.
     *
     * @param msgKey
     * @param args
     * @param locale
     *            the current locale
     * @return
     */
    public String getText(final String msgKey, final Object[] args, final Locale locale) {
        return messages.getMessage(msgKey, args, locale);
    }

    /**
     * Convenience message to send messages to users, includes app URL as
     * footer.
     * 
     * @param user
     *            the user to send a message to.
     * @param msg
     *            the message to send.
     * @param url
     *            the URL of the application.
     */
    protected void sendUserMessage(final User user, final String templateName, final String msg, final String url) {
        if (log.isDebugEnabled()) {
            log.debug("sending e-mail to user [" + user.getEmail() + "]...");
        }

        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        final Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);

        // TODO: once you figure out how to get the global resource bundle in
        // WebWork, then figure it out here too. In the meantime, the Username
        // and Password labels are hard-coded into the template.
        // model.put("bundle", getTexts());
        model.put("message", msg);
        model.put("applicationURL", url);
        mailEngine.sendMessage(message, templateName, model);
    }

    protected String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !isAnonymousLogin()) {
            return authentication.getName();
        }
        return null;
    }

    protected boolean isAnonymousLogin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isAnonymous(authentication);
    }

    protected boolean isRememberMeLogin() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authenticationTrustResolver.isRememberMe(authentication);
    }

    protected boolean isFullyAuthenticated() {
        return !isAnonymousLogin() && !isRememberMeLogin();
    }

    /**
     * 
     * @return
     */
    protected HttpServletRequest getServletRequest() {
        final HttpServletRequest request = RequestFactoryServlet.getThreadLocalRequest();
        if (request == null) { // jax-rs
            return messageContext.getHttpServletRequest();
        }
        return request;
    }

    /**
     * 
     * @return
     */
    protected HttpServletResponse getServletResponse() {
        final HttpServletResponse response = RequestFactoryServlet.getThreadLocalResponse();
        if (response == null) { // jax-rs
            return messageContext.getHttpServletResponse();
        }
        return response;
    }

    protected ServletContext getServletContext() {
        return servletContext;
    }
}
