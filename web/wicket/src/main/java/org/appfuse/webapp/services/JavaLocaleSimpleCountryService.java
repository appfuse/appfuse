package org.appfuse.webapp.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CountryService implementation (for simplified Wicket mechanism) based on Locale.getAvailableLocales() from JDK.
 *
 * Manual map caching could be a premature optimisation. Maybe it is worth to use @Cacheable from Spring or
 * some other mechanism?
 *
 * TODO: Add sorting by the country name instead of code and rewrite it using data from http://cldr.unicode.org/
 *       (and maybe https://wicket.apache.org/guide/guide/chapter13.html#chapter13_4 for Wicket)
 *
 * @author Marcin Zajączkowski, 2013-11-19
 */
@Service("simpleCountryService")
public class JavaLocaleSimpleCountryService implements SimpleCountryService {

    private final static Logger log = LoggerFactory.getLogger(JavaLocaleSimpleCountryService.class);

    private final static Map<String, Map<String, String>> mmm = new HashMap<>(Locale.getAvailableLocales().length);

    @Override
    public List<String> getAvailableCountryCodesInLocale(Locale locale) {
        return new ArrayList<>(getAvailableCountryMapInLocale(locale).keySet());
    }

    private Map<String, String> getAvailableCountryMapInLocale(Locale locale) {
        String languageTag = locale.getLanguage();
        if (mmm.get(languageTag) == null) {
            mmm.put(languageTag, Collections.unmodifiableMap(createSortedSet(locale)));
        }
        return mmm.get(languageTag);
    }

    @Override
    public String getCountryNameInLocaleByCode(String code, Locale locale) {
        for (Map.Entry<String, String> entry : getAvailableCountryMapInLocale(locale).entrySet()) {
            if (code.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private Map<String, String> createSortedSet(Locale requestedLocale) {
        final String EMPTY = "";
        Locale[] availableLocales = Locale.getAvailableLocales();
        Map<String, String> countryMap = new TreeMap<>();

        for (Locale locale : availableLocales) {
            String name = locale.getDisplayCountry(requestedLocale);
            String iso = locale.getCountry();
            if (!EMPTY.equals(name) && !EMPTY.equals(iso)) {
                countryMap.put(iso, name);
            }
        }
        log.debug("Number of countries added: " + countryMap.size());

        return countryMap;
    }
}
