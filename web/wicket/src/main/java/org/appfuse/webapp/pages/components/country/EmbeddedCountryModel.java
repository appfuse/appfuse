package org.appfuse.webapp.pages.components.country;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.webapp.services.CountryService;

import java.util.Locale;

/**
 * Embedded country model which converts Country representation (Wicket) into String representation (domain).
 *
 * @author Marcin Zajączkowski, 2013-11-18
 */
@Deprecated //not needed with SimpleCountryDropDownChoice
public class EmbeddedCountryModel implements IModel<Country> {
    private final IModel<String> nestedModel;
    private final Locale locale;

    @SpringBean
    private CountryService countryService;

    public EmbeddedCountryModel(IModel<String> nestedModel, Locale locale) {
        this.nestedModel = nestedModel;
        this.locale = locale;
        Injector.get().inject(this);
    }

    @Override
    public Country getObject() {
        if (nestedModel.getObject() != null) {
            return countryService.getCountryInLocaleByCode(locale, nestedModel.getObject());
        } else {
            return null;
        }
    }

    @Override
    public void setObject(Country object) {
        if (object != null) {
            nestedModel.setObject(object.getLocale());
        } else {
            nestedModel.setObject(null);
        }
    }

    @Override
    public void detach() {
        nestedModel.detach();
    }
}
