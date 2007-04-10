package org.appfuse.webapp.action;

import java.io.Serializable;
import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.MailEngine;
import org.appfuse.service.UserManager;

import org.springframework.mail.SimpleMailMessage;

public class BasePage {
    public static final String jstlBundleParam = "javax.servlet.jsp.jstl.fmt.localizationContext";
    protected final Log log = LogFactory.getLog(getClass());
    protected UserManager userManager = null;
    protected MailEngine mailEngine = null;
    protected SimpleMailMessage message = null;
    protected String templateName = null;
    protected FacesContext facesContext = null;
    protected String sortColumn = null;
    protected boolean ascending = true;
    protected boolean nullsAreHigh = true;

    /**
     * Allow overriding of facesContext for unit tests
     * @param facesContext the current context
     */
    public void setFacesContext(FacesContext facesContext) {
        this.facesContext = facesContext;
    }
    
    public FacesContext getFacesContext() {
        if (facesContext != null){
            // for unit tests
            return facesContext;
        }
        return FacesContext.getCurrentInstance();
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    // Convenience methods ====================================================
    public String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public String getBundleName() {
        // get name of resource bundle from JSTL settings, JSF makes this too hard
        return getServletContext().getInitParameter(jstlBundleParam);
    }

    public Map getCountries() {
        CountryModel model = new CountryModel();

        return model.getCountries(getRequest().getLocale());
    }

    public ResourceBundle getBundle() {
        ClassLoader classLoader =  Thread.currentThread().getContextClassLoader();
        return ResourceBundle.getBundle(getBundleName(), getRequest().getLocale(), classLoader);
    }

    public String getText(String key) {
        String message;

        try {
            message = getBundle().getString(key);
        } catch (java.util.MissingResourceException mre) {
            log.warn("Missing key for '" + key + "'");
            return "???" + key + "???";
        }

        return message;
    }

    public String getText(String key, Object arg) {
        if (arg == null) {
            return getText(key);
        }

        MessageFormat form = new MessageFormat(getBundle().getString(key));

        if (arg instanceof String) {
            return form.format(new Object[] { arg });
        } else if (arg instanceof Object[]) {
            return form.format(arg);
        } else {
            log.error("arg '" + arg + "' not String or Object[]");

            return "";
        }
    }

    @SuppressWarnings("unchecked")
	protected void addMessage(String key, Object arg) {
        // JSF Success Messages won't live past a redirect, so it's not used
        // FacesUtils.addInfoMessage(formatMessage(key, arg));
        List<String> messages = (List) getSession().getAttribute("messages");

        if (messages == null) {
            messages = new ArrayList<String>();
        }

        messages.add(getText(key, arg));
        getSession().setAttribute("messages", messages);
    }

    protected void addMessage(String key) {
        addMessage(key, null);
    }

    @SuppressWarnings("unchecked")
	protected void addError(String key, Object arg) {
        // The "JSF Way" doesn't allow you to put HTML in your error messages, so I don't use it.
        // FacesUtils.addErrorMessage(formatMessage(key, arg));
        List<String> errors = (List) getSession().getAttribute("errors");

        if (errors == null) {
            errors = new ArrayList<String>();
        }

        errors.add(getText(key, arg));

        getSession().setAttribute("errors", errors);
    }

    protected void addError(String key) {
        addError(key, null);
    }
    
    /**
     * Convenience method for unit tests.
     * @return boolean indicator of an "errors" attribute in the session
     */
    public boolean hasErrors() {
        return (getSession().getAttribute("errors") != null);
    }

    /**
     * Servlet API Convenience method
     * @return HttpServletRequest from the FacesContext
     */
    protected HttpServletRequest getRequest() {
        return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
    }

    /**
     * Servlet API Convenience method
     * @return the current user's session
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * Servlet API Convenience method
     * @return HttpServletResponse from the FacesContext
     */
    protected HttpServletResponse getResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }

    /**
     * Servlet API Convenience method
     * @return the ServletContext form the FacesContext
     */
    protected ServletContext getServletContext() {
        return (ServletContext) getFacesContext().getExternalContext().getContext();
    }

    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     *
     * @return the user's populated form from the session
     */
    protected Map getConfiguration() {
        Map config = (HashMap) getServletContext().getAttribute(Constants.CONFIG);

        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }

        return config;
    }

    /**
     * Convenience message to send messages to users, includes app URL as footer.
     * @param user the user to send the message to
     * @param msg the message to send
     * @param url the application's URL
     */
    protected void sendUserMessage(User user, String msg, String url) {
        if (log.isDebugEnabled()) {
            log.debug("sending e-mail to user [" + user.getEmail() + "]...");
        }

        message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);

        // TODO: once you figure out how to get the global resource bundle in
        // WebWork, then figure it out here too.  In the meantime, the Username
        // and Password labels are hard-coded into the template. 
        // model.put("bundle", getTexts());
        model.put("message", msg);
        model.put("applicationURL", url);
        mailEngine.sendMessage(message, templateName, model);
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    // The following methods are used by t:dataTable for sorting.
    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Sort list according to which column has been clicked on.
     * @param list the java.util.List to sort
     * @return ordered list
     */
    @SuppressWarnings("unchecked")
    protected List sort(List list) {
        Comparator comparator = new BeanComparator(sortColumn, new NullComparator(nullsAreHigh));
        if (!ascending) {
            comparator = new ReverseComparator(comparator);
        }
        Collections.sort(list, comparator);
        return list;
    }
}
