/**
 * 
 */
package org.appfuse.webapp.client.proxies;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

/**
 * @author ivangsa
 *
 */
@ProxyFor(org.appfuse.webapp.server.services.UsersSearchCriteria.class)
public interface UsersSearchCriteriaProxy extends ValueProxy {

	String getSearchTerm();

	void setSearchTerm(String searchTerm);
}
