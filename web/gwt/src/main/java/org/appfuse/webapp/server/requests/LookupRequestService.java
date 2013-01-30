/**
 * 
 */
package org.appfuse.webapp.server.requests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.appfuse.model.LabelValue;
import org.appfuse.model.Role;
import org.appfuse.service.RoleManager;
import org.appfuse.webapp.proxies.LookupConstantsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
		final Locale locale = LocaleContextHolder.getLocale();
		LookupConstants applicationConstants = new LookupConstants();
		
		applicationConstants.setAvailableRoles(roleManager.getAll());
		applicationConstants.setCountries(buildCountryList(locale));
		
		return applicationConstants;
	}
	
    protected List<LabelValue> buildCountryList(final Locale locale) {
        final String EMPTY = "";
        final Locale[] available = Locale.getAvailableLocales();

        List<LabelValue> countries = new ArrayList<LabelValue>();

        for (int i = 0; i < available.length; i++) {
            final String iso = available[i].getCountry();
            final String name = available[i].getDisplayCountry(locale);

            if (!EMPTY.equals(iso) && !EMPTY.equals(name)) {
                LabelValue country = new LabelValue(name, iso);

                if (!countries.contains(country)) {
                    countries.add(new LabelValue(name, iso));
                }
            }
        }

        Collections.sort(countries, new Comparator<LabelValue>() {
        	@Override
        	public int compare(LabelValue arg0, LabelValue arg1) {
        		return arg0.getLabel().compareTo(arg1.getLabel());			
        	
        	}
        });

        return countries;
    }	
}