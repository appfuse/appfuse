package org.appfuse.webapp.tapestry;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.appfuse.webapp.action.BaseEngine;

public class DirectService extends org.apache.tapestry.engine.DirectService
{
    private static final String STATEFUL_ON = "1";
    private static final String STATEFUL_OFF = "0";
    
    /**
     * @see org.apache.tapestry.engine.IEngineService#getLink(org.apache.tapestry.IRequestCycle, org.apache.tapestry.IComponent, java.lang.Object[])
     */
    public ILink getLink(IRequestCycle requestCycle, IComponent component, Object[] parameters)
    {
        IPage renderPage = requestCycle.getPage();
        IPage componentPage = component.getPage();
        
        boolean complex = renderPage != componentPage;
        
        String stateful = requestCycle.getEngine().isStateful() ? STATEFUL_ON : STATEFUL_OFF;

        String[] serviceContext = new String[complex ? 4 : 3];
        
        int i = 0;

        serviceContext[i++] = Tapestry.DIRECT_SERVICE;
        serviceContext[i++] = stateful;
        
        if (complex)
        {
            serviceContext[i++] = componentPage.getPageName();
        }
        
        serviceContext[i++] = component.getIdPath();
        
        String suffix = requestCycle.getEngine().getSpecification().getProperty(BaseEngine.PAGE_URL_SUFFIX);
        
        return new Link(requestCycle, renderPage.getPageName() + suffix, serviceContext, parameters);
    }
    
    /**
     * @see org.apache.tapestry.engine.AbstractService#getServiceContext(org.apache.tapestry.request.RequestContext)
     */
    protected String[] getServiceContext(RequestContext requestContext)
    {
        IApplicationSpecification specification = requestContext.getServlet().getApplicationSpecification();
        String urlSuffix = specification.getProperty(BaseEngine.PAGE_URL_SUFFIX);
        String[] serviceContext = requestContext.getParameters(Tapestry.SERVICE_QUERY_PARAMETER_NAME);
        
        String servletPath = requestContext.getRequest().getServletPath();
        
        // Remove service name from service context
        serviceContext[0] = serviceContext[1];
        // Insert page name as second service context element
        serviceContext[1] = servletPath.substring(1, servletPath.indexOf(urlSuffix));
        
        return serviceContext;
    }
}