package org.appfuse.webapp.pages.components.country;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.webapp.services.SimpleCountryService;

import java.util.ArrayList;

/**
 * A components wrapping drop down components with countries.
 *
 * @author Marcin Zajączkowski, 2013-11-18
 */
public class SimpleCountryDropDownChoice extends DropDownChoice<String> {

    @SpringBean(name = "simpleCountryService")
    private SimpleCountryService countryService;

    public SimpleCountryDropDownChoice(String id, PropertyModel<String> country) {
        super(id, country, new ArrayList<String>());
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setChoices(countryService.getAvailableCountryCodesInLocale(getLocale()));
        setChoiceRenderer(new SimpleCountryChoiceRenderer(getLocale()));
    }
}
