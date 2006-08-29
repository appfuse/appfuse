package org.appfuse.webapp.taglib;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.Constants;


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
 *
 * @jsp.tag name="constants" bodycontent="empty"
 *  tei-class="org.appfuse.webapp.taglib.ConstantsTei"
 */
public class ConstantsTag extends TagSupport {
    private static final long serialVersionUID = 3258417209566116146L;
    private final Log log = LogFactory.getLog(ConstantsTag.class);
    
    /**
     * The class to expose the variables from.
     */
    public String clazz = Constants.class.getName();

    /**
     * The scope to be put the variable in.
     */
    protected String scope = null;

    /**
     * The single variable to expose.
     */
    protected String var = null;

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

                for (int i = 0; i < fields.length; i++) {
                    /*if (log.isDebugEnabled()) {
                       log.debug("putting '" + fields[i].getName() + "=" +
                                 fields[i].get(this) + "' into " + scope +
                                 " scope");
                       }*/
                    pageContext.setAttribute(fields[i].getName(),
                                             fields[i].get(this), toScope);
                }
            } else {
                try {
                    Object value = c.getField(var).get(this);
                    pageContext.setAttribute(c.getField(var).getName(), value,
                                             toScope);
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

    /**
     * @jsp.attribute
     */
    public void setClassName(String clazz) {
        this.clazz = clazz;
    }

    public String getClassName() {
        return this.clazz;
    }

    /**
     * @jsp.attribute
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return (this.scope);
    }

    /**
     * @jsp.attribute
     */
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
    
    // ~========== From Struts' TagUtils class =====================

    /**
     * Maps lowercase JSP scope names to their PageContext integer constant
     * values.
     */
    private static final Map scopes = new HashMap();

    /**
     * Initialize the scope names map and the encode variable with the 
     * Java 1.4 method if available.
     */
    static {
        scopes.put("page", new Integer(PageContext.PAGE_SCOPE));
        scopes.put("request", new Integer(PageContext.REQUEST_SCOPE));
        scopes.put("session", new Integer(PageContext.SESSION_SCOPE));
        scopes.put("application", new Integer(PageContext.APPLICATION_SCOPE));
    }
    
    /**
     * Converts the scope name into its corresponding PageContext constant value.
     * @param scopeName Can be "page", "request", "session", or "application" in any
     * case.
     * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
     * @throws JspException if the scopeName is not a valid name.
     */
    public int getScope(String scopeName) throws JspException {
        Integer scope = (Integer) scopes.get(scopeName.toLowerCase());

        if (scope == null) {
            throw new JspException("Scope '" + scopeName + "' not a valid option");
        }

        return scope.intValue();
    }
}
