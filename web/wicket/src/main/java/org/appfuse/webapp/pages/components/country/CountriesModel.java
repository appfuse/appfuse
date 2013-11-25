package org.appfuse.webapp.pages.components.country;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.webapp.services.CountryService;

import java.util.List;
import java.util.Locale;

/**
 * Loadable and detachable model using a service as a list of countries to display.
 *
 * @author Marcin Zajączkowski, 2013-11-18
 */
@Deprecated //not needed with SimpleCountryDropDownChoice
public class CountriesModel extends LoadableDetachableModel<List<? extends Country>> {

    @SpringBean
    private CountryService countryService;

    private final Locale locale;

    public CountriesModel(Locale locale) {
        this.locale = locale;
        Injector.get().inject(this);
    }

    @Override
    protected List<? extends Country> load() {
        return countryService.getAvailableCountriesInLocale(locale);
    }
}
