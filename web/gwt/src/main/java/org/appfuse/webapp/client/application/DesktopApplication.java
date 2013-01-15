package org.appfuse.webapp.client.application;


import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.appfuse.webapp.client.ui.DesktopShell;
import org.appfuse.webapp.client.ui.login.AuthRequiredEvent;
import org.appfuse.webapp.client.ui.login.LoginActivity;
import org.appfuse.webapp.client.ui.login.LoginPlace;
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
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryLogHandler;
import com.google.web.bindery.requestfactory.shared.LoggingRequest;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;


/**
 * Application for browsing entities.
 */
public class DesktopApplication extends Application {
	private static final Logger LOGGER = Logger.getLogger(DesktopApplication.class.getName());

	@Inject
	public DesktopApplication(
			DesktopShell shell,
			ApplicationRequestFactory requestFactory, 
			EventBus eventBus,
			PlaceController placeController,
			ApplicationViewFactory viewFactory,
			ApplicationValidatorFactory validatorFactory) {
		super(shell, requestFactory, eventBus, placeController, viewFactory, validatorFactory);
	}

	
	
	public void run() {
		setProgress(30);

		/* Add handlers */
		initHandlers();

		/* Browser history integration */
		ApplicationPlaceHistoryMapper historyMapper = GWT.create(ApplicationPlaceHistoryMapper.class);
        final PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        
		/* setup activities and display */
		final ActivityMapper activityMapper = new ApplicationActivityMapper(this);
		final ActivityManager masterActivityManager = new ActivityManager(activityMapper, eventBus);
		masterActivityManager.setDisplay(shell.getContentsPanel());
        
        
        /* Authentication */
		setProgress(50);
        requestFactory.userRequest().getCurrentUser().fire(new Receiver<UserProxy>() {

			@Override
			public void onSuccess(UserProxy currentUser) {

				if(currentUser != null) {
					setProgress(70);
					setCurrentUser(currentUser);
					/* load application constants */
					requestFactory.lookupRequest().getApplicationConstants().fire(new Receiver<LookupConstantsProxy>() {
						@Override
						public void onSuccess(LookupConstantsProxy response) {
							setLookupConstants(response);

							setProgress(80);
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
		Element loading = Document.get().getElementById("loading");
		loading.getParentElement().removeChild(loading);
		
		/* And show the user the shell */
		RootLayoutPanel.get().add(shell);
		
		//remove gwt positioning and overflow from extra divs, and hope for the best..
		shell.getElement().setId("shell");
		__fixPositioningAndOverflow(Document.get().getElementById("shell"));
	}
	
	/**
	 * remove gwt positioning and overflow from extra divs, and hope for the best..
	 */
	private Element __fixPositioningAndOverflow(Element element) {
		if("body".equalsIgnoreCase(element.getTagName())){
			return element;
		} else {
			element.removeAttribute("style");
			return __fixPositioningAndOverflow(element.getParentElement());
		}
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
				if (requestEvent.getState() == RequestEvent.State.SENT) {
					shell.getMole().showDelayed(LOADING_TIMEOUT);
				} else {
					shell.getMole().hide();
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
	          receiver.onTransportFailure(new ServerFailure(exception.getMessage()));
	        }

	        public void onResponseReceived(Request request, Response response) {
	          wireLogger.finest("Response received");
	          if (Response.SC_UNAUTHORIZED == response.getStatusCode()) {
	        	  eventBus.fireEvent(new AuthRequiredEvent());
	          } else if (Response.SC_OK == response.getStatusCode()) {
	            String text = response.getText();
	            receiver.onTransportSuccess(text);
	          } else {
	            String message = SERVER_ERROR + " " + response.getStatusCode() + " " + response.getText();
	            wireLogger.severe(message);
	            receiver.onTransportFailure(new ServerFailure(message));
	          }
	        }
	      };
	}
}
