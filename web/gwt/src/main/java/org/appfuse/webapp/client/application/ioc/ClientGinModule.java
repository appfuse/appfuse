package org.appfuse.webapp.client.application.ioc;


import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.DesktopApplication;
import org.appfuse.webapp.client.application.base.request.EventSourceRequestTransport;
import org.appfuse.webapp.client.requests.ApplicationRequestFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;


public class ClientGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(Application.class).to(DesktopApplication.class).in(Singleton.class);
		bind(ApplicationRequestFactory.class).toProvider(RequestFactoryProvider.class).in(Singleton.class);
		bind(PlaceController.class).toProvider(PlaceControllerProvider.class).in(Singleton.class);
	}
	
	
	static class PlaceControllerProvider implements Provider<PlaceController> {
		private final PlaceController placeController;

		@Inject
		public PlaceControllerProvider(EventBus eventBus) {
			this.placeController = new PlaceController(eventBus);
		}

		public PlaceController get() {
			return placeController;
		}
	}

	static class RequestFactoryProvider implements Provider<ApplicationRequestFactory> {
		private final ApplicationRequestFactory requestFactory;

		@Inject
		public RequestFactoryProvider(EventBus eventBus) {
			requestFactory = GWT.create(ApplicationRequestFactory.class);
			requestFactory.initialize(eventBus, new EventSourceRequestTransport(eventBus));
		}

		public ApplicationRequestFactory get() {
			return requestFactory;
		}
	}

}
