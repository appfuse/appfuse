/**
 * 
 */
package org.appfuse.webapp.client.ui.users.list;

import org.appfuse.webapp.client.application.base.view.ProxyListView;
import org.appfuse.webapp.proxies.UserProxy;
import org.appfuse.webapp.proxies.UsersSearchCriteriaProxy;

import com.google.gwt.editor.client.EditorDriver;

/**
 * @author ivangsa
 *
 */
public interface UsersListView extends ProxyListView<UserProxy> {


	interface SearchDelegate extends ProxyListView.Delegate<UserProxy>{

		void doneClicked();
		void searchClicked();
	}
	
	void setSearchDelegate(SearchDelegate searchDelegate);

	UsersSearchCriteriaProxy getSearchCriteria();	
	void setSearchCriteria(UsersSearchCriteriaProxy searchCriteria);
}
