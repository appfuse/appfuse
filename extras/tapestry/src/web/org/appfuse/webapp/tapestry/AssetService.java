package org.appfuse.webapp.tapestry;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.appfuse.webapp.action.BaseEngine;

public class AssetService extends org.apache.tapestry.asset.AssetService
{
    /**
     * @see org.apache.tapestry.engine.IEngineService#getLink(org.apache.tapestry.IRequestCycle, org.apache.tapestry.IComponent, java.lang.Object[])
     */
    public ILink getLink(IRequestCycle requestCycle, IComponent component, Object[] parameters)
    {
        if (Tapestry.size(parameters) != 1)
        {
            throw new ApplicationRuntimeException(Tapestry.format("service-single-parameter", Tapestry.ASSET_SERVICE));
        }
        
        String suffix = requestCycle.getEngine().getSpecification().getProperty(BaseEngine.SERVICE_URL_SUFFIX);
        
        return new Link(requestCycle, Tapestry.ASSET_SERVICE + suffix, null, parameters);
    }
}
