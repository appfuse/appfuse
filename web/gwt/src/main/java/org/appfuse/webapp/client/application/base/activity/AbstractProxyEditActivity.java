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
 *	<li> {@link #loadProxyRequest(RequestContext, EntityProxyId)}</li>
 * 	<li> {@link #saveOrUpdateRequest(RequestContext, EntityProxy)}</li>
 * 	<li> {@link #deleteRequest(RequestContext, EntityProxy)}</li>
 * 	<li> {@link #nextPlace(boolean)}</li>
 * 	<li> {@link #previousPlace()}</li>
 * </ol>
 * 
 * Customization:
 * <ol>
 * 	<li> {@link #createProxy(RequestContext)}</li>
 * 	<li> {@link #getEntityId()}</li>
 * 	<li> {@link #getProxyClass()}</li>
 * 	<li> {@link #loadProxyRequest(RequestContext, EntityProxyId)}</li>
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
	
	private String abandonChangesMessage = "Are you sure you want to abandon your changes?";//FIXME i18n
	private String savedMessage = i18n.entity_saved();
	private String deletedMessage = i18n.entity_deleted();
	private String deleteConfirmation = i18n.delete_confirm("");

	public String getAbandonChangesMessage() {
		return abandonChangesMessage;
	}

	public void setAbandonChangesMessage(String abandonChangesMessage) {
		this.abandonChangesMessage = abandonChangesMessage;
	}

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
	 * Create are Request to load this entity proxy.
	 * 
	 * Note: Request.find() method is disabled in the server for security reasons.
	 * 
	 * @param requestContext
	 * @param proxyId
	 * @return
	 */
	protected abstract Request<P> loadProxyRequest(RequestContext requestContext, String proxyId);

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
	public void start(final AcceptsOneWidget display, EventBus eventBus) {
		view = createView(currentPlace);
		view.setDelegate(this);
		editorDriver = view.createEditorDriver();
		
		doLoadEntityProxy(new Receiver<P>() {
			@Override
			public void onSuccess(P response) {
				entityProxy = response;
				display.setWidget(view);
			}
		});
	}
	
	protected String getEntityId(){
		if(currentPlace instanceof EntityProxyPlace) {
			return ((EntityProxyPlace) currentPlace).getEntityId();
		}
		return null;
	}
	
	protected Class<? extends EntityProxy> getProxyClass(){
		if(currentPlace instanceof EntityProxyPlace) {
			return (Class<? extends EntityProxy>) ((EntityProxyPlace) currentPlace).getProxyClass();
		}
		return null;
	}

	
	/**
	 * 
	 */
	protected void doLoadEntityProxy(final Receiver<P> onloadCallback) {
		String proxyId = getEntityId();
		if(proxyId == null) {
			//create a brand new proxy entity
			RequestContext requestContext = createProxyRequest();
			P proxy = createProxy(requestContext);
			//edit this entity proxy on the same request it was created
			editorDriver.edit(proxy, saveOrUpdateRequest(requestContext, proxy));
			//finish loading
			onloadCallback.onSuccess(proxy);
		} else {
			//find this entity proxy on the server
			loadProxyRequest(createProxyRequest(), proxyId)
				.with(editorDriver.getPaths())
				.fire(new Receiver<P>() {
					@Override
					public void onSuccess(P response) {
						//edit this entity proxy on a new request
						editorDriver.edit(response, saveOrUpdateRequest(createProxyRequest(), response));
						//finish loading
						onloadCallback.onSuccess(response);
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
					throw new RuntimeException(error.getMessage());//FIXME
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
			return getAbandonChangesMessage();
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
			return editorDriver != null && editorDriver.isDirty();
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