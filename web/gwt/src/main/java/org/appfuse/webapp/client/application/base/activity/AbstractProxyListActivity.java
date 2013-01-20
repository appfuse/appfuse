package org.appfuse.webapp.client.application.base.activity;

import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.view.ProxyListView;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

/**
 * Abstract activity for displaying a list of {@link EntityProxy}. These
 * activities are not re-usable. Once they are stopped, they cannot be
 * restarted.
 * <p/>
 * Subclasses must:
 * <p/>
 * <ul>
 * <li>provide a {@link ProxyListView}
 * <li>implement method to request a full count
 * <li>implement method to find a range of entities
 * <li>respond to "show details" commands
 * </ul>
 * <p/>
 * Only the properties required by the view will be requested.
 *
 * @param <P> the type of {@link EntityProxy} listed
 */
public abstract class AbstractProxyListActivity<P extends EntityProxy> extends AbstractBaseActivity implements Activity, ProxyListView.Delegate<P> {

	protected final EntityListPlace currentPlace;
	protected ProxyListView<P> view;
	
	private SingleSelectionModel<P> selectionModel;
	private HandlerRegistration rangeChangeHandler;
	private AcceptsOneWidget panel;

	protected abstract ProxyListView<P> createView();
	protected abstract Request<List<P>> createRangeRequest(Range range);
	protected abstract void fireCountRequest(Receiver<Long> callback);	
	
	public AbstractProxyListActivity(EntityListPlace currentPlace, Application application) {
		super(application);
		this.currentPlace = currentPlace;
	}
	
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.panel = panel;
		view = createView();
		view.setDelegate(this);
		if(currentPlace.getMaxResults() > 0) {
			view.setPageSize(currentPlace.getMaxResults());
		}
		
		final HasData<P> hasData = view.asHasData();
		rangeChangeHandler = hasData.addRangeChangeHandler(new RangeChangeEvent.Handler() {
			public void onRangeChange(RangeChangeEvent event) {
				AbstractProxyListActivity.this.onRangeChanged(hasData, hasData.getVisibleRange());
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
		Range range = hasData.getVisibleRange();
		if(currentPlace.getFirstResult() > 0 || 
				(currentPlace.getMaxResults() != range.getLength() && currentPlace.getMaxResults() > 0)) 
		{
			range = new Range(currentPlace.getFirstResult(), currentPlace.getMaxResults());			
		}
		loadListItems(range);
	}	

	/**
	 * Load items on start.
	 */
	protected void loadListItems(final Range range) {
		fireCountRequest(new Receiver<Long>() {
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

		final Receiver<List<P>> callback = new Receiver<List<P>>() {
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
				currentPlace.setFirstResult(range.getStart());
				currentPlace.setMaxResults(range.getLength());
				String historyToken = new EntityListPlace.Tokenizer().getFullHistoryToken(currentPlace);
				History.newItem(historyToken, false);
			}
		};

		createRangeRequest(range).with(view.getPaths()).fire(callback);
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
	public void deleteClicked() {
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
