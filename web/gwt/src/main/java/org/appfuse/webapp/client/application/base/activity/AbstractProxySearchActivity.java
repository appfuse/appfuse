package org.appfuse.webapp.client.application.base.activity;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.view.ProxySearchView;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
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
 * @param <S> the type of {@link BaseProxy} acting as search criteria
 */
public abstract class AbstractProxySearchActivity<P extends EntityProxy, S extends BaseProxy> extends AbstractBaseActivity implements Activity, ProxySearchView.Delegate<P> {

	protected final Logger logger = Logger.getLogger(getClass().getName());
	
	protected final Class<S> searchCriteriaType;
	protected final EntityListPlace currentPlace;
	protected ProxySearchView<P, S> view;
	
	private SingleSelectionModel<P> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private S searchCriteria;

	protected abstract ProxySearchView<P, S> createView();
	
	protected abstract RequestContext createRequestContext();
	protected abstract Request<Long> createCountRequest(RequestContext requestContext, S searchCriteria);
	protected abstract Request<List<P>> createSearchRequest(RequestContext requestContext, S searchCriteria, Range range, ColumnSortList columnSortList);
	
	
	public AbstractProxySearchActivity(Application application, Class<S> searchCriteriaType) {
		super(application);
		this.currentPlace = (EntityListPlace) application.getPlaceController().getWhere();
		this.searchCriteriaType = searchCriteriaType;
	}
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = createView();
		view.setDelegate(this);
		panel.setWidget(view);
		
		searchCriteria = (S) currentPlace.getSearchCriteria();
		if(searchCriteria == null) {
			searchCriteria = proxyFactory.create(searchCriteriaType);
		}
		view.setSearchCriteria(searchCriteria);
		
		if(currentPlace.getMaxResults() > 0) {
			view.setPageSize(currentPlace.getMaxResults());
		}
		
		final CellTable<P> cellTable = view.getCellTable();
		rangeChangeHandler = cellTable.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				AbstractProxySearchActivity.this.onRangeChanged(cellTable, cellTable.getVisibleRange());
			}
		});
		
		// Select the current page range to load (by default or from place tokens)
		Range range = view.getCellTable().getVisibleRange();
		if(currentPlace.getFirstResult() > 0 || 
				(currentPlace.getMaxResults() != range.getLength() && currentPlace.getMaxResults() > 0)) 
		{
			range = new Range(currentPlace.getFirstResult(), currentPlace.getMaxResults());			
		}

		loadItems(searchCriteria, range);
	}	

	
	protected void loadItems(final S searchCriteria) {
		// Select the current page size to load
		Range currentRange = view.getCellTable().getVisibleRange();
		loadItems(searchCriteria, new Range(0, currentRange.getLength()));
	}
	
	/**
	 * Load items on start.
	 */
	protected void loadItems(final S searchCriteria, final Range range) {
		proxyFactory.setFrozen(searchCriteria, true);
		final RequestContext requestContext = createRequestContext();
		createCountRequest(requestContext, searchCriteria).fire(new Receiver<Long>() {
			@Override
			public void onSuccess(Long response) {
				if (view == null) {
					// This activity is dead
					return;
				}
				view.getCellTable().setRowCount(response.intValue(), true);
				onRangeChanged(view.getCellTable(), range);
			}
		});
	}
	
	/**
	 * Called by the table as it needs data.
	 */
	protected void onRangeChanged(final CellTable<P> cellTable, final Range range) {
		final RequestContext requestContext = createRequestContext();
		createSearchRequest(requestContext, searchCriteria, range, cellTable.getColumnSortList())
			.with(view.getPaths()).fire( new Receiver<List<P>>() {
				@Override
				public void onSuccess(List<P> results) {
					if (view == null) {
						// This activity is dead
						return;
					}
					cellTable.setRowData(range.getStart(), results);
					newHistoryToken(searchCriteria, range.getStart(), range.getLength());
				}
			});
	}
	
	protected void newHistoryToken(S searchCriteria, int firstResult, int maxResults) {
		String historyToken = new EntityListPlace.Tokenizer(proxyFactory, requests)
			.getFullHistoryToken(new EntityListPlace(currentPlace.getProxyClass(), firstResult, maxResults, searchCriteria));
		History.newItem(historyToken, false);
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
	public void searchClicked() {
		proxyFactory.setFrozen(searchCriteria, false);
		searchCriteria = view.getEditorDriver().flush();
		if(view.getEditorDriver().hasErrors()) {
			return;
		}
		Set<ConstraintViolation<?>> violations = validate(searchCriteria);
		if(violations != null && !violations.isEmpty()) {
			view.getEditorDriver().setConstraintViolations(violations);
		}
		loadItems(searchCriteria);
	}
	
	/**
	 * Validates given searchCriteria.
	 * 
	 * Override if you want to apply validation, example:
	 * <code><pre>
	 * protected Set<ConstraintViolation<?>> validate(S searchCriteria){
	 * 	(Set) getValidator().validate(searchCriteria);
	 * }
	 * </pre></code>
	 * @param searchCriteria
	 * @return
	 */
	protected Set<ConstraintViolation<?>> validate(S searchCriteria){
		return null;//
	}
	
	@Override
	public void deleteClicked(P record) {
		Window.alert("deleteClicked");
	}
	

	@Override
	public void cancelClicked() {
		placeController.goTo(new MainMenuPlace());
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
