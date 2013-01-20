/**
 * 
 */
package org.appfuse.webapp.server;

import org.appfuse.webapp.client.application.ApplicationResources;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

/**
 * @author ivangsa
 *
 */
public class CustomValidationMessageInterpolator extends ResourceBundleMessageInterpolator {

	private static final String APPLICATION_RESOURCES_NAME = ApplicationResources.class.getName() + ".kk";
	/**
	 * @param userResourceBundleLocator
	 * @param cacheMessages
	 */
	public CustomValidationMessageInterpolator() {
		super(new PlatformResourceBundleLocator(APPLICATION_RESOURCES_NAME));
	}

	
	
}
