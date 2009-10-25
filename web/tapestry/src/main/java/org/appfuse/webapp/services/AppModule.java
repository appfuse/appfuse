package org.appfuse.webapp.services;

import java.io.IOException;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.RequestExceptionHandler;
import org.apache.tapestry5.services.ResponseRenderer;
import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.services.impl.ServiceFacadeImpl;
import org.appfuse.webapp.services.impl.ValidationDelegate;
import org.slf4j.Logger;
import org.springframework.mail.SimpleMailMessage;


/**
 * Application global configurations
 *
 * @author Serge Eby
 * @version $Id: AppModule.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class AppModule {
    public static void bind(ServiceBinder binder) {
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC
        // services.
        // Use service builder methods (example below) when the
        // implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        configuration.add(SymbolConstants.SUPPORTED_LOCALES,
                "de,en,es,fr,it,ko,nl,no,pt_BR,pt,tr,zh_CN,zh_TW,en_US");

        configuration.add(SymbolConstants.APPLICATION_CATALOG,
                "context:WEB-INF/classes/ApplicationResources.properties");

        // The factory default is true but during the early stages of an
        // application
        // overriding to false is a good idea. In addition, this is often
        // overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

    }

///**
// * Sample timing filter
// */
//	public RequestFilter buildTimingFilter(final Logger log) {
//		return new RequestFilter() {
//			public boolean service(Request request, Response response,
//					RequestHandler handler) throws IOException {
//				long startTime = System.currentTimeMillis();
//
//				try {
//					// The responsibility of a filter is to invoke the
//					// corresponding method
//					// in the handler. When you chain multiple filters together,
//					// each filter
//					// received a handler that is a bridge to the next filter.
//
//					return handler.service(request, response);
//				} finally {
//					long elapsed = System.currentTimeMillis() - startTime;
//
//					log.info(String.format("Request time: %d ms", elapsed));
//				}
//			}
//		};
//	}

//	/**
//	 * This is a contribution to the RequestHandler service configuration. This
//	 * is how we extend Tapestry using the timing filter. A common use for this
//	 * kind of filter is transaction management or security.
//	 */
//	public void contributeRequestHandler(
//			OrderedConfiguration<RequestFilter> configuration,
//			@InjectService("TimingFilter") RequestFilter filter 
//			) {
//		// Each contribution to an ordered configuration has a name, When
//		// necessary, you may
//		// set constraints to precisely control the invocation order of the
//		// contributed filter
//		// within the pipeline.
//		configuration.add("Timing", filter);
//	}


    /**
     * Facade for Spring services
     */
    public static ServiceFacade buildServiceFacade(final Logger logger,
                                                   final Context context,
                                                   @Inject MailEngine mailEngine,
                                                   @Inject UserManager userManager,
                                                   @Inject RoleManager roleManager,
                                                   @Inject SimpleMailMessage mailMessage,
                                                   final ThreadLocale threadLocale) {
        return new ServiceFacadeImpl(logger, context, mailEngine,
                userManager, roleManager, mailMessage, threadLocale);
    }

    public void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
                                         final Environment environment,
                                         @Path("context:images/iconWarning.gif")
                                         final Asset fieldErrorIcon
    ) {
        MarkupRendererFilter filter = new MarkupRendererFilter() {
            public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer) {
                environment.push(ValidationDecorator.class, new ValidationDelegate(environment, fieldErrorIcon, writer));
                renderer.renderMarkup(writer);
                environment.pop(ValidationDecorator.class);
            }
        };
        configuration.add("ValidationDelegate", filter, "after:DefaultValidationDecorator");

    }

    /**
     * Decorate Error page
     *
     * @param logger
     * @param renderer
     * @param componentSource
     * @param productionMode
     * @param service
     * @return
     */
    public RequestExceptionHandler decorateRequestExceptionHandler(
            final Logger logger,
            final ResponseRenderer renderer,
            final ComponentSource componentSource,
            @Symbol(SymbolConstants.PRODUCTION_MODE)
            boolean productionMode,
            Object service) {
        if (!productionMode) {
            return null;
        }

        return new RequestExceptionHandler() {
            public void handleRequestException(Throwable exception)
                    throws IOException {
                logger.error("Unexpected runtime exception: " + exception.getMessage(), exception);
                ExceptionReporter error = (ExceptionReporter) componentSource.getPage("Error");
                error.reportException(exception);
                renderer.renderPageMarkupResponse("Error");
            }
        };
    }


}
