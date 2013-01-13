/**
 * 
 */
package org.appfuse.webapp.client.ui.users.list;

import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyListActivity;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.view.ProxyListView;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;

import com.google.gwt.view.client.Range;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;

/**
 * @author ivangsa
 *
 */
public class UsersListActivity extends AbstractProxyListActivity<UserProxy> {

	public UsersListActivity(EntityListPlace currentPlace, Application application) {
		super(currentPlace, application);
	}


	@Override
	protected ProxyListView<UserProxy> createView() {
		return viewFactory.getView(UsersListView.class);
	}

	@Override
	protected Request<List<UserProxy>> createRangeRequest(Range range) {
		return requests.userRequest()
			.searchUsers((UsersSearchCriteriaProxy) currentPlace.getSearchCriteria(), 
					range.getStart(), range.getLength());
	}

	@Override
	protected void fireCountRequest(Receiver<Long> callback) {
		requests.userRequest()
			.countUsers((UsersSearchCriteriaProxy) currentPlace.getSearchCriteria())
			.fire(callback);
		
	}

}
