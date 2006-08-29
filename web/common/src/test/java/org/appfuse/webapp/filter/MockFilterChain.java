package org.appfuse.webapp.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Borrowed from the Display Tag project:
 * http://displaytag.sourceforge.net/xref-test/org/displaytag/filter/MockFilterSupport.html
 */
public class MockFilterChain implements FilterChain {
    private final Log log = LogFactory.getLog(MockFilterChain.class);
    
    public void doFilter(ServletRequest request, ServletResponse response)
    throws IOException, ServletException {
        String uri = ((HttpServletRequest) request).getRequestURI();
        String requestContext = ((HttpServletRequest) request).getContextPath();

        if (StringUtils.isNotEmpty(requestContext) &&
                uri.startsWith(requestContext)) {
            uri = uri.substring(requestContext.length());
        }

        if (log.isDebugEnabled()) {
            log.debug("Redirecting to [" + uri + "]");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(uri);
        dispatcher.forward(request, response);
    }
}
