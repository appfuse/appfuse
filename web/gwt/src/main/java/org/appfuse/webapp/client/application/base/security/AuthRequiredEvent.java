/**
 * 
 */
package org.appfuse.webapp.client.application.base.security;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @author ivangsa
 *
 */
public class AuthRequiredEvent extends GwtEvent<AuthRequiredEvent.Handler> {

    private static final Type<Handler> TYPE = new Type<Handler>();

    /**
     * Implemented by handlers of this type of event.
     */
    public interface Handler extends EventHandler {

        /**
         * Called when a {@link AuthRequiredEvent} is fired.
         *
         * @param authRequiredEvent
         *            a {@link AuthRequiredEvent} instance
         */
        void onAuthRequiredEvent(AuthRequiredEvent authRequiredEvent);
    }

    @Override
    public GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Register a {@link AuthRequiredEvent.Handler} on an {@link EventBus}.
     *
     * @param eventBus
     *            the {@link EventBus}
     * @param handler
     *            a {@link AuthRequiredEvent.Handler}
     * @return a {@link HandlerRegistration} instance
     */
    public static HandlerRegistration register(EventBus eventBus, AuthRequiredEvent.Handler handler) {
        return eventBus.addHandler(TYPE, handler);
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onAuthRequiredEvent(this);
    }
}
