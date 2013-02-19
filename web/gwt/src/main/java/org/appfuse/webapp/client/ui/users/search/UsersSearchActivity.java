/**
 * 
 */
package org.appfuse.webapp.client.ui.users.search;

import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxySearchActivity;
import org.appfuse.webapp.client.application.base.place.EntitySearchPlace;
import org.appfuse.webapp.client.application.base.view.ProxySearchView;
import org.appfuse.webapp.client.application.utils.tables.LocalColumnSortHandler;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;
import org.appfuse.webapp.requests.UserRequest;

import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class UsersSearchActivity extends AbstractProxySearchActivity<UserProxy, UsersSearchCriteriaProxy> {

	private Handler clientSideSortHandler;
	
	public UsersSearchActivity(EntitySearchPlace currentPlace, Application application) {
		super(application, UsersSearchCriteriaProxy.class);
		setTitle(i18n.userList_title());
	}


	@Override
	protected ProxySearchView<UserProxy, UsersSearchCriteriaProxy> createView() {
		final UsersSearchView view = viewFactory.getView(UsersSearchView.class);
		view.setDelegate(this);
        clientSideSortHandler = new LocalColumnSortHandler<UserProxy>(view.getCellTable()) {
			@Override
			public List<UserProxy> getList() {
				return view.getCellTable().getVisibleItems();
			}
        };
        view.getCellTable().addColumnSortHandler(clientSideSortHandler);
		return view;
	}

	@Override
	protected RequestContext createRequestContext() {
		return requests.userRequest();
	}

	@Override
	protected Request<Long> createCountRequest(RequestContext requestContext, UsersSearchCriteriaProxy searchCriteria) {
		return ((UserRequest) requestContext).countUsers(searchCriteria);
	}
	
	@Override
	protected Request<List<UserProxy>> createSearchRequest(RequestContext requestContext, UsersSearchCriteriaProxy searchCriteria, Range range, ColumnSortList columnSortList) {
		return ((UserRequest) requestContext).searchUsers(searchCriteria, range.getStart(), range.getLength());
	}
	
	@Override
	public void onStop() {
		//XXX view.getCellTable().removeColumnSortHandler(clientColumnSortHandler);
		clientSideSortHandler = null;
		super.onStop();
	}
}
