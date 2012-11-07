package org.appfuse.webapp.interceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.springframework.security.access.AccessDeniedException;

/**
 * Correctly report spring-security's AccessDeniedException thrown from within Struts actions as 403 error.
 * These exceptions can be fired in a call to the service layer, for instance.
 * 
 * @author jgarcia
 */
public class AccessDeniedInterceptor implements Interceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            return invocation.invoke();
        } catch (AccessDeniedException e) {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }
    }

    /**
     * This method currently does nothing.
     */
    @Override
    public void destroy() {
    }

    /**
     * This method currently does nothing.
     */
    @Override
    public void init() {
    }
}
