package org.appfuse.webapp.taglib;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.TagUtils;
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
 * @version $Revision: 1.4 $ $Date: 2004/08/19 00:13:57 $
 *
 * @jsp.tag name="constants" bodycontent="empty"
 *  tei-class="org.appfuse.webapp.taglib.ConstantsTei"
 */
public class ConstantsTag extends TagSupport {
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
            // default to pageScope
            toScope = TagUtils.getInstance().getScope(scope);
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
                    String value = (String) c.getField(var).get(this);
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
}
