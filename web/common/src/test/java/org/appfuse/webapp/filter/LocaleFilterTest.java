package org.appfuse.webapp.filter;

import org.appfuse.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleFilterTest {
    private LocaleFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new LocaleFilter();
        filter.init(new MockFilterConfig());
    }

    @Test
    public void testSetLocaleInSessionWhenSessionIsNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("locale", "es");

        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());

        // no session, should result in null
        assertNull(request.getSession().getAttribute(Constants.PREFERRED_LOCALE_KEY));
        // thread locale should always have it, regardless of session
        assertNotNull(LocaleContextHolder.getLocale());
    }

    @Test
    public void testSetLocaleInSessionWhenSessionNotNull() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("locale", "es");

        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setSession(new MockHttpSession(null));

        filter.doFilter(request, response, new MockFilterChain());

        // session not null, should result in not null
        Locale locale = (Locale) request.getSession().getAttribute(Constants.PREFERRED_LOCALE_KEY);
        assertNotNull(locale);
        assertNotNull(LocaleContextHolder.getLocale());
        assertEquals(new Locale("es"), locale);
    }

    @Test
    public void testSetInvalidLocale() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("locale", "foo");

        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setSession(new MockHttpSession(null));

        filter.doFilter(request, response, new MockFilterChain());

        // a locale will get set regardless - there's no such thing as an invalid one
        assertNotNull(request.getSession().getAttribute(Constants.PREFERRED_LOCALE_KEY));
    }

    @Test
    public void testJstlLocaleIsSet() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("locale", "es");

        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setSession(new MockHttpSession(null));

        filter.doFilter(request, response, new MockFilterChain());

        assertNotNull(Config.get(request.getSession(), Config.FMT_LOCALE));
    }

    @Test
    public void testLocaleAndCountry() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(new MockHttpSession());
        request.addParameter("locale", "zh_TW");

        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());

        // session not null, should result in not null
        Locale locale = (Locale) request.getSession().getAttribute(Constants.PREFERRED_LOCALE_KEY);
        assertNotNull(locale);
        assertEquals(new Locale("zh", "TW"), locale);
    }
}
