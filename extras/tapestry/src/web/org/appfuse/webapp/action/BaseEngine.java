package org.appfuse.webapp.action;

import java.util.Map;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BaseEngine extends org.apache.tapestry.engine.BaseEngine {
    public static final String APPLICATION_CONTEXT_KEY = "appContext";
    public static final String SERVICE_URL_SUFFIX = "org.apache.tapestry.service-url-suffix";
    public static final String PAGE_URL_SUFFIX = "org.apache.tapestry.page-url-suffix";
    
    /**
     * @see org.apache.tapestry.engine.AbstractEngine#setupForRequest(org.apache.tapestry.request.RequestContext)
     */
    protected void setupForRequest(RequestContext context) {
        super.setupForRequest(context);

        Map global = (Map) getGlobal();
        ApplicationContext ac = (ApplicationContext) global
                .get(APPLICATION_CONTEXT_KEY);

        ac = WebApplicationContextUtils.getWebApplicationContext(context
                .getServlet().getServletContext());
        global.put(APPLICATION_CONTEXT_KEY, ac);
        
        // turn off caching of the locale and get it from the request each time
        setLocale(context.getRequest().getLocale());
    }
    
    /**
     * @see org.apache.tapestry.engine.AbstractEngine#extractServiceName(org.apache.tapestry.request.RequestContext)
     */
    protected String extractServiceName(RequestContext requestContext) {
        String[] serviceContext = requestContext.getParameters(Tapestry.SERVICE_QUERY_PARAMETER_NAME);
        
        if (requestContext.getRequest().getParameter(Tapestry.TAG_SUPPORT_SERVICE_ATTRIBUTE) != null) {
            return Tapestry.TAGSUPPORT_SERVICE;
        }

        String servletPath = requestContext.getRequest().getServletPath();
        IApplicationSpecification specification = this.getSpecification();
        String pageUrlSuffix = specification.getProperty(PAGE_URL_SUFFIX);
        
        if (servletPath.endsWith(pageUrlSuffix)) {
            if ((serviceContext == null) || (serviceContext.length == 0)) {
                if (requestContext.getRequest().getParameter(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME) == null) {
                    return Tapestry.PAGE_SERVICE;
                }
                
                return Tapestry.EXTERNAL_SERVICE;
            }
            
            return serviceContext[0];
        }
        
        String serviceUrlSuffix = specification.getProperty(SERVICE_URL_SUFFIX);
        
        if (servletPath.endsWith(serviceUrlSuffix)) {
            return servletPath.substring(1, servletPath.indexOf(serviceUrlSuffix));
        }
        
        throw new ApplicationRuntimeException("Invalid request: " + requestContext.getRequest().getRequestURL());
    }
}
