package org.appfuse.webapp.tapestry;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.util.io.DataSqueezer;

public class Link implements ILink
{
    protected IRequestCycle requestCycle;
    private String servletPath;
    private String[] serviceContext;
    private String[] parameters;

    public Link(IRequestCycle requestCycle, String servletPath, String[] serviceContext, Object[] parameters)
    {
        this.requestCycle = requestCycle;
        this.servletPath = servletPath;
        this.serviceContext = serviceContext;

        if (parameters != null)
        {
            DataSqueezer squeezer = requestCycle.getEngine().getDataSqueezer();

            try
            {
                this.parameters = squeezer.squeeze(parameters);
            }
            catch (IOException e)
            {
                throw new ApplicationRuntimeException(e);
            }
        }
    }
    
    /**
     * @see org.apache.tapestry.engine.ILink#getURL()
     */
    public String getURL()
    {
        return getURL(null, true);
    }

    /**
     * @see org.apache.tapestry.engine.ILink#getURL(java.lang.String, boolean)
     */
    public String getURL(String anchor, boolean includeParameters)
    {
        return constructURL(new StringBuffer(), anchor, includeParameters);
    }

    /**
     * @see org.apache.tapestry.engine.ILink#getAbsoluteURL()
     */
    public String getAbsoluteURL()
    {
        return getAbsoluteURL(null, null, 0, null, true);
    }

    /**
     * @see org.apache.tapestry.engine.ILink#getAbsoluteURL(java.lang.String, java.lang.String, int, java.lang.String, boolean)
     */
    public String getAbsoluteURL(String scheme, String server, int port, String anchor, boolean includeParameters)
    {
        RequestContext context = this.requestCycle.getRequestContext();
        StringBuffer buffer = new StringBuffer();
        
        buffer.append((scheme != null) ? scheme : context.getScheme());
        buffer.append("://");
        buffer.append((server != null) ? server : context.getServerName());
        buffer.append(':');
        buffer.append((port != 0) ? port : context.getServerPort());
        
        return constructURL(buffer, anchor, includeParameters);
    }
    
    private String constructURL(StringBuffer buffer, String anchor, boolean includeParameters)
    {
        RequestContext context = this.requestCycle.getRequestContext();
        
        buffer.append(context.getRequest().getContextPath()).append('/').append(this.servletPath);
        
        if (includeParameters)
        {
            String encoding = this.requestCycle.getEngine().getOutputEncoding();

            buffer.append('?');
            
            try
            {
                if (this.serviceContext != null)
                {
                    for (int i = 0; i < this.serviceContext.length; i++)
                    {
                        buffer.append(Tapestry.SERVICE_QUERY_PARAMETER_NAME);
                        buffer.append('=');
                        buffer.append(URLEncoder.encode(this.serviceContext[i], encoding));
                        buffer.append('&');
                    }
                }
                
                if (this.parameters != null)
                {
                    for (int i = 0; i < this.parameters.length; i++)
                    {
                        buffer.append(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);
                        buffer.append('=');
                        buffer.append(URLEncoder.encode(this.parameters[i], encoding));
                        buffer.append('&');
                    }
                }
                
                // Delete trailing & or ?
                buffer.deleteCharAt(buffer.length() - 1);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new ApplicationRuntimeException(e);
            }
        }
        
        if (anchor != null)
        {
            buffer.append('#').append(anchor);
        }
        
        return this.requestCycle.encodeURL(buffer.toString());
    }

    /**
     * @see org.apache.tapestry.engine.ILink#getParameterNames()
     */
    public String[] getParameterNames()
    {
        List parameterList = new ArrayList(2);

        if ((this.serviceContext != null) && (this.serviceContext.length > 0))
        {
            parameterList.add(Tapestry.SERVICE_QUERY_PARAMETER_NAME);
        }

        if ((this.parameters != null) && (this.parameters.length > 0))
        {
            parameterList.add(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);
        }
        
        return (String[]) parameterList.toArray(new String[parameterList.size()]);
    }

    /**
     * @see org.apache.tapestry.engine.ILink#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String name)
    {
        if (name.equals(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME))
        {
            return this.parameters;
        }
        
        if (name.equals(Tapestry.SERVICE_QUERY_PARAMETER_NAME))
        {
            return this.serviceContext;
        }
        
        throw new IllegalArgumentException(Tapestry.format("EngineServiceLink.unknown-parameter-name", name));
    }
}
