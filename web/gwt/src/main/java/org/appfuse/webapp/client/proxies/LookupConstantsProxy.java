/**
 * 
 */
package org.appfuse.webapp.client.proxies;

import java.util.List;

import org.appfuse.webapp.server.services.LookupRequestService.LookupConstants;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

/**
 * @author ivangsa
 *
 */
@ProxyFor(LookupConstants.class)
public interface LookupConstantsProxy extends ValueProxy {

    List<RoleProxy> getAvailableRoles();

    void setAvailableRoles(List<RoleProxy> availableRoles);

    List<LabelValueProxy> getCountries();

    void setCountries(List<LabelValueProxy> countries);
}
