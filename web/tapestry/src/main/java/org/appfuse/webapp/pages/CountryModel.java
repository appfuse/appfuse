package org.appfuse.webapp.pages;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.appfuse.model.LabelValue;

public class CountryModel implements IPropertySelectionModel {
    private List countries = null;
    
    public CountryModel(Locale locale) {
        this.countries = getCountries(locale);
    }
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
    public List getCountries(Locale locale) {
        final String EMPTY = "";
        final Locale[] available = Locale.getAvailableLocales();

        List<LabelValue> countries = new ArrayList<LabelValue>();
        countries.add(new LabelValue("",""));

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

        Collections.sort(countries, new LabelValueComparator(locale));

        return countries;
    }

    /**
     * Class to compare LabelValues using their labels with
     * locale-sensitive behaviour.
     */
    public class LabelValueComparator implements Comparator {
        private Comparator c;

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
        @SuppressWarnings("unchecked")
        public final int compare(Object o1, Object o2) {
            LabelValue lhs = (LabelValue) o1;
            LabelValue rhs = (LabelValue) o2;

            return c.compare(lhs.getLabel(), rhs.getLabel());
        }
    }
    
    public int getOptionCount() {
        return this.countries.size();
    }

    public Object getOption(int index) {
        //return this.countries.get(index);
        LabelValue country = (LabelValue) this.countries.get(index);
        return country.getValue();
    }

    public String getLabel(int index) {
        LabelValue country = (LabelValue) this.countries.get(index);
        return country.getLabel();
    }

    public String getValue(int index) {
        LabelValue country = (LabelValue) this.countries.get(index);
        return country.getValue();
    }

    public Object translateValue(String value) {
        return value;
    }

}
