package org.appfuse.webapp.interceptor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * Security interceptor checks to see if users are in the specified roles
 * before proceeding.  Similar to Spring's UserRoleAuthorizationInterceptor.
 *
 * <p>
 * <a href="UserRoleAuthorizationInterceptor.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @see org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor
 */
public class UserRoleAuthorizationInterceptor implements Interceptor {
    private static final long serialVersionUID = 5067790608840427509L;
    private String[] authorizedRoles;

    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();

        if (this.authorizedRoles != null) {
            for (int i = 0; i < this.authorizedRoles.length; i++) {
                if (request.isUserInRole(this.authorizedRoles[i])) {
                    return invocation.invoke();
                }
            }
        }

        HttpServletResponse response = ServletActionContext.getResponse();
        handleNotAuthorized(request, response);
        return null;
    }

    /**
     * Set the roles that this interceptor should treat as authorized.
     * @param authorizedRoles array of role names
     */
    public final void setAuthorizedRoles(String[] authorizedRoles) {
        this.authorizedRoles = authorizedRoles;
    }

    /**
     * Handle a request that is not authorized according to this interceptor.
     * Default implementation sends HTTP status code 403 ("forbidden").
     * <p>This method can be overridden to write a custom message, forward or
     * redirect to some error page or login page, or throw a ServletException.
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @throws javax.servlet.ServletException if there is an internal error
     * @throws java.io.IOException in case of an I/O error when writing the response
     */
    protected void handleNotAuthorized(HttpServletRequest request,
                                       HttpServletResponse response)
    throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
    
    public void destroy() {
    }

    public void init() {
    }
}
