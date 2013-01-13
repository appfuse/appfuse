package org.appfuse.webapp.server;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayerDecorator;
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
				ServerFailure customServerFailure = createCustomServerFailure(throwable);
				if(customServerFailure != null) {
					return customServerFailure;
				}
			} 
			catch (Exception e) {
				return createServerFailure(throwable);
			}
			return super.createServerFailure(throwable);
		}
		
		/**
		 * 
		 * @param throwable
		 * @return
		 * @throws Exception
		 */
		private ServerFailure createCustomServerFailure(Throwable throwable) throws Exception{
			if(throwable instanceof AuthenticationException) {
				getServletResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return null; 
			}
			if(throwable instanceof AccessDeniedException) {
				getServletResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
				return null; 
			}
			return null;
		}
		
		private HttpServletResponse getServletResponse() {
			return RequestFactoryServlet.getThreadLocalResponse();			
		}
	}
}
