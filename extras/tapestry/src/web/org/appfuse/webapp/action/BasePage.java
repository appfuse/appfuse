package org.appfuse.webapp.action;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidationConstraint;
import org.appfuse.Constants;

public abstract class BasePage extends org.apache.tapestry.html.BasePage {
    protected final Log log = LogFactory.getLog(getClass());
    private String message = null;
    private ResourceBundle bundle = null;
    private IValidationDelegate delegate = null;
    private Map global = null;
    
    /**
     * Friendly method for unit tests
     * @param rb
     */
    public void setBundle(ResourceBundle rb) {
        this.bundle = rb;
    }

    /**
     * Friendly method for unit tests
     * @param global
     */
    public void setGlobal(Map global) {
        this.global = global;
    }
    
    
    protected IValidationDelegate getValidationDelegate() {
        if (delegate == null) {
            return (IValidationDelegate) getBeans().getBean("delegate");
        } else {
            return delegate;
        }
    }
    
    /**
     * Convenience method to be friendly to unit tests
     * @param delegate
     */
    protected void setValidationDelegate(IValidationDelegate delegate) {
        this.delegate = delegate;
    }

    protected void addError(IValidationDelegate delegate, String componentId,
                            String message, ValidationConstraint constraint) {
        IFormComponent component = (IFormComponent) getComponent(componentId);

        delegate.setFormComponent(component);
        delegate.record(message, constraint);
    }

    /**
     * Convenience method to get the Configuration HashMap
     * from the servlet context.
     *
     * @return the user's populated form from the session
     */
    protected Map getConfiguration() {
        Map config =
            (HashMap) getServletContext().getAttribute(Constants.CONFIG);

        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }

        return config;
    }

    /**
     * Set success message 
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get success message 
     * @return
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Convenience method for unit tests.
     * @return
     */
    public boolean hasErrors() {
        return (getSession().getAttribute("errors") != null);
    }
    
    /**
     * Servlet API Convenience method
     * @return
     */
    public HttpServletRequest getRequest() {
        return getRequestCycle().getRequestContext().getRequest();
    }

    /**
     * Servlet API Convenience method
     * @return
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * Servlet API Convenience method
     * @return
     */
    public HttpServletResponse getResponse() {
        return getRequestCycle().getRequestContext().getResponse();
    }
    
    /**
     * Servlet API Convenience method
     * @return
     */
    public ServletContext getServletContext() {
        return getRequestCycle().getRequestContext().getServlet().getServletContext();
    }
    
    /**
     * Be friendly to unit tests
     */
    public Object getGlobal() {
        if (global == null) {
            return super.getGlobal();
        } else {
            return this.global;
        }        
    }
    
    /**
     * This is merely a convenience method to allow unit tests to pass
     */
    public String getMessage(String key) {
        if (bundle != null) {
            return bundle.getString(key);
        } else {
            return super.getMessage(key);
        }
    }
    
    public String format(String key, Object obj1) {
        if (bundle != null) {
            return getText(key, obj1);
        } else {
            return super.format(key, obj1);
        }
    }
    
    public String format(String key, Object obj1, Object obj2) {
        if (bundle != null) {
            return getText(key, new Object[] {obj1, obj1});
        } else {
            return super.format(key, obj1, obj2);
        }
    }
    
    private String getText(String key, Object arg) {
        if (arg == null) {
            return bundle.getString(key);
        }

        MessageFormat form = new MessageFormat(bundle.getString(key));

        if (arg instanceof String) {
            return form.format(new Object[] { arg });
        } else if (arg instanceof Object[]) {
            return form.format(arg);
        } else {
            log.error("arg '" + arg + "' not String or Object[]");

            return "";
        }
    }

    
}
