/**
 * 
 */
package org.appfuse.webapp.client.ui.users.list;

import org.appfuse.webapp.client.application.base.view.ProxyListView;
import org.appfuse.webapp.proxies.UserProxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;

/**
 * @author ivangsa
 *
 */
public interface UsersListView extends ProxyListView<UserProxy> {


	interface SearchDelegate {

		void doneClicked();
		void searchClicked();
	}
	
	void setSearchDelegate(SearchDelegate searchDelegate);
	
}
