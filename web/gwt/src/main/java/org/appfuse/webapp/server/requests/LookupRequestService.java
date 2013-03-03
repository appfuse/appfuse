package org.appfuse.webapp.server.requests;

import org.appfuse.webapp.server.requests.LookupRequestServiceImpl.LookupConstants;
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