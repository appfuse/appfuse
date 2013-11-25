package org.appfuse.webapp.services;

import org.appfuse.webapp.pages.components.country.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CountryService implementation based on Locale.getAvailableLocales() from JDK.
 *
 * Based on CountryTag implementation by Matt Raible.
 *
 * TODO: MZA: Add caching with @Cacheable annotation from Spring
 *
 * @author Marcin Zajączkowski, 2013-11-18
 */
@Deprecated //not needed with SimpleCountryDropDownChoice
@Service("countryService")
public class JavaLocaleCountryService implements CountryService {

    private final static Logger log = LoggerFactory.getLogger(JavaLocaleCountryService.class);

    private CountryComparator countryComparator = new CountryComparator();

    public List<Country> getAvailableCountriesInLocale(Locale locale) {
        return new ArrayList<>(createSortedSet(locale));
    }

    public Country getCountryInLocaleByCode(Locale locale, String code) {
        for (Country country : getAvailableCountriesInLocale(locale)) {
            if (code.equals(country.getLocale())) {
                return country;
            }
        }
        return null;
    }

    private Set<Country> createSortedSet(Locale currentLocale) {
        final String EMPTY = "";
        Locale[] availableLocales = Locale.getAvailableLocales();
        Set<Country> countrySet = new TreeSet<>(countryComparator);

        for (Locale locale : availableLocales) {
            String name = locale.getDisplayCountry(currentLocale);
            String iso = locale.getCountry();
            if (!EMPTY.equals(name) && !EMPTY.equals(iso)) {
                countrySet.add(new Country(iso, name));
            }
        }
        log.debug("Number of countries added: " + countrySet.size());

        return countrySet;
    }

    private static class CountryComparator implements Comparator<Country> {
        @Override
        public int compare(Country country1, Country country2) {
            return country1.getName().compareToIgnoreCase(country2.getName());
        }
    }
}
