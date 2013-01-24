package org.appfuse.webapp.client.application.base.activity;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.application.ApplicationViewFactory;
import org.appfuse.webapp.client.ui.Shell;
import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public abstract class AbstractBaseActivity extends AbstractActivity {

	protected final Place currentPlace;
	protected final Shell shell;
	protected final Application application;
	protected final EventBus eventBus;
    protected final ApplicationRequestFactory requests;
    protected final PlaceController placeController;
    protected final ApplicationViewFactory viewFactory;
    protected final ValidatorFactory validatorFactory;
    protected final ApplicationResources i18n = GWT.create(ApplicationResources.class);
    
	/**
	 */
	public AbstractBaseActivity(Application application) {
		super();
		this.application = application;
		this.shell = application.getShell();
		this.eventBus = application.getEventBus();
		this.requests = application.getRequestFactory();
		this.placeController = application.getPlaceController();
		this.currentPlace = placeController.getWhere();
		this.viewFactory = application.getViewFactory();
		this.validatorFactory = application.getValidatorFactory();
	}
	
	protected Validator getValidator() {
		return validatorFactory.getValidator();
	}
}
