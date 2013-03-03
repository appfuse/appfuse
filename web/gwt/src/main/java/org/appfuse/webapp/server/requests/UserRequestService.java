package org.appfuse.webapp.server.requests;

import java.util.List;

import org.appfuse.model.User;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserRequestService {

	/**
	 * 
	 * @return
	 */
	abstract User getCurrentUser();

	/**
	 * 
	 * @return
	 */
	abstract User signUp();

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	abstract User signUp(User user) throws Exception;

	/**
	 * 
	 * @return
	 */
	@PreAuthorize("isAuthenticated()")
	abstract User editProfile();

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("isFullyAuthenticated()")
	abstract User editProfile(User user) throws Exception;

	/**
	 * 
	 * @param userId
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	abstract User getUser(Long userId);

	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	abstract User saveUser(User user) throws Exception;

	/**
	 * 
	 * @param searchCriteria
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	abstract long countUsers(UsersSearchCriteria searchCriteria);

	/**
	 * 
	 * @param searchCriteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	abstract List<User> searchUsers(UsersSearchCriteria searchCriteria, int firstResult, int maxResults);

	/**
	 * 
	 * @param searchCriteria
	 * @param firstResult
	 * @param maxResults
	 * @param sortProperty
	 * @param ascending
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	abstract List<User> searchUsers(UsersSearchCriteria searchCriteria, int firstResult, int maxResults, String sortProperty, boolean ascending);
	
	/**
	 * 
	 * @param user
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	abstract void removeUser(Long userId);

	/**
	 * 
	 * @param username
	 * @return
	 */
	abstract String sendPasswordHint(String username);

	/**
	 * 
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	abstract List<User> getActiveUsers();

	abstract boolean logout();

}