/**
 * 
 */
package org.appfuse.webapp.server;

import java.util.Locale;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author ivangsa
 *
 */
public class CustomValidationMessageInterpolator extends ResourceBundleMessageInterpolator {

    private static final String APPLICATION_RESOURCES_NAME = ApplicationResources.class.getName();

    /**
     * @param userResourceBundleLocator
     * @param cacheMessages
     */
    public CustomValidationMessageInterpolator() {
        super(new PlatformResourceBundleLocator(APPLICATION_RESOURCES_NAME));
    }

    @Override
    public String interpolate(String message, Context context) {
        return interpolate(message, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(String message, Context context, Locale locale) {
        return super.interpolate(message, context, locale);
    }

}
