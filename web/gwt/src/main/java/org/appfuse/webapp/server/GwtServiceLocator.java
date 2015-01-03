package org.appfuse.webapp.server;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServiceLocator;

public class GwtServiceLocator implements ServiceLocator {

    HttpServletRequest request = RequestFactoryServlet.getThreadLocalRequest();
    ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());

    @Override
    public Object getInstance(Class<?> clazz) {
        return context.getBean(clazz);
    }
}
