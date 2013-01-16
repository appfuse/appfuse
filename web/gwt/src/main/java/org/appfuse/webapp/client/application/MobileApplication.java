package org.appfuse.webapp.client.application;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.appfuse.webapp.client.ui.MobileShell;
import org.appfuse.webapp.client.ui.login.LoginActivity;
import org.appfuse.webapp.client.ui.login.LoginPlace;
import org.appfuse.webapp.client.ui.login.events.AuthRequiredEvent;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.proxies.LookupConstantsProxy;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.requests.ApplicationRequestFactory;
import org.appfuse.webapp.requests.RequestEvent;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;


/**
 * Application for browsing entities.
 */
public class MobileApplication extends Application {
	private static final Logger LOGGER = Logger.getLogger(MobileApplication.class.getName());

	@Inject
	public MobileApplication(
			MobileShell shell,
			ApplicationRequestFactory requestFactory, 
			EventBus eventBus,
			PlaceController placeController,
			ApplicationViewFactory viewFactory,
			ApplicationValidatorFactory validatorFactory) {
		super(shell, requestFactory, eventBus, placeController, viewFactory, validatorFactory);
	}

	
	public void run() {
		setProgress(10);

		/* Add handlers */
		initHandlers();
		setProgress(30);
		


		/* Browser history integration */
		ApplicationPlaceHistoryMapper historyMapper = GWT.create(ApplicationPlaceHistoryMapper.class);
        final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        
        /* Authentication */
        requestFactory.userRequest().getCurrentUser().fire(new Receiver<UserProxy>() {

			@Override
			public void onSuccess(UserProxy currentUser) {
				setProgress(50);

				if(currentUser != null) {
					setCurrentUser(currentUser);
					/* load application constants */
					requestFactory.lookupRequest().getApplicationConstants().fire(new Receiver<LookupConstantsProxy>() {
						@Override
						public void onSuccess(LookupConstantsProxy response) {
							setLookupConstants(response);
							
							showShell();

							/* Register home place and parse url for current place token */
							Place defaultPlace = new MainMenuPlace();
							historyHandler.register(placeController, eventBus, defaultPlace);
							historyHandler.handleCurrentHistory();

						}
					});
				} else {
					showShell();
					
					/* Register home place and parse url for current place token */
					Place defaultPlace = new LoginPlace();
					historyHandler.register(placeController, eventBus, defaultPlace);
					placeController.goTo(defaultPlace);
				}
			}
			
			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("Error " + error.getMessage());
				super.onFailure(error);
			}
    	   
       });
		
	}

	
	protected void showShell() {
		setProgress(80);

		/* setup activities and display */
		final ActivityMapper activityMapper = new ApplicationActivityMapper(this);
		final ActivityManager masterActivityManager = new ActivityManager(activityMapper, eventBus);
		masterActivityManager.setDisplay(shell.getContentsPanel());
		
		setProgress(95);
		Element loading = Document.get().getElementById("loading");
		loading.getParentElement().removeChild(loading);
		
		/* And show the user the shell */
		RootLayoutPanel.get().add(shell);		
	}

	protected void initHandlers() {
		//Custom request transport
		requestFactory.initialize(eventBus, new CustomDefaultRequestTransport(eventBus));
		
		AuthRequiredEvent.register(eventBus, new LoginActivity(this));

		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				Window.alert("Error: " + e.getMessage());
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		});

		if (LogConfiguration.loggingIsEnabled()) {
			// Add remote logging handler
			RequestFactoryLogHandler.LoggingRequestProvider provider = new RequestFactoryLogHandler.LoggingRequestProvider() {
				public LoggingRequest getLoggingRequest() {
					return requestFactory.loggingRequest();
				}
			};
			Logger.getLogger("").addHandler(new RequestFactoryLogHandler(provider, Level.WARNING, new ArrayList<String>()));
		}

		RequestEvent.register(eventBus, new RequestEvent.Handler() {
			// Only show loading status if a request isn't serviced in 250ms.
			private static final int LOADING_TIMEOUT = 250;

			public void onRequestEvent(RequestEvent requestEvent) {
				if(shell != null && shell.getMole() != null) {
					if (requestEvent.getState() == RequestEvent.State.SENT) {
						shell.getMole().showDelayed(LOADING_TIMEOUT);
					} else {
						shell.getMole().hide();
					}
				}
			}
		});

	}
	
	/* The progressbar */
	private Element progressbar;
	private void setProgress(int progress) {
		if(progressbar == null) {
			progressbar = Document.get().getElementById("progressbar");
		}
		progressbar.setAttribute("style", "width: " + progress + "%;");
	}
}
