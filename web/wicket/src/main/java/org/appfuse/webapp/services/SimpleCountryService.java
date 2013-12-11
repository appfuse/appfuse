package org.appfuse.webapp.services;

import java.util.List;
import java.util.Locale;

/**
 * Service providing countries for drop down country list.
 *
 * @author Marcin Zajączkowski, 2013-11-19
 */
public interface SimpleCountryService {

    List<String> getAvailableCountryCodesInLocale(Locale locale);

    String getCountryNameInLocaleByCode(String code, Locale locale);
}
