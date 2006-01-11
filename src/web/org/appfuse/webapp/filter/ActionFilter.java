package org.appfuse.webapp.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter sets the locale from the request to Spring's LocaleContextHolder
 * so service and data layer's can retrieve it.
 *
 * <p><a href="ActionFilter.java.html"><i>View Source</i></a>
 *
 * @author  Matt Raible
 *
 * @web.filter name="actionFilter"
 */
public class ActionFilter extends OncePerRequestFilter {

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                 FilterChain chain)
    throws IOException, ServletException {

        // notify the LocaleContextHolder what locale is being used so
        // service and data layer classes can get the locale
        LocaleContextHolder.setLocale(request.getLocale());

        chain.doFilter(request, response);

        // Reset thread-bound LocaleContext.
        LocaleContextHolder.setLocaleContext(null);
    }
}
