package org.appfuse.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.model.UserCookie;
import org.appfuse.model.UserRole;
import org.appfuse.persistence.UserDAO;
import org.appfuse.util.RandomGUID;
import org.appfuse.util.StringUtil;

import java.util.List;


/**
 * Implementation of UserManager interface.  This basically transforms POJOs ->
 * Forms and back again.
 * <p/>
 * <p/>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.3 $ $Date: 2004/03/18 20:33:07 $
 */
public class UserManagerImpl extends BaseManager implements UserManager {
    private Log log = LogFactory.getLog(UserManagerImpl.class);
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
    public Object getUser(String username) throws Exception {
        return dao.getUser(username);
    }

    /**
     * @see org.appfuse.service.UserManager#getUsers(java.lang.Object)
     */
    public List getUsers(Object obj) throws Exception {
        return dao.getUsers((User) obj);
    }

    /**
     * @see org.appfuse.service.UserManager#saveUser(java.lang.Object)
     */
    public Object saveUser(Object obj) throws Exception {
        return dao.saveUser((User) obj);
    }

    /**
     * @see org.appfuse.service.UserManager#removeUser(java.lang.Object)
     */
    public void removeUser(Object obj) throws Exception {
        User user = (User) obj;

        if (log.isDebugEnabled()) {
            log.debug("removing user: " + user.getUsername());
        }

        if (user.getRoles() != null) {
            user.getRoles().clear();
        }

        dao.removeUser(user);
    }
    
    /**
     * @see org.appfuse.service.UserManager#checkLoginCookie(java.lang.String)
     */
    public String checkLoginCookie(String value) throws Exception {
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
    public String createLoginCookie(String username) throws Exception {
        UserCookie cookie = new UserCookie();
        cookie.setUsername(username);

        return saveLoginCookie(cookie);
    }

    /**
     * Convenience method to set a unique cookie id and save to database
     * @param cookie
     * @return
     * @throws Exception
     */
    private String saveLoginCookie(UserCookie cookie) throws Exception {
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
