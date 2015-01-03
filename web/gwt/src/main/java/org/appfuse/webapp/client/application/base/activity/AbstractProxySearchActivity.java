package org.appfuse.webapp.client.application.base.activity;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.place.EntityProxyPlace;
import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.application.base.view.ProxySearchView;
import org.appfuse.webapp.client.ui.home.HomePlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
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
 * Subclasses must provide:
 * <p/>
 * <ul>
 * <li>{@link #createView()}
 * <li>{@link #createRequestContext()}
 * <li>{@link #createCountRequest(RequestContext, BaseProxy)}
 * <li>
 * {@link #createSearchRequest(RequestContext, BaseProxy, Range, ColumnSortList)}
 * </ul>
 * <p/>
 * Only the properties required by the view will be requested.
 *
 * @param <P>
 *            the type of {@link EntityProxy} listed
 * @param <S>
 *            the type of {@link BaseProxy} acting as search criteria
 */
public abstract class AbstractProxySearchActivity<P extends EntityProxy, S> extends AbstractBaseActivity implements Activity, ProxySearchView.Delegate<P> {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    private S searchCriteria;
    protected final Class<S> searchCriteriaType;
    protected final EntitySearchPlace currentPlace;

    protected ProxySearchView<P, S> view;
    private HandlerRegistration rangeChangeHandler;

    protected abstract RequestContext createRequestContext();

    protected abstract Request<Long> createCountRequest(RequestContext requestContext, S searchCriteria);

    protected abstract Request<List<P>> createSearchRequest(RequestContext requestContext, S searchCriteria, Range range, ColumnSortList columnSortList);

    public AbstractProxySearchActivity(final Application application, final ProxySearchView<P, S> view,
            final Class<S> searchCriteriaType) {
        super(application);
        this.currentPlace = (EntitySearchPlace) application.getPlaceController().getWhere();
        this.view = view;
        this.searchCriteriaType = searchCriteriaType;
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        view.setDelegate(this);
        panel.setWidget(view);
        setDocumentTitleAndBodyAttributtes();

        searchCriteria = (S) currentPlace.getSearchCriteria();
        if (searchCriteria == null) {
            searchCriteria = createSearchCriteria();
        }
        view.setSearchCriteria(searchCriteria);

        if (currentPlace.getMaxResults() > 0) {
            view.setPageSize(currentPlace.getMaxResults());
        }

        final HasData<P> hasData = view.asHasData();
        rangeChangeHandler = hasData.addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(final RangeChangeEvent event) {
                AbstractProxySearchActivity.this.onRangeChanged(hasData, hasData.getVisibleRange(), view.getColumnSortList());
            }
        });

        // Select the current page range to load (by default or from place
        // tokens)
        Range range = hasData.getVisibleRange();
        if (currentPlace.getFirstResult() > 0 ||
                (currentPlace.getMaxResults() != range.getLength() && currentPlace.getMaxResults() > 0))
        {
            range = new Range(currentPlace.getFirstResult(), currentPlace.getMaxResults());
        }

        loadItems(searchCriteria, range);
    }

    protected S createSearchCriteria() {
        if (!String.class.equals(searchCriteriaType)) {
            return (S) proxyFactory.create((Class<BaseProxy>) searchCriteriaType);
        }
        return null;
    }

    protected void loadItems(final S searchCriteria) {
        // Select the current page size to load
        final Range currentRange = view.asHasData().getVisibleRange();
        loadItems(searchCriteria, new Range(0, currentRange.getLength()));
    }

    /**
     * Load items on start.
     */
    protected void loadItems(final S searchCriteria, final Range range) {
        if (searchCriteria instanceof BaseProxy) {
            proxyFactory.setFrozen((BaseProxy) searchCriteria, true);
        }
        final RequestContext requestContext = createRequestContext();
        createCountRequest(requestContext, searchCriteria).fire(new Receiver<Long>() {
            @Override
            public void onSuccess(final Long response) {
                if (view == null) {
                    // This activity is dead
                    return;
                }
                view.asHasData().setRowCount(response.intValue(), true);
                onRangeChanged(view.asHasData(), range, view.getColumnSortList());
            }
        });
    }

    /**
     * Called by the table as it needs data.
     */
    protected void onRangeChanged(final HasData<P> hasData, final Range range, final ColumnSortList columnSortList) {
        final RequestContext requestContext = createRequestContext();
        createSearchRequest(requestContext, searchCriteria, range, columnSortList)
                .with(view.getPaths()).fire(new Receiver<List<P>>() {
                    @Override
                    public void onSuccess(final List<P> results) {
                        if (view == null) {
                            // This activity is dead
                            return;
                        }
                        hasData.setRowData(range.getStart(), results);
                        newHistoryToken(searchCriteria, range.getStart(), range.getLength());
                    }
                });
    }

    protected void newHistoryToken(final S searchCriteria, final int firstResult, final int maxResults) {
        final String historyToken = new EntitySearchPlace.Tokenizer(proxyFactory, requests)
                .getFullHistoryToken(new EntitySearchPlace(currentPlace.getProxyClass(), firstResult, maxResults, searchCriteria));
        History.newItem(historyToken, false);
    }

    @Override
    public void addClicked() {
        placeController.goTo(new EntityProxyPlace(currentPlace.getProxyClass()));
    }

    @Override
    public void showDetails(final Class<? extends EntityProxy> proxyClass, final String entityId) {
        placeController.goTo(new EntityProxyPlace(proxyClass, entityId, EntityProxyPlace.Operation.EDIT));
    }

    @Override
    public void searchClicked() {
        if (searchCriteria instanceof BaseProxy) {
            proxyFactory.setFrozen((BaseProxy) searchCriteria, false);
        }
        searchCriteria = view.getSearchCriteria();
        final Set<ConstraintViolation<S>> violations = validate(searchCriteria);
        view.setConstraintViolations(violations);
        if (violations == null || violations.isEmpty()) {
            loadItems(searchCriteria);
        }
    }

    /**
     * Validates given searchCriteria.
     * 
     * Override if you want to apply validation, example: <code><pre>
     * protected Set<ConstraintViolation<S>> validate(S searchCriteria){
     * 	return getValidator().validate(searchCriteria);
     * }
     * </pre></code>
     * 
     * @param searchCriteria
     * @return
     */
    protected Set<ConstraintViolation<S>> validate(final S searchCriteria) {
        return null;//
    }

    @Override
    public void deleteClicked(final Class<? extends EntityProxy> proxyClass, final String entityId) {
        Window.alert("deleteClicked");
    }

    @Override
    public void cancelClicked() {
        placeController.goTo(new HomePlace());
    }

    @Override
    public void onCancel() {
        onStop();
    }

    @Override
    public void onStop() {
        view.setDelegate(null);
        view = null;
        rangeChangeHandler.removeHandler();
        rangeChangeHandler = null;
    }
}
