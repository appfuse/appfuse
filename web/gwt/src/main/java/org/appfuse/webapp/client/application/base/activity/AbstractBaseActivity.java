package org.appfuse.webapp.client.application.base.activity;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.ApplicationValidatorFactory;
import org.appfuse.webapp.client.application.ApplicationViewFactory;
import org.appfuse.webapp.client.ui.Shell;
import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;

public abstract class AbstractBaseActivity extends AbstractActivity {

	protected final Place currentPlace;
	protected final Shell shell;
	protected final Application application;
    protected final ApplicationRequestFactory requests;
    protected final PlaceController placeController;
    protected final ApplicationViewFactory viewFactory;
    protected final ApplicationValidatorFactory validatorFactory;
    
	/**
	 */
	public AbstractBaseActivity(Application application) {
		super();
		this.application = application;
		this.shell = application.getShell();
		this.requests = application.getRequestFactory();
		this.placeController = application.getPlaceController();
		this.currentPlace = placeController.getWhere();
		this.viewFactory = application.getViewFactory();
		this.validatorFactory = application.getValidatorFactory();
	}
	
}
