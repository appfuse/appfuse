package org.appfuse.webapp.services;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.*;
import org.appfuse.Constants;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;


/**
 * AppFuse Test module.
 *
 * @author Serge Eby
 */
@SubModule(AppModule.class)
public class AppTestModule {

    /**
     * Use to allow PageTester to run with spring and spring-security.
     *
     * @param config
     * @param requestGlobals
     */
    public static void contributeRequestHandler(OrderedConfiguration<RequestFilter> config,
                                                final RequestGlobals requestGlobals) {
        RequestFilter filter = new RequestFilter() {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException {
                requestGlobals.storeServletRequestResponse(
                        new MockHttpServletRequest(),
                        new MockHttpServletResponse());

                return handler.service(request, response);
            }
        };
        config.add("SpringMockHttpRequestAndResponse", filter, "after:CheckForUpdates","before:URLRewriter");
    }

}
