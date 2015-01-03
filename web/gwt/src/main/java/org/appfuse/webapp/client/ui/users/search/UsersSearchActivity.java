/**
 * 
 */
package org.appfuse.webapp.client.ui.users.search;

import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxySearchActivity;
import org.appfuse.webapp.client.application.utils.tables.CustomColumn;
import org.appfuse.webapp.client.application.utils.tables.LocalColumnSortHandler;
import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.proxies.UsersSearchCriteriaProxy;
import org.appfuse.webapp.client.requests.UserRequest;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class UsersSearchActivity extends AbstractProxySearchActivity<UserProxy, UsersSearchCriteriaProxy> {

    private final UsersSearchView view;
    private Handler sortHandler;

    @Inject
    public UsersSearchActivity(final Application application, final UsersSearchView view) {
        super(application, view, UsersSearchCriteriaProxy.class);
        this.view = view;
        setTitle(i18n.userList_title());
    }

    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        view.setDelegate(this);
        // Configure local/remote sorting
        // sortHandler = createLocalColumnSortHandler(view.asHasData());
        sortHandler = new ColumnSortEvent.AsyncHandler(view.asHasData());
        view.addColumnSortHandler(sortHandler);

        super.start(panel, eventBus);
    }

    /**
     * @param hasData
     */
    private Handler createLocalColumnSortHandler(final HasData hasData) {
        return new LocalColumnSortHandler<UserProxy>(hasData) {
            @Override
            public List<UserProxy> getList() {
                return (List<UserProxy>) hasData.getVisibleItems();
            }
        };
    }

    private String getPropertyNameForColumn(final Column column) {
        if (column instanceof CustomColumn) {
            return ((CustomColumn) column).getPropertyName();
        }
        return null;
    }

    @Override
    protected RequestContext createRequestContext() {
        return requests.userRequest();
    }

    @Override
    protected Request<Long> createCountRequest(final RequestContext requestContext, final UsersSearchCriteriaProxy searchCriteria) {
        return ((UserRequest) requestContext).countUsers(searchCriteria);
    }

    @Override
    protected Request<List<UserProxy>> createSearchRequest(
            final RequestContext requestContext, final UsersSearchCriteriaProxy searchCriteria,
            final Range range, final ColumnSortList columnSortList) {

        String sortProperty = null;
        boolean ascending = true;
        if (columnSortList.size() > 0) {
            final Column sortColumn = columnSortList.get(0).getColumn();
            sortProperty = getPropertyNameForColumn(sortColumn);
            ascending = columnSortList.get(0).isAscending();
        }

        return ((UserRequest) requestContext).searchUsers(searchCriteria,
                range.getStart(), range.getLength(),
                sortProperty, ascending);
    }

    @Override
    public void onStop() {
        // XXX view.removeColumnSortHandler(sortHandle);
        sortHandler = null;
        super.onStop();
    }
}
