/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.appfuse.webapp.client.application.base.activity;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.view.ProxyEditView;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Abstract activity for editing a record. Subclasses must provide access to the
 * request that will be fired when Save is clicked.
 * <p>
 * Instances are not reusable. Once an activity is stoped, it cannot be
 * restarted.
 * 
 * @param <P> the type of Proxy being edited
 */
public abstract class AbstractProxyEditActivity<P extends EntityProxy> extends AbstractBaseActivity implements Activity,  ProxyEditView.Delegate {

	protected ProxyEditView<P, ?> view;
	protected RequestFactoryEditorDriver<P, ?> editorDriver;
	protected P entityProxy;
	private boolean waiting;

	/**
	 * Called on {@link #start(AcceptsOneWidget, EventBus)}.
	 * @return the view for editing
	 */
	protected abstract ProxyEditView<P, ?> createView(Place place);
	
	/**
	 * Called once to create the appropriate request to save changes.
	 * 
	 * @return the request context to fire when the save button is clicked
	 */
	protected abstract RequestContext createSaveRequest(P proxy);

	/**
	 * Called on {@link #deleteClicked()} to create the appropriate request to delete entity.
	 * 
	 * @return the request context to fire when the delete button is clicked
	 */
	protected abstract RequestContext createDeleteRequest(P proxy);

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
	public AbstractProxyEditActivity(EntityProxyPlace currentPlace, Application application) {
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
	
	/**
	 * 
	 */
	protected void loadEntityProxy() {
		EntityProxyPlace entityProxyPlace = (EntityProxyPlace) currentPlace;
		EntityProxyId<P> proxyId = (EntityProxyId<P>)entityProxyPlace.getProxyId();
		if(proxyId == null) {
			entityProxy = (P) requests.userRequest().create(((EntityProxyPlace)currentPlace).getProxyClass());
			editorDriver.edit(entityProxy, createSaveRequest(entityProxy));			
		} else {
			requests.find(proxyId).with(editorDriver.getPaths()).fire(new Receiver<P>() {
				@Override
				public void onSuccess(P response) {
					entityProxy = response;
					editorDriver.edit(entityProxy, createSaveRequest(entityProxy));
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
		if (!changed()) {
			placeController.goTo(nextPlace(false));
		}

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
					// We want no warnings from mayStop, so:

					// Defeat isChanged check
					editorDriver = null;

					// Defeat call-in-flight check
					setWaiting(false);

					placeController.goTo(nextPlace(true));
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
		createDeleteRequest(entityProxy).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void response) {
				placeController.goTo(nextPlace(false));
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
		return false;
		//return editorDriver != null && editorDriver.flush().isChanged();
	}

	/**
	 * @return true if we're waiting for an rpc response.
	 */
	private boolean isWaiting() {
		return waiting;
	}

	/**
	 * While we are waiting for a response, we cannot poke setters on the proxy
	 * (that is, we cannot call editorDriver.flush). So we set the waiting flag to
	 * warn ourselves not to, and to disable the view.
	 */
	private void setWaiting(boolean wait) {
		this.waiting = wait;
		view.setEnabled(!wait);
	}

}