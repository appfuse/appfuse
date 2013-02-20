package org.appfuse.webapp.client.application.base.activity;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.view.ProxyEditView;

import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Abstract activity for editing a record. Subclasses must provide access to the
 * request that will be fired when Save is clicked.
 * <p>
 * Instances are not reusable. Once an activity is stoped, it cannot be
 * restarted.
 * 
 * Required methods are:
 * <ol>
 * 	<li> {@link #createView(Place)}</li>
 * 	<li> {@link #createProxyRequest()}</li>
 * 	<li> {@link #saveOrUpdateRequest(RequestContext, EntityProxy)}</li>
 * 	<li> {@link #deleteRequest(RequestContext, EntityProxy)}</li>
 * 	<li> {@link #nextPlace(boolean)}</li>
 * 	<li> {@link #previousPlace()}</li>
 * </ol>
 * 
 * Customization:
 * <ol>
 * 	<li> {@link #createProxy(RequestContext)}</li>
 * 	<li> {@link #getProxyId()}</li>
 * 	<li> {@link #getProxyClass()}</li>
 * 	<li> {@link #findProxyRequest(RequestContext, EntityProxyId)}</li>
 * 	<li> {@link #getSavedMessage()}</li>
 * 	<li> {@link #getDeletedMessage()}</li>
 * </ol>
 * 
 * @param <P> the type of Proxy being edited
 */
public abstract class AbstractProxyEditActivity<P extends EntityProxy> extends AbstractBaseActivity implements Activity,  ProxyEditView.Delegate {

	protected ProxyEditView<P, ?> view;
	protected RequestFactoryEditorDriver<P, ?> editorDriver;
	protected P entityProxy;
	private boolean waiting;
	
	private String savedMessage = i18n.entity_saved();
	private String deletedMessage = i18n.entity_deleted();
	private String deleteConfirmation = i18n.delete_confirm("");

	public String getSavedMessage() {
		return savedMessage;
	}

	public void setSavedMessage(String savedMessage) {
		this.savedMessage = savedMessage;
	}

	public String getDeletedMessage() {
		return deletedMessage;
	}

	public void setDeletedMessage(String deletedMessage) {
		this.deletedMessage = deletedMessage;
	}
	
	public String getDeleteConfirmation() {
		return deleteConfirmation;
	}

	public void setDeleteConfirmation(String deleteConfirmation) {
		this.deleteConfirmation = deleteConfirmation;
	}


	/**
	 * Called on {@link #start(AcceptsOneWidget, EventBus)}.
	 * @return the view for editing
	 */
	protected abstract ProxyEditView<P, ?> createView(Place place);
	
	/**
	 * Called once to create the appropriate request to create/find and edit this entity and 
	 * that will be fired when the save button is clicked.
	 * 
	 * @return the request context to edit this entity proxy.
	 */
	protected abstract RequestContext createProxyRequest();

	/**
	 * 
	 * @param requestContext
	 * @return
	 */
	protected P createProxy(RequestContext requestContext) {
		return (P) requestContext.create(getProxyClass());
	}
	/**
	 * 
	 * @param requestContext
	 * @param proxyId
	 * @return
	 */
	protected Request<P> findProxyRequest(RequestContext requestContext, EntityProxyId<P> proxyId) {
		return requestContext.find(proxyId);		
	}

	/**
	 * 
	 * @param requestContext
	 * @param proxy
	 * @return
	 */
	protected abstract RequestContext saveOrUpdateRequest(RequestContext requestContext, P proxy);
	
	/**
	 * Called on {@link #deleteClicked()} to create the appropriate request to delete entity.
	 * 
	 * @return the request context to fire when the delete button is clicked
	 */
	protected abstract RequestContext deleteRequest(RequestContext requestContext, P proxy);

	
	/**
	 * Creates the {@link Place} to go after successfully saved or deleted this entity.
	 * @param saved 
	 * @return
	 */
	protected abstract Place nextPlace(boolean saved);

	/**
	 * Creates the {@link Place} to go when this activity is canceled.
	 * @param saved 
	 * @return
	 */
	protected abstract Place previousPlace();
	
	/**
	 * @param currentPlace
	 * @param view
	 */
	public AbstractProxyEditActivity(Application application) {
		super(application);
	}

	
	/**
	 * 
	 */
	public void start(AcceptsOneWidget display, EventBus eventBus) {
		view = createView(currentPlace);
		view.setDelegate(this);
		editorDriver = view.createEditorDriver();
		display.setWidget(view);
		
		loadEntityProxy();
	}
	
	protected EntityProxyId<P> getProxyId(){
		return (EntityProxyId<P>) ((EntityProxyPlace) currentPlace).getProxyId();
	}
	
	protected Class<? extends EntityProxy> getProxyClass(){
		return (Class<? extends EntityProxy>) ((EntityProxyPlace) currentPlace).getProxyClass();
	}

	
	/**
	 * 
	 */
	protected void loadEntityProxy() {
		EntityProxyId<P> proxyId = getProxyId();
		if(proxyId == null) {
			RequestContext requestContext = createProxyRequest();
			entityProxy = createProxy(requestContext);
			editorDriver.edit(entityProxy, saveOrUpdateRequest(requestContext, entityProxy));
		} else {
			findProxyRequest(createProxyRequest(), proxyId).with(editorDriver.getPaths())
			.fire(new Receiver<P>() {
				@Override
				public void onSuccess(P response) {
					entityProxy = response;
					editorDriver.edit(entityProxy, saveOrUpdateRequest(createProxyRequest(), entityProxy));
				}
			});
		}
	}


	/**
	 * 
	 * @see org.appfuse.webapp.client.application.base.view.ProxyEditView.Delegate#saveClicked()
	 */
	@Override
	public void saveClicked() {

		setWaiting(true);
		editorDriver.flush().fire(new Receiver<Void>() {
			/*
			 * Callbacks do nothing if editorDriver is null, we were stopped in
			 * midflight
			 */
			@Override
			public void onFailure(ServerFailure error) {
				if (editorDriver != null) {
					setWaiting(false);
					super.onFailure(error);
				}
			}

			@Override
			public void onSuccess(Void ignore) {
				if (editorDriver != null) {
					editorDriver = null;
					setWaiting(false);
					placeController.goTo(nextPlace(true));
					addMessage(getSavedMessage(), AlertType.SUCCESS);
				}
			}

			@Override
			public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
				if (editorDriver != null) {
					setWaiting(false);
					editorDriver.setConstraintViolations(violations);
				}
			}

		});
	}
	

	/**
	 * 
	 * @see org.appfuse.webapp.client.application.base.view.ProxyEditView.Delegate#deleteClicked()
	 */
	@Override
	public void deleteClicked() {
		if(!Window.confirm(getDeleteConfirmation())) {
			return;
		}
		deleteRequest(createProxyRequest(), entityProxy).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				placeController.goTo(nextPlace(false));
				addMessage(getDeletedMessage(), AlertType.SUCCESS);
			}
		});
	}

	/**
	 * 
	 * @see org.appfuse.webapp.client.application.base.view.ProxyEditView.Delegate#cancelClicked()
	 */
	public void cancelClicked() {
		String unsavedChangesWarning = mayStop();
		if ((unsavedChangesWarning == null)
				|| Window.confirm(unsavedChangesWarning)) {
			editorDriver = null;
			placeController.goTo(previousPlace());
		}
	}

	protected void clearMessages() {
		shell.clearMessages();
	}
	
	protected void addMessage(String html, AlertType alertType) {
		shell.addMessage(html, alertType);
	}
	
	/**
	 * 
	 * @see com.google.gwt.activity.shared.AbstractActivity#mayStop()
	 */
	public String mayStop() {
		if (isWaiting() || changed()) {
			return "Are you sure you want to abandon your changes? FIXME i18n"; //FIXME
		}

		return null;
	}

	/**
	 * 
	 * @see com.google.gwt.activity.shared.AbstractActivity#onCancel()
	 */
	public void onCancel() {
		onStop();
	}

	/**
	 * 
	 * @see com.google.gwt.activity.shared.AbstractActivity#onStop()
	 */
	public void onStop() {
		view.setDelegate(null);
		editorDriver = null;
	}

	/**
	 * 
	 * @return
	 */
	private boolean changed() {
		try {
			return editorDriver != null && editorDriver.flush().isChanged();
		} catch (Exception e) {
			Logger.getLogger("").log(Level.SEVERE, e.getMessage(), e);
			return false;
		}
	}

	/**
	 * @return true if we're waiting for an rpc response.
	 */
	protected boolean isWaiting() {
		return waiting;
	}

	/**
	 * While we are waiting for a response, we cannot poke setters on the proxy
	 * (that is, we cannot call editorDriver.flush). So we set the waiting flag to
	 * warn ourselves not to, and to disable the view.
	 */
	protected void setWaiting(boolean wait) {
		this.waiting = wait;
		view.setEnabled(!wait);
	}

}