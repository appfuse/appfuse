package org.appfuse.webapp.client.application.base.activity;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.ApplicationProxyFactory;
import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.application.ApplicationViewFactory;
import org.appfuse.webapp.client.ui.AbstractShell;
import org.appfuse.webapp.requests.ApplicationRequestFactory;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;

public abstract class AbstractBaseActivity extends AbstractActivity {

	protected final Place currentPlace;
	protected final AbstractShell shell;
	protected final Application application;
	protected final EventBus eventBus;
    protected final ApplicationRequestFactory requests;
    protected final PlaceController placeController;
    protected final ApplicationViewFactory viewFactory;
    protected final ApplicationProxyFactory proxyFactory;
    protected final ValidatorFactory validatorFactory;
    protected final ApplicationResources i18n;
    
    private String title;
    private String bodyId;
    private String bodyClassname;
    
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
		this.proxyFactory = application.getProxyFactory();
		this.validatorFactory = application.getValidatorFactory();
		this.i18n = application.getI18n();
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getBodyId() {
		return bodyId;
	}

	public String getBodyClassName() {
		return bodyClassname;
	}
	
	public String getBodyClassname() {
		return bodyClassname;
	}

	public void setBodyClassname(String bodyClassname) {
		this.bodyClassname = bodyClassname;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBodyId(String bodyId) {
		this.bodyId = bodyId;
	}

	/**
	 * 
	 * @return
	 */
	protected Validator getValidator() {
		return validatorFactory.getValidator();
	}
	
	//Mobile interface
	
	protected Place getBackButtonPlace() {
		return null;
	}

	protected String getBackButtonText() {
		return null;
	}

	protected Place getEditButtonPlace() {
		return null;
	}

	protected boolean hasEditButton() {
		return false;
	}
	
}
