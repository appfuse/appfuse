/**
 * 
 */
package org.appfuse.webapp.requests;

import org.appfuse.webapp.proxies.LookupConstantsProxy;
import org.appfuse.webapp.server.GwtServiceLocator;
import org.appfuse.webapp.server.requests.LookupRequestService;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

/**
 * @author ivangsa
 *
 */
@Service(value = LookupRequestService.class, locator = GwtServiceLocator.class)
public interface LookupRequest extends RequestContext {

	public Request<LookupConstantsProxy> getApplicationConstants();
}
