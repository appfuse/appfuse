package org.appfuse.webapp.client.requests;

import java.util.List;

import org.appfuse.webapp.client.proxies.UserProxy;
import org.appfuse.webapp.client.proxies.UsersSearchCriteriaProxy;
import org.appfuse.webapp.server.GwtServiceLocator;
import org.appfuse.webapp.server.services.UserRequestService;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;

@Service(value = UserRequestService.class, locator = GwtServiceLocator.class)
public interface UserRequest extends RequestContext {

    abstract Request<UserProxy> getCurrentUser();

    abstract Request<UserProxy> signUp();

    abstract Request<UserProxy> signUp(UserProxy user);

    abstract Request<UserProxy> editProfile();

    abstract Request<UserProxy> editProfile(UserProxy user);

    abstract Request<UserProxy> getUser(Long userId);

    abstract Request<UserProxy> saveUser(UserProxy user);

    abstract Request<Long> countUsers(UsersSearchCriteriaProxy searchCriteria);

    abstract Request<List<UserProxy>> searchUsers(UsersSearchCriteriaProxy searchCriteria, int firstResult, int maxResults);

    abstract Request<List<UserProxy>> searchUsers(UsersSearchCriteriaProxy searchCriteria, int firstResult, int maxResults, String sortProperty, boolean ascending);

    abstract Request<Void> removeUser(Long userId);

    abstract Request<String> sendPasswordHint(String username);

    abstract Request<String> requestRecoveryToken(String username);

    abstract Request<UserProxy> updatePassword(String username, String token, String currentPassword, String password);

    abstract Request<List<UserProxy>> getActiveUsers();

    abstract Request<Boolean> logout();

}
