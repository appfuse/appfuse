package org.appfuse.webapp.services.impl;

import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.appfuse.webapp.services.CountryService;
import org.slf4j.Logger;

import java.text.Collator;
import java.util.*;

/**
 * Implementation of CountryService
 *
 * @author Serge Eby
 */
public class CountryServiceImpl implements CountryService {
    private final Logger logger;
    private final ThreadLocale threadLocale;
    private final Map<String, String> sortedCountries;

    public CountryServiceImpl(Logger logger, ThreadLocale threadLocale) {
        this.logger = logger;
        this.threadLocale = threadLocale;

        Map<String, String> countries = initialize();
        sortedCountries = new TreeMap<String, String>
                (new CountryComparator(countries, this.threadLocale.getLocale()));
        sortedCountries.putAll(countries);
    }

    public Map<String, String> getAvailableCountries() {
        return sortedCountries;
    }

    private Map<String, String> initialize() {
        Map<String, String> countries = new HashMap<String, String>();
        Locale currentLocale = threadLocale.getLocale();
        final String EMPTY = "";
        Locale[] availableLocales = Locale.getAvailableLocales();

        for (Locale locale : availableLocales) {
            String name = locale.getDisplayCountry(currentLocale);
            String iso = locale.getCountry();
            if (!EMPTY.equals(name) && !EMPTY.equals(iso)) {
                countries.put(iso, name);
            }
        }
        logger.debug("Number of countries added: " + countries.size());

        return countries;

    }

    /**
     * Class to compare LabelValues using their labels with locale-sensitive
     * behaviour.
     */
    private class CountryComparator implements Comparator<String> {
        private Collator c;
        private Map<String, String> unsortedMap;

        /**
         * Creates a new CountryComparator object.
         *
         * @param map    of country and locale
         * @param locale The Locale used for localized String comparison.
         */
        public CountryComparator(Map<String, String> map, Locale locale) {
            unsortedMap = map;
            c = Collator.getInstance(locale);
        }

        /**
         * Compares the localized labels of two LabelValues.
         *
         * @param rhs The first String to compare.
         * @param lhs The second String to compare.
         * @return The value returned by comparing the localized labels.
         */

        public final int compare(String lhs, String rhs) {
            String lvalue = unsortedMap.get(lhs);
            String rvalue = unsortedMap.get(rhs);
            return c.compare(lvalue, rvalue);
        }
    }
}
