/**
 * 
 */
package org.appfuse.webapp.server.locators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.appfuse.model.LabelValue;
import org.appfuse.model.Role;
import org.appfuse.service.RoleManager;
import org.appfuse.webapp.proxies.LookupConstantsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ivangsa
 *
 */
@Component
public class LookupRequestService {

	@Autowired
	private RoleManager roleManager;

	/**
	 * Application wide constants to be sent to the client.
	 * 
	 * @see LookupConstantsProxy
	 */
	public static class LookupConstants {
		
		private List<Role> availableRoles = new ArrayList<Role>();
		private List<LabelValue> countries = new ArrayList<LabelValue>();
		
		public List<Role> getAvailableRoles() {
			return availableRoles;
		}
		public void setAvailableRoles(List<Role> availableRoles) {
			this.availableRoles = availableRoles;
		}
		public List<LabelValue> getCountries() {
			return countries;
		}
		public void setCountries(List<LabelValue> countries) {
			this.countries = countries;
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public LookupConstants getApplicationConstants() {
		LookupConstants applicationConstants = new LookupConstants();
		
		applicationConstants.setAvailableRoles(roleManager.getAll());
		applicationConstants.setCountries(Arrays.asList(new LabelValue("ES", "ES")));
		
		return applicationConstants;
	}
}