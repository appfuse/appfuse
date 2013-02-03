package org.appfuse.webapp.client.application.base.activity;

import java.util.List;
import java.util.logging.Logger;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.view.ProxySearchView;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * Abstract activity for displaying a list of {@link EntityProxy}. These
 * activities are not re-usable. Once they are stopped, they cannot be
 * restarted.
 * <p/>
 * Subclasses must:
 * <p/>
 * <ul>
 * <li>provide a {@link ProxySearchView}
 * <li>implement method to request a full count
 * <li>implement method to find a range of entities
 * <li>respond to "show details" commands
 * </ul>
 * <p/>
 * Only the properties required by the view will be requested.
 *
 * @param <P> the type of {@link EntityProxy} listed
 */
public abstract class AbstractProxySearchActivity<P extends EntityProxy, S extends BaseProxy> extends AbstractBaseActivity implements Activity, ProxySearchView.Delegate<P> {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	
	protected final Class<S> searchCriteriaType;
	protected final EntityListPlace currentPlace;
	protected ProxySearchView<P, S> view;
	
	private SingleSelectionModel<P> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private AcceptsOneWidget panel;
	private S searchCriteria;

	protected abstract ProxySearchView<P, S> createView();
	
	protected abstract RequestContext createRequestContext();
	protected abstract Request<Long> createCountRequest(RequestContext requestContext, S searchCriteria);
	protected abstract Request<List<P>> createSearchRequest(RequestContext requestContext, S searchCriteria, int firsResult, int maxResults);
	
	
	public AbstractProxySearchActivity(Application application, Class<S> searchCriteriaType) {
		super(application);
		this.currentPlace = (EntityListPlace) application.getPlaceController().getWhere();
		this.searchCriteriaType = searchCriteriaType;
	}
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.panel = panel;
		view = createView();
		view.setDelegate(this);
		
		searchCriteria = (S) currentPlace.getSearchCriteria();
		if(searchCriteria == null) {
			//searchCriteria = proxyFactory.create(searchCriteriaType);
			RequestContext requestContext = createRequestContext();
			searchCriteria = requestContext.create(searchCriteriaType);
			requestContext.fire();
			logger.info("Created new UsersSearchCriteriaProxy " + searchCriteria);
		}
		view.setSearchCriteria(searchCriteria);
		
		if(currentPlace.getMaxResults() > 0) {
			view.setPageSize(currentPlace.getMaxResults());
		}
		
		final HasData<P> hasData = view.asHasData();
		rangeChangeHandler = hasData.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				AbstractProxySearchActivity.this.onRangeChanged(hasData, hasData.getVisibleRange());
			}
		});

		// Inherit the view's key provider
		ProvidesKey<P> keyProvider = ((AbstractHasData<P>) hasData).getKeyProvider();
		selectionModel = new SingleSelectionModel<P>(keyProvider);
		hasData.setSelectionModel(selectionModel);

		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				P selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					showDetails(selectedObject);
				}
			}
		});		

		// Select the current page range to load (by default or from place tokens)
		Range range = view.asHasData().getVisibleRange();
		if(currentPlace.getFirstResult() > 0 || 
				(currentPlace.getMaxResults() != range.getLength() && currentPlace.getMaxResults() > 0)) 
		{
			range = new Range(currentPlace.getFirstResult(), currentPlace.getMaxResults());			
		}

		loadItems(searchCriteria, range);
	}	

	
	protected void loadItems(final S searchCriteria) {
		loadItems(searchCriteria, new Range(0, currentPlace.getMaxResults()));
	}
	
	/**
	 * Load items on start.
	 */
	protected void loadItems(final S searchCriteria, final Range range) {
		RequestContext requestContext = createRequestContext();
		createCountRequest(requestContext, searchCriteria).fire(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				view.asHasData().setRowCount(response.intValue(), true);
				onRangeChanged(view.asHasData(), range);
			}
		});
	}
	
	/**
	 * Called by the table as it needs data.
	 */
	protected void onRangeChanged(final HasData<P> listView, final Range range) {
		RequestContext requestContext = createRequestContext();
		createSearchRequest(requestContext, searchCriteria, range.getStart(), range.getLength())
			.with(view.getPaths()).fire( new Receiver<List<P>>() {
				@Override
				public void onSuccess(List<P> values) {
					if (view == null) {
						// This activity is dead
						return;
					}
					view.asHasData().setRowData(range.getStart(), values);
					if (panel != null) {
						panel.setWidget(view);
					}
					//create a new history token for this range
					newHistoryToken(searchCriteria, range.getStart(), range.getLength());
				}
			});
	}

	protected void setSearchCriteria(S searchCriteria) {
//		if(searchCriteria != null) {
//			this.frozenSearchCriteria = proxyFactory.clone(searchCriteria);
//			proxyFactory.setFrozen(frozenSearchCriteria, true);
//		} else {
//			this.frozenSearchCriteria = null;
//		}
		this.searchCriteria = searchCriteria;
	}
	
	protected void newHistoryToken(S searchCriteria, int firstResult, int maxResults) {
		//TODO				
//					String historyToken = new EntityListPlace.Tokenizer().getFullHistoryToken(currentPlace);
//					History.newItem(historyToken, false);
	}

	
	@Override
	public void addClicked() {
		placeController.goTo(new EntityProxyPlace(currentPlace.getProxyClass()));
	}

	@Override
	public void showDetails(P record) {
		placeController.goTo(new EntityProxyPlace(record.stableId(), EntityProxyPlace.Operation.EDIT));		
	}
	
	@Override
	public void deleteClicked(P record) {
		Window.alert("deleteClicked");
	}
	
	public void onCancel() {
		onStop();
	}

	public void onStop() {
		view.setDelegate(null);
		view = null;
		rangeChangeHandler.removeHandler();
		rangeChangeHandler = null;
	}
}
