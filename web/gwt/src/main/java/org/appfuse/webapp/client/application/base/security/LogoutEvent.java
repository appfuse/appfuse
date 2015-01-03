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
public class LogoutEvent extends GwtEvent<LogoutEvent.Handler> {

    private static final Type<Handler> TYPE = new Type<Handler>();

    /**
     * Implemented by handlers of this type of event.
     */
    public interface Handler extends EventHandler {

        /**
         * Called when a {@link LogoutEvent} is fired.
         *
         * @param authRequiredEvent
         *            a {@link LogoutEvent} instance
         */
        void onLogoutEvent(LogoutEvent logoutEvent);
    }

    @Override
    public GwtEvent.Type<Handler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Register a {@link LogoutEvent.Handler} on an {@link EventBus}.
     *
     * @param eventBus
     *            the {@link EventBus}
     * @param handler
     *            a {@link LogoutEvent.Handler}
     * @return a {@link HandlerRegistration} instance
     */
    public static HandlerRegistration register(EventBus eventBus, LogoutEvent.Handler handler) {
        return eventBus.addHandler(TYPE, handler);
    }

    @Override
    protected void dispatch(Handler handler) {
        handler.onLogoutEvent(this);
    }
}
