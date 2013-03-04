package org.appfuse.webapp.server.services;

import org.appfuse.webapp.server.services.impl.LookupRequestServiceImpl.LookupConstants;
import org.springframework.security.access.annotation.Secured;

public interface LookupRequestService {

	/**
	 * 
	 * @return
	 */
	LookupConstants getApplicationConstants();

	/**
	 * 
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	LookupConstants reloadOptions();

}