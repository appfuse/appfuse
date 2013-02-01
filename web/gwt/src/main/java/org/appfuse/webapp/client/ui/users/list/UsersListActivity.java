/**
 * 
 */
package org.appfuse.webapp.client.ui.users.list;

import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxyListActivity;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.view.ProxyListView;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;
import org.appfuse.webapp.requests.UserRequest;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.impl.AbstractRequestFactory;
import com.google.web.bindery.requestfactory.shared.impl.Constants;
import com.google.web.bindery.requestfactory.shared.impl.SimpleProxyId;

/**
 * @author ivangsa
 *
 */
public class UsersListActivity extends AbstractProxyListActivity<UserProxy> implements UsersListView.SearchDelegate {
	UsersSearchCriteriaProxy.Factory autoBeanFactory = GWT.create(UsersSearchCriteriaProxy.Factory.class);

	public UsersListActivity(EntityListPlace currentPlace, Application application) {
		super(currentPlace, application);
		setTitle(i18n.userList_title());
	}


	@Override
	protected ProxyListView<UserProxy> createView() {
		UsersListView view = viewFactory.getView(UsersListView.class);
		view.setDelegate(this);
		view.setSearchDelegate(this);
		UsersSearchCriteriaProxy searchCriteria = (UsersSearchCriteriaProxy) currentPlace.getSearchCriteria();
		if(searchCriteria == null) {
			AutoBean<UsersSearchCriteriaProxy> autoBean = autoBeanFactory.searchCriteria();
			SimpleProxyId<UsersSearchCriteriaProxy> id =((AbstractRequestFactory)requests).allocateId(UsersSearchCriteriaProxy.class);
			autoBean.setTag(Constants.STABLE_ID, id);
			searchCriteria = autoBean.as();
		}
		view.setSearchCriteria(searchCriteria);
		return view;
	}

	@Override
	protected Request<List<UserProxy>> createRangeRequest(Range range) {
		UserRequest userRequest = requests.userRequest();
		if(currentPlace.getSearchCriteria() != null) {
			userRequest.edit(currentPlace.getSearchCriteria());
		}
		return userRequest.searchUsers((UsersSearchCriteriaProxy) currentPlace.getSearchCriteria(), 
					range.getStart(), range.getLength());
	}

	@Override
	protected void fireCountRequest(Receiver<Long> callback) {
		UserRequest userRequest = requests.userRequest();
		if(currentPlace.getSearchCriteria() != null) {
			userRequest.edit(currentPlace.getSearchCriteria());
		}
		userRequest.countUsers((UsersSearchCriteriaProxy) currentPlace.getSearchCriteria())
			.fire(callback);
	}


	@Override
	public void doneClicked() {
		placeController.goTo(new MainMenuPlace());
	}


	@Override
	public void searchClicked() {
		UsersSearchCriteriaProxy searchCriteria = ((UsersListView)view).getSearchCriteria();
//		AutoBean<UsersSearchCriteriaProxy> autoBean = autoBeanFactory.searchCriteria(searchCriteria); 
//		Object id = autoBean.getTag(Constants.STABLE_ID);
//		autoBean = AutoBeanCodex.decode(autoBeanFactory, UsersSearchCriteriaProxy.class, 
//						AutoBeanCodex.encode(autoBean));
//		if(true) {
//			//id =((AbstractRequestFactory)requests).allocateId(UsersSearchCriteriaProxy.class);
//		}
//		autoBean.setTag(Constants.STABLE_ID, id);
//		searchCriteria = autoBean.as();
		currentPlace.setSearchCriteria(searchCriteria);
		//History.newItem(new EntityListPlace.Tokenizer().getFullHistoryToken(currentPlace), false);
		placeController.goTo(currentPlace);
	}
}
