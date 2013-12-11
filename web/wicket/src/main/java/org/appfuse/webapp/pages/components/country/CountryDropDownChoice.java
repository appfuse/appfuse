package org.appfuse.webapp.pages.components.country;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;

import java.util.Locale;

/**
 * A components wrapping drop down components with countries.
 *
 * @author Marcin Zajączkowski, 2013-11-18
 */
@Deprecated //use SimpleCountryDropDownChoice
public class CountryDropDownChoice extends DropDownChoice<Country> {

    public CountryDropDownChoice(String id, PropertyModel<String> country, Locale locale) {
        super(id, new EmbeddedCountryModel(country, locale), new CountriesModel(locale), new ChoiceRenderer<>("name", "locale"));
    }
}
