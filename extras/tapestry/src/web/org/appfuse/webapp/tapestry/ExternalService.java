package org.appfuse.webapp.tapestry;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.appfuse.webapp.action.BaseEngine;

public class ExternalService extends org.apache.tapestry.engine.ExternalService
{
    /**
     * @see org.apache.tapestry.engine.IEngineService#getLink(org.apache.tapestry.IRequestCycle, org.apache.tapestry.IComponent, java.lang.Object[])
     */
    public ILink getLink(IRequestCycle requestCycle, IComponent component, Object[] parameters)
    {
        if (parameters == null || parameters.length == 0)
        {
            throw new ApplicationRuntimeException(Tapestry.format("service-requires-parameters", Tapestry.EXTERNAL_SERVICE));
        }

        String page = (String) parameters[0];

        Object[] pageParameters = new Object[parameters.length - 1];
        System.arraycopy(parameters, 1, pageParameters, 0, parameters.length - 1);

        String suffix = requestCycle.getEngine().getSpecification().getProperty(BaseEngine.PAGE_URL_SUFFIX);
        
        return new Link(requestCycle, page + suffix, null, pageParameters);
    }
    
    /**
     * @see org.apache.tapestry.engine.AbstractService#getServiceContext(org.apache.tapestry.request.RequestContext)
     */
    protected String[] getServiceContext(RequestContext requestContext)
    {
        IApplicationSpecification specification = requestContext.getServlet().getApplicationSpecification();
        String urlSuffix = specification.getProperty(BaseEngine.PAGE_URL_SUFFIX);
        String servletPath = requestContext.getRequest().getServletPath();

        return new String[] { servletPath.substring(1, servletPath.indexOf(urlSuffix)) };
    }
}
