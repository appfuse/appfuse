package org.appfuse.webapp.client.application.base.request;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.appfuse.webapp.client.application.base.security.AuthRequiredEvent;
import org.appfuse.webapp.client.application.base.security.RequestForbidenEvent;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

class CustomDefaultRequestTransport extends DefaultRequestTransport {

    private static final String SERVER_ERROR = "Server Error";
    private static final Logger wireLogger = Logger.getLogger("WireActivityLogger");

    private final EventBus eventBus;

    /**
     * @param eventBus
     */
    public CustomDefaultRequestTransport(EventBus eventBus) {
        super();
        this.eventBus = eventBus;
    }

    @Override
    protected RequestCallback createRequestCallback(final TransportReceiver receiver) {
        return new RequestCallback() {

            public void onError(Request request, Throwable exception) {
                wireLogger.log(Level.SEVERE, SERVER_ERROR, exception);
                receiver.onTransportFailure(new ServerFailure(exception.getMessage(), getExceptionName(exception), null, true));
            }

            public void onResponseReceived(Request request, Response response) {
                wireLogger.finest("Response received");
                if (Response.SC_UNAUTHORIZED == response.getStatusCode()) {
                    eventBus.fireEvent(new AuthRequiredEvent());
                } else if (Response.SC_FORBIDDEN == response.getStatusCode()) {
                    eventBus.fireEvent(new RequestForbidenEvent());
                } else if (Response.SC_OK == response.getStatusCode()) {
                    String text = response.getText();
                    receiver.onTransportSuccess(text);
                } else {
                    String message = response.getStatusCode() + " " + response.getText();
                    wireLogger.severe(SERVER_ERROR + " " + message);
                    receiver.onTransportFailure(new ServerFailure(message, null, null, true));
                }
            }
        };
    }

    private String getExceptionName(Throwable exception) {
        Class exceptionType = getMostSpecificCause(exception).getClass();
        String simpleName = exceptionType.getName();
        // simpleName = simpleName.substring(simpleName.lastIndexOf('.'));
        // simpleName = simpleName.replace("Exception", "");
        return simpleName;
    }

    private Throwable getMostSpecificCause(Throwable exception) {
        if (exception.getCause() != null) {
            return getMostSpecificCause(exception);
        } else {
            return exception;
        }
    }

}
