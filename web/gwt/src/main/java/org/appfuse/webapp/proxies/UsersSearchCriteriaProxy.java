/**
 * 
 */
package org.appfuse.webapp.proxies;

import org.appfuse.webapp.server.requests.UsersSearchCriteria;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

/**
 * @author ivangsa
 *
 */
@ProxyFor(UsersSearchCriteria.class)
public interface UsersSearchCriteriaProxy extends ValueProxy {

	interface Factory extends AutoBeanFactory {
		  AutoBean<UsersSearchCriteriaProxy> searchCriteria();
		  AutoBean<UsersSearchCriteriaProxy> searchCriteria(UsersSearchCriteriaProxy toWrap);
	}
	
	String getSearchTerm();

	void setSearchTerm(String searchTerm);
}
