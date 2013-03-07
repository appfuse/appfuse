/**
 * 
 */
package org.appfuse.webapp.requests;

import org.appfuse.webapp.proxies.LookupConstantsProxy;
import org.appfuse.webapp.server.GwtServiceLocator;
import org.appfuse.webapp.server.services.LookupRequestService;
import org.appfuse.webapp.server.services.impl.LookupRequestServiceImpl;

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

	public Request<LookupConstantsProxy> reloadOptions();
}
