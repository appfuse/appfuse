package org.appfuse.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.DAOException;
import org.appfuse.dao.UserDAO;
import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.appfuse.service.BaseManager;
import org.appfuse.service.ServiceException;
import org.appfuse.service.UserManager;
import org.appfuse.util.RandomGUID;
import org.appfuse.util.StringUtil;


/**
 * Implementation of UserManager interface.  This basically transforms POJOs ->
 * Forms and back again.</p>
 * 
 * <p>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/08/19 00:10:41 $
 */
public class UserManagerImpl extends BaseManager implements UserManager {
    private UserDAO dao;

    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setUserDAO(UserDAO dao) {
        this.dao = dao;
    }

    /**
     * @see org.appfuse.service.UserManager#getUser(java.lang.String)
     */
    public User getUser(String username) throws ServiceException {
        try {
        	return dao.getUser(username);
        } catch (DAOException d) {
        	throw new ServiceException(d.getMessage(), d);
        }
    }

    /**
     * @see org.appfuse.service.UserManager#getUsers(org.appfuse.model.User)
     */
    public List getUsers(User user) {
        return dao.getUsers(user);
    }

    /**
     * @see org.appfuse.service.UserManager#saveUser(org.appfuse.model.User)
     */
    public User saveUser(User user) throws ServiceException {
        try {
        	return dao.saveUser(user);
        } catch (DAOException d) {
        	throw new ServiceException(d.getMessage(), d);
        }
    }

    /**
     * @see org.appfuse.service.UserManager#removeUser(java.lang.String)
     */
    public void removeUser(String username) throws ServiceException {

        if (log.isDebugEnabled()) {
            log.debug("removing user: " + username);
        }

        try {
        	dao.removeUser(username);
        } catch (DAOException d) {
            throw new ServiceException(d.getMessage(), d);
        }
    }
    
    /**
     * @see org.appfuse.service.UserManager#checkLoginCookie(java.lang.String)
     */
    public String checkLoginCookie(String value) {
        value = StringUtil.decodeString(value);

        String[] values = StringUtils.split(value, "|");

        if (log.isDebugEnabled()) {
            log.debug("looking up cookieId: " + values[1]);
        }

        UserCookie cookie = dao.getUserCookie(values[1]);

        if (cookie != null) {
            if (log.isDebugEnabled()) {
                log.debug("cookieId lookup succeeded, generating new cookieId");
            }

            return saveLoginCookie(cookie);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("cookieId lookup failed, returning null");
            }

            return null;
        }
    }

    /**
     * @see org.appfuse.service.UserManager#createLoginCookie(java.lang.String)
     */
    public String createLoginCookie(String username) {
        UserCookie cookie = new UserCookie();
        cookie.setUsername(username);

        return saveLoginCookie(cookie);
    }

    /**
     * Convenience method to set a unique cookie id and save to database
     * @param cookie
     * @return
     */
    private String saveLoginCookie(UserCookie cookie) {
        cookie.setCookieId(new RandomGUID().toString());
        dao.saveUserCookie(cookie);

        return StringUtil.encodeString(cookie.getUsername() + "|" +
                                       cookie.getCookieId());
    }

    /**
     * @see org.appfuse.service.UserManager#removeLoginCookies(java.lang.String)
     */
    public void removeLoginCookies(String username) {
        dao.removeUserCookies(username);
    }
}
