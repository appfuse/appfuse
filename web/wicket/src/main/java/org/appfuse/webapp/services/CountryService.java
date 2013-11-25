package org.appfuse.webapp.services;

import org.appfuse.webapp.pages.components.country.Country;

import java.util.List;
import java.util.Locale;

/**
 * Service providing countries for drop down country list.
 *
 * @author Marcin Zajączkowski, 2013-11-18
 */
@Deprecated //not needed with SimpleCountryDropDownChoice
public interface CountryService {

    List<Country> getAvailableCountriesInLocale(Locale locale);

    Country getCountryInLocaleByCode(Locale locale, String code);
}
