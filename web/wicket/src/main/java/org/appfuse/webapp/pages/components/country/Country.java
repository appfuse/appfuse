package org.appfuse.webapp.pages.components.country;

import java.io.Serializable;

/**
 * Represents country as a class used in drop down component.
 *
 * TODO: MZA: It should be moved into common, but in every web frontend it is currently implemented independently in
 * different ways.
 *
 * @author Marcin Zajączkowski, 2013-11-18
 */
@Deprecated //not needed with SimpleCountryDropDownChoice
public class Country implements Serializable {
    private final String locale;
    private final String name;

    public Country(String locale, String name) {
        this.locale = locale;
        this.name = name;
    }

    public String getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }
}
