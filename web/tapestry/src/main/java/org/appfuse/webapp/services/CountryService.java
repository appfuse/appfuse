package org.appfuse.webapp.services;

import java.util.Map;

/**
 * Provide access to a list of countries based on your locale
 *
 * @author Serge Eby
 */
public interface CountryService {
    Map<String, String> getAvailableCountries();
}
