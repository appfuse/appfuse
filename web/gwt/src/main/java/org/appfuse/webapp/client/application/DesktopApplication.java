package org.appfuse.webapp.client.application;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.appfuse.webapp.client.application.base.request.RequestEvent;
import org.appfuse.webapp.client.application.base.security.AuthRequiredEvent;
import org.appfuse.webapp.client.application.base.security.LoginEvent;
import org.appfuse.webapp.client.application.base.security.LogoutEvent;
import org.appfuse.webapp.client.application.base.security.RequestForbidenEvent;
import org.appfuse.webapp.client.proxies.LookupConstantsProxy;
import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.requests.ApplicationRequestFactory;
import org.appfuse.webapp.client.ui.Shell;
import org.appfuse.webapp.client.ui.home.HomePlace;
import org.appfuse.webapp.client.ui.login.LoginPlace;

import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.Receiver;

/**
 * Application for browsing entities.
 */
public class DesktopApplication extends Application implements LoginEvent.Handler, LogoutEvent.Handler {
    private static final Logger LOGGER = Logger.getLogger(DesktopApplication.class.getName());

    @Inject
    public DesktopApplication(
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

    @Override
    public void run() {
        setProgress(50);

        /* Add handlers */
        initHandlers();

        setProgress(60);
        /* load application constants */
        requestFactory.lookupRequest().getApplicationConstants().fire(new Receiver<LookupConstantsProxy>() {
            @Override
            public void onSuccess(final LookupConstantsProxy lookupConstants) {
                setLookupConstants(lookupConstants);
                setProgress(70);

                /* Authentication */
                requestFactory.userRequest().getCurrentUser().with("roles").fire(new Receiver<UserProxy>() {

                    @Override
                    public void onSuccess(final UserProxy currentUser) {
                        setProgress(85);

                        if (currentUser != null) {
                            setCurrentUser(currentUser);

                            setProgress(100);
                            showShell();

                            /*
                             * Register home place and parse url for current
                             * place token
                             */
                            final Place defaultPlace = new HomePlace();
                            placeHistoryHandler.register(placeController, eventBus, defaultPlace);
                            placeHistoryHandler.handleCurrentHistory();
                            shell.onLoginEvent(new LoginEvent());
                        } else {
                            showShell();

                            /*
                             * Register home place and parse url for current
                             * place token
                             */
                            final Place defaultPlace = new LoginPlace(History.getToken());
                            placeHistoryHandler.register(placeController, eventBus, defaultPlace);
                            placeController.goTo(defaultPlace);
                        }
                    }
                });

            }
        });

    }

    protected void showShell() {
        final Element loading = Document.get().getElementById("loading");
        loading.getParentElement().removeChild(loading);

        /* And show the user the shell */
        RootLayoutPanel.get().add(shell);
        RootLayoutPanel.get().getElement().setId("rootPanel");

        // remove gwt extra divs and place shell nodes directly into the
        // document body,
        // (and hope for the best about xbrowser compatibility..)
        shell.getElement().setId("shell");
        final Element shellElement = Document.get().getElementById("shell");
        shellElement.removeFromParent();
        Document.get().getElementById("rootPanel").removeFromParent();

        Element shellChildElement = shellElement.getFirstChildElement();
        while (shellChildElement != null) {
            shellChildElement.removeFromParent();
            Document.get().getBody().appendChild(shellChildElement);
            shellChildElement = shellElement.getFirstChildElement();
        }

    }

    protected void initHandlers() {

        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(final Throwable e) {
                Window.alert("Error: " + e.getMessage());
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        });

        if (LogConfiguration.loggingIsEnabled()) {
            // Add remote logging handler
            final RequestFactoryLogHandler.LoggingRequestProvider provider = new RequestFactoryLogHandler.LoggingRequestProvider() {
                @Override
                public LoggingRequest getLoggingRequest() {
                    return requestFactory.loggingRequest();
                }
            };
            Logger.getLogger("").addHandler(new RequestFactoryLogHandler(provider, Level.WARNING, new ArrayList<String>()));
        }

        RequestEvent.register(eventBus, new RequestEvent.Handler() {
            // Only show loading status if a request isn't serviced in 250ms.
            private static final int LOADING_TIMEOUT = 250;

            @Override
            public void onRequestEvent(final RequestEvent requestEvent) {
                if (requestEvent.getState() == RequestEvent.State.SENT) {
                    DOM.setStyleAttribute(shell.getElement(), "cursor", "wait");
                    shell.getMole().showDelayed(LOADING_TIMEOUT);
                } else {
                    DOM.setStyleAttribute(shell.getElement(), "cursor", "default");
                    shell.getMole().hide();
                }
            }
        });

        if (shell instanceof PlaceChangeEvent.Handler) {
            eventBus.addHandler(PlaceChangeEvent.TYPE, (PlaceChangeEvent.Handler) shell);
        }

        LoginEvent.register(eventBus, this);
        LogoutEvent.register(eventBus, this);

        AuthRequiredEvent.register(eventBus, new AuthRequiredEvent.Handler() {
            @Override
            public void onAuthRequiredEvent(final AuthRequiredEvent authRequiredEvent) {
                placeController.goTo(new LoginPlace(History.getToken()));
            }
        });

        RequestForbidenEvent.register(eventBus, new RequestForbidenEvent.Handler() {

            @Override
            public void onRequestForbidenEvent(final RequestForbidenEvent requestForbidenEvent) {
                shell.addMessage(i18n._03_title(), AlertType.ERROR);
            }
        });
    }

    @Override
    public void onLoginEvent(final LoginEvent loginEvent) {

        requestFactory.userRequest().getCurrentUser().with("roles").fire(new Receiver<UserProxy>() {
            @Override
            public void onSuccess(final UserProxy currentUser) {
                if (currentUser != null) {
                    setCurrentUser(currentUser);
                    /* re-load application constants */
                    requestFactory.lookupRequest().getApplicationConstants().fire(new Receiver<LookupConstantsProxy>() {
                        @Override
                        public void onSuccess(final LookupConstantsProxy lookupConstants) {
                            setLookupConstants(lookupConstants);
                            shell.onLoginEvent(loginEvent);

                            final Place currentPlace = placeController.getWhere();
                            if (currentPlace instanceof LoginPlace) {// explicit
                                                                     // login
                                final LoginPlace loginPlace = (LoginPlace) currentPlace;
                                if (loginPlace.getHistoryToken() != null && !"".equals(loginPlace.getHistoryToken())) {
                                    History.newItem(loginPlace.getHistoryToken());
                                } else {
                                    placeController.goTo(new HomePlace());
                                }
                            } else {
                                // this was an intercepted login so we leave
                                // user on current page
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onLogoutEvent(final LogoutEvent logoutEvent) {
        setCurrentUser(null);
        shell.onLogoutEvent(logoutEvent);
        placeController.goTo(new LoginPlace());
    }

    /* The progressbar */
    private Element progressbar;

    private void setProgress(final int progress) {
        if (progressbar == null) {
            progressbar = Document.get().getElementById("progressbar");
        }
        progressbar.setAttribute("style", "width: " + progress + "%;");
    }

}
