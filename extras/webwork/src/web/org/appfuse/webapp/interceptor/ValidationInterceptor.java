package org.appfuse.webapp.interceptor;

import java.util.Map;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.AroundInterceptor;
import com.opensymphony.xwork.validator.ActionValidatorManager;


/**
 * Custom ValidationInterceptor to cancel validation when cancel 
 * or delete is in request.
 */
public class ValidationInterceptor extends AroundInterceptor {

    protected void after(ActionInvocation dispatcher, String result) throws Exception {
    }

    protected void before(ActionInvocation invocation) throws Exception {
        Action action = invocation.getAction();
        String context = invocation.getProxy().getActionName();
        
        final Map parameters = ActionContext.getContext().getParameters();
        // don't validate on cancel, delete or GET
        if (ServletActionContext.getRequest().getMethod().equals("GET")) {
            if (log.isDebugEnabled()) {
                log.debug("Cancelling validation, detected GET request");
            }
        } else if (parameters.containsKey("cancel")  || parameters.containsKey("delete")) {
            if (log.isDebugEnabled()) {
                log.debug("Cancelling validation, detected clicking cancel or delete");
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Validating " + invocation.getProxy().getNamespace() + invocation.getProxy().getActionName() + ".");
            }
    
            ActionValidatorManager.validate(action, context);
        }
    }
}
