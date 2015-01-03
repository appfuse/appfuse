package org.appfuse.webapp.server;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
import com.google.web.bindery.requestfactory.server.impl.FindService;
import com.google.web.bindery.requestfactory.shared.Locator;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class CustomRequestFactoryServlet extends RequestFactoryServlet {

    private static final long serialVersionUID = -4245826401547466758L;

    public CustomRequestFactoryServlet() {
        super(new CustomExceptionHandler(), new ServiceLayerDecorator() {

            @Override
            public <T extends Locator<?, ?>> T createLocator(Class<T> clazz) {
                ApplicationContext context = WebApplicationContextUtils
                        .getWebApplicationContext(CustomRequestFactoryServlet.getThreadLocalServletContext());
                return context.getBean(clazz);
            }

            @Override
            public Object invoke(Method domainMethod, Object... args) {
                if (FindService.class.equals(domainMethod.getDeclaringClass())) {
                    // Entities should only be accessed through secured
                    // RequestService methods (do not use find)
                    throw new AccessDeniedException("Access is disabled through FindService.find() method");
                    // FIXME this exception is not gracefully handled by
                    // CustomExceptionHandler, but at least we are safer
                }
                return super.invoke(domainMethod, args);
            }
        });
    }

    /**
	 * 
	 *
	 */
    private static class CustomExceptionHandler extends DefaultExceptionHandler {
        /**
         * 
         * @see com.google.web.bindery.requestfactory.server.DefaultExceptionHandler#createServerFailure(java.lang.Throwable)
         */
        @Override
        public ServerFailure createServerFailure(Throwable throwable) {
            try {
                return createCustomServerFailure(throwable);
            } catch (Exception e) {
                return super.createServerFailure(throwable);
            }
        }

        /**
         * 
         * @param throwable
         * @return
         * @throws Exception
         */
        private ServerFailure createCustomServerFailure(Throwable throwable) throws Exception {
            if (throwable instanceof AuthenticationException) {
                getServletResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return null;
            }
            if (throwable instanceof AccessDeniedException) {
                if (!RequestFactoryServlet.getThreadLocalRequest().isRequestedSessionIdValid()) {
                    // if session has expired send a 401 error code instead of
                    // 403
                    getServletResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return null;
                }
                getServletResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
                return null;
            }
            return super.createServerFailure(throwable);
        }

        private HttpServletResponse getServletResponse() {
            return RequestFactoryServlet.getThreadLocalResponse();
        }
    }
}
