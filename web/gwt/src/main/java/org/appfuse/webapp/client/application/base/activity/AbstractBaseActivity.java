package org.appfuse.webapp.client.application.base.activity;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.ApplicationProxyFactory;
import org.appfuse.webapp.client.application.ApplicationResources;
import org.appfuse.webapp.client.requests.ApplicationRequestFactory;
import org.appfuse.webapp.client.ui.Shell;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.dom.client.Document;
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
    protected final ApplicationProxyFactory proxyFactory;
    protected final ValidatorFactory validatorFactory;
    protected final ApplicationResources i18n;

    private String title;
    private String bodyId;
    private String bodyClassname;

    /**
     */
    public AbstractBaseActivity(final Application application) {
        super();
        this.application = application;
        this.shell = application.getShell();
        this.eventBus = application.getEventBus();
        this.requests = application.getRequestFactory();
        this.placeController = application.getPlaceController();
        this.currentPlace = placeController.getWhere();
        this.proxyFactory = application.getProxyFactory();
        this.validatorFactory = application.getValidatorFactory();
        this.i18n = application.getI18n();
    }

    protected void setDocumentTitleAndBodyAttributtes() {
        // set document title and body class/id
        if (getTitle() != null) {
            Document.get().setTitle(getTitle() + " | " + application.getI18n().webapp_name());
        } else {
            Document.get().setTitle(application.getI18n().webapp_name());
        }

        if (getBodyId() != null) {
            Document.get().getBody().setId(getBodyId());
        } else {
            Document.get().getBody().removeAttribute("id");
        }

        if (getBodyClassName() != null) {
            Document.get().getBody().setClassName(getBodyClassName());
        } else {
            Document.get().getBody().removeAttribute("class");
        }
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

    public void setBodyClassname(final String bodyClassname) {
        this.bodyClassname = bodyClassname;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setBodyId(final String bodyId) {
        this.bodyId = bodyId;
    }

    /**
     * 
     * @return
     */
    protected Validator getValidator() {
        return validatorFactory.getValidator();
    }

    // Mobile interface

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
