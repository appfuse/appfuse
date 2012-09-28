package com.mycompany.webapp.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.mycompany.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>This class is designed to put all the public variables in a class to a
 * specified scope - designed for exposing a Constants class to Tag
 * Libraries.</p>
 *
 * <p>It is designed to be used as follows:
 * <pre>&lt;tag:constants /&gt;</pre>
 * </p>
 *
 * <p>Optional values are "className" (fully qualified) and "scope".</p>
 *
 * <p>
 * <a href="BaseAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class ConstantsTag extends TagSupport {
    private static final long serialVersionUID = 3258417209566116146L;
    private final Log log = LogFactory.getLog(ConstantsTag.class);

    /**
     * The class to expose the variables from.
     */
    private String clazz = Constants.class.getName();

    /**
     * The scope to be put the variable in.
     */
    protected String scope;

    /**
     * The single variable to expose.
     */
    protected String var;

    /**
     * Main method that does processing and exposes Constants in specified scope 
     * @return int
     * @throws JspException if processing fails
     */
    @Override
    public int doStartTag() throws JspException {
        // Using reflection, get the available field names in the class
        Class c = null;
        int toScope = PageContext.PAGE_SCOPE;

        if (scope != null) {
            toScope = getScope(scope);
        }

        try {
            c = Class.forName(clazz);
        } catch (ClassNotFoundException cnf) {
            log.error("ClassNotFound - maybe a typo?");
            throw new JspException(cnf.getMessage());
        }

        try {
            // if var is null, expose all variables
            if (var == null) {
                Field[] fields = c.getDeclaredFields();

                AccessibleObject.setAccessible(fields, true);

                for (Field field : fields) {
                    pageContext.setAttribute(field.getName(), field.get(this), toScope);
                }
            } else {
                try {
                    Object value = c.getField(var).get(this);
                    pageContext.setAttribute(c.getField(var).getName(), value, toScope);
                } catch (NoSuchFieldException nsf) {
                    log.error(nsf.getMessage());
                    throw new JspException(nsf);
                }
            }
        } catch (IllegalAccessException iae) {
            log.error("Illegal Access Exception - maybe a classloader issue?");
            throw new JspException(iae);
        }

        // Continue processing this page
        return (SKIP_BODY);
    }

    public void setClassName(String clazz) {
        this.clazz = clazz;
    }

    public String getClassName() {
        return this.clazz;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return (this.scope);
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return (this.var);
    }

    /**
     * Release all allocated resources.
     */
    public void release() {
        super.release();
        clazz = null;
        scope = Constants.class.getName();
    }

    /**
     * Maps lowercase JSP scope names to their PageContext integer constant
     * values.
     */
    private static final Map<String, Integer> SCOPES = new HashMap<String, Integer>();

    /**
     * Initialize the scope names map and the encode variable
     */
    static {
        SCOPES.put("page", PageContext.PAGE_SCOPE);
        SCOPES.put("request", PageContext.REQUEST_SCOPE);
        SCOPES.put("session", PageContext.SESSION_SCOPE);
        SCOPES.put("application", PageContext.APPLICATION_SCOPE);
    }
    
    /**
     * Converts the scope name into its corresponding PageContext constant value.
     * @param scopeName Can be "page", "request", "session", or "application" in any
     * case.
     * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
     * @throws JspException if the scopeName is not a valid name.
     */
    public int getScope(String scopeName) throws JspException {
        Integer scope = (Integer) SCOPES.get(scopeName.toLowerCase());

        if (scope == null) {
            throw new JspException("Scope '" + scopeName + "' not a valid option");
        }

        return scope;
    }
}
