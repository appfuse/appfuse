package org.appfuse.webapp.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import org.appfuse.Constants;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to wrap request with a request including user preferred locale.
 */
public class LocaleFilter extends OncePerRequestFilter {

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                 FilterChain chain)
            throws IOException, ServletException {

        String locale = request.getParameter("locale");
        Locale preferredLocale = request.getLocale();

        if (locale != null) {
            preferredLocale = new Locale(locale);
        }
        
        LocaleContextHolder.setLocale(preferredLocale);
        
        HttpSession session = request.getSession(false);

        if (session != null) {
            if (preferredLocale == null) {
                preferredLocale = (Locale) session.getAttribute(Constants.PREFERRED_LOCALE_KEY);
            } else {
                session.setAttribute(Constants.PREFERRED_LOCALE_KEY, preferredLocale);
                Config.set(session, Config.FMT_LOCALE, locale);
            }

            if (preferredLocale != null && !(request instanceof LocaleRequestWrapper)) {
                request = new LocaleRequestWrapper(request, preferredLocale);
            }
        }

        chain.doFilter(request, response);
        
        // Reset thread-bound LocaleContext.
        LocaleContextHolder.setLocaleContext(null);
    }
}
