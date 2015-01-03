package org.appfuse.webapp.client.requests;

import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

public interface ApplicationRequestFactory extends RequestFactory {

    /**
     * Return a GWT logging request.
     */
    LoggingRequest loggingRequest();

    LookupRequest lookupRequest();

    UserRequest userRequest();

}
