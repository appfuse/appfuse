package org.appfuse.webapp.client.application;

import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.application.base.security.LogoutEvent;
import org.appfuse.webapp.client.requests.ApplicationRequestFactory;
import org.appfuse.webapp.client.ui.Shell;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Application for browsing entities.
 */
public class MobileApplication extends DesktopApplication implements LoginEvent.Handler, LogoutEvent.Handler {

    @Inject
    public MobileApplication(
            final Shell shell,
            final ApplicationMenu menu,
            final ApplicationRequestFactory requestFactory,
            final EventBus eventBus,
            final PlaceController placeController,
            final PlaceHistoryMapper placeHistoryMapper,
            final PlaceHistoryHandler placeHistoryHandler,
            final ActivityManager activityManager,
            final ApplicationProxyFactory proxyFactory,
            final ApplicationValidatorFactory validatorFactory) {
        super(shell, menu, requestFactory, eventBus, placeController, placeHistoryMapper, placeHistoryHandler, activityManager, proxyFactory,
                validatorFactory);
    }
}
