package org.appfuse.webapp.tapestry;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;

public abstract class AbstractService extends org.apache.tapestry.engine.AbstractService
{
    protected String[] getServiceContext(RequestContext requestContext)
    {
        return requestContext.getParameters(Tapestry.SERVICE_QUERY_PARAMETER_NAME);
    }
}