package org.appfuse.webapp.action;

import org.appfuse.model.LabelValue;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CountryModel {
    private Map<String, String> availableCountries;

    /**
     * Build a List of LabelValues for all the available countries. Uses
     * the two letter uppercase ISO name of the country as the value and the
     * localized country name as the label.
     *
     * @param locale The Locale used to localize the country names.
     *
     * @return List of LabelValues for all available countries.
     */
    @SuppressWarnings("unchecked")
    public Map<String, String> getCountries(Locale locale) {
        if (availableCountries == null) {
            final String EMPTY = "";
            final Locale[] available = Locale.getAvailableLocales();
    
            List<LabelValue> countries = new ArrayList<LabelValue>();
            countries.add(new LabelValue("",""));

            for (Locale anAvailable : available) {
                final String iso = anAvailable.getCountry();
                final String name = anAvailable.getDisplayCountry(locale);

                if (!EMPTY.equals(iso) && !EMPTY.equals(name)) {
                    LabelValue country = new LabelValue(name, iso);

                    if (!countries.contains(country)) {
                        countries.add(new LabelValue(name, iso));
                    }
                }
            }

            Collections.sort(countries, new LabelValueComparator(locale));
            
            Map<String, String> options = new LinkedHashMap<String, String>();
            // loop through and convert list to a JSF-Friendly Map for a <select>
            for (Object country : countries) {
                LabelValue option = (LabelValue) country;
                if (!options.containsValue(option.getValue())) {
                    options.put(option.getLabel(), option.getValue());
                }
            }
            this.availableCountries = options;
        }

        return availableCountries;
    }

    /**
     * Class to compare LabelValues using their labels with
     * locale-sensitive behaviour.
     */
    public class LabelValueComparator implements Comparator {
        private Comparator<Object> c;

        /**
         * Creates a new LabelValueComparator object.
         *
         * @param locale The Locale used for localized String comparison.
         */
        public LabelValueComparator(Locale locale) {
            c = Collator.getInstance(locale);
        }

        /**
         * Compares the localized labels of two LabelValues.
         *
         * @param o1 The first LabelValue to compare.
         * @param o2 The second LabelValue to compare.
         *
         * @return The value returned by comparing the localized labels.
         */
        public final int compare(Object o1, Object o2) {
            LabelValue lhs = (LabelValue) o1;
            LabelValue rhs = (LabelValue) o2;

            return c.compare(lhs.getLabel(), rhs.getLabel());
        }
    }
}
