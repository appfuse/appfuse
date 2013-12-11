package org.appfuse.webapp.pages.components.country;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.appfuse.webapp.services.SimpleCountryService;

import java.util.Locale;

/**
 * A choice renderer implementation for countries.
 *
 * @author Marcin Zajączkowski, 2013-11-19
 */
public class SimpleCountryChoiceRenderer implements IChoiceRenderer<String> {

    private final Locale locale;

    @SpringBean(name = "simpleCountryService")
    private SimpleCountryService countryService;

    public SimpleCountryChoiceRenderer(Locale locale) {
        this.locale = locale;
        Injector.get().inject(this);
    }

    @Override
    public Object getDisplayValue(String code) {
        return countryService.getCountryNameInLocaleByCode(code, locale);
    }

    @Override
    public String getIdValue(String object, int index) {
        return object;
    }
}
