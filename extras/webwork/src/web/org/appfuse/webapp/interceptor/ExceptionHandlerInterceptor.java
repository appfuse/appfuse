package org.appfuse.webapp.interceptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;

/**
 * Interceptor to handle Exceptions thrown by Actions. 
 * 
 * <p>
 * <a href="ExceptionHandlerInterceptor.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class ExceptionHandlerInterceptor implements Interceptor {
    public static final String exceptionAttribute = "exception";
    protected final Log logger = LogFactory.getLog(getClass());
    private Map exceptionMappings;

    /**
     * Set the mappings between exception class names and result names.
     * @param mappings fully qualified exception class names as keys,
     * and result names as values
     */
    public void setExceptionMappings(Properties mappings)
    throws ClassNotFoundException {
        this.exceptionMappings = new HashMap();

        for (Iterator it = mappings.keySet().iterator(); it.hasNext();) {
            String exceptionClassName = (String) it.next();
            String viewName = mappings.getProperty(exceptionClassName);
            Class exceptionClass =
                Class.forName(exceptionClassName, true,
                              Thread.currentThread().getContextClassLoader());
            this.exceptionMappings.put(exceptionClass, viewName);
        }
    }

    /**
     * Invoke action and if an exception occurs, route it to the mapped result.
     */
    public String intercept(ActionInvocation invocation)
    throws Exception {
        String result = null;

        try {
            result = invocation.invoke();
        } catch (Exception ex) {
            ex.printStackTrace();

            // check for specific mappings
            if (this.exceptionMappings != null) {
                for (Iterator it = this.exceptionMappings.keySet().iterator(); it.hasNext();) {
                    Class exceptionClass = (Class) it.next();

                    if (exceptionClass.isInstance(ex)) {
                        result = (String) this.exceptionMappings.get(exceptionClass);
                        ServletActionContext.getRequest().setAttribute(exceptionAttribute, ex);                        
                    }
                }
            }
        }

        return result;
    }

    public void destroy() {
    }

    public void init() {
    }
}
