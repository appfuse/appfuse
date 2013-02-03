/**
 * 
 */
package org.appfuse.webapp.client.ui.users.search;

import java.util.List;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractProxySearchActivity;
import org.appfuse.webapp.client.application.base.place.EntityListPlace;
import org.appfuse.webapp.client.application.base.view.ProxySearchView;
import org.appfuse.webapp.client.ui.mainMenu.MainMenuPlace;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;
import org.appfuse.webapp.requests.UserRequest;

import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;

/**
 * @author ivangsa
 *
 */
public class UsersSearchActivity extends AbstractProxySearchActivity<UserProxy, UsersSearchCriteriaProxy> {

	private RequestFactoryEditorDriver<UsersSearchCriteriaProxy,?> editorDriver;
	
	public UsersSearchActivity(EntityListPlace currentPlace, Application application) {
		super(application, UsersSearchCriteriaProxy.class);
		setTitle(i18n.userList_title());
	}


	@Override
	protected ProxySearchView<UserProxy, UsersSearchCriteriaProxy> createView() {
		UsersSearchView view = viewFactory.getView(UsersSearchView.class);
		view.setDelegate(this);
		return view;
	}

	@Override
	protected RequestContext createRequestContext() {
		return requests.userRequest();
	}
	
	@Override
	protected Request<List<UserProxy>> createSearchRequest(RequestContext requestContext, UsersSearchCriteriaProxy searchCriteria, int firstResult, int maxResults) {
		return ((UserRequest) requestContext).searchUsers(searchCriteria,	firstResult, maxResults);
	}

	@Override
	protected Request<Long> createCountRequest(RequestContext requestContext, UsersSearchCriteriaProxy searchCriteria) {
		return ((UserRequest) requestContext).countUsers(searchCriteria);
	}


	@Override
	public void cancelClicked() {
		placeController.goTo(new MainMenuPlace());
	}


	@Override
	public void searchClicked() {
		UsersSearchCriteriaProxy searchCriteria = ((UsersSearchView)view).getSearchCriteria();
		logger.info("searchClicked " + searchCriteria);
		loadItems(searchCriteria);
	}

}
