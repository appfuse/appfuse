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
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:10 $
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
        User user = dao.getUser(username);
        return convertUser(user);
    }
    
    /**
     * Convenience method to convert user -> form so save() method can 
     * use (without calling get).  This is mainly so that transactions
     * will commit.  If I can getUser from saveUser - the transaction
     * doesn't commit until I leave saveUser.  Therefore, everything must
     * happen in the saveUser method.
     * 
     * @param user
     * @return a UserForm object - populated from the user object
     * @throws Exception
     */
    private Object convertUser(User user) throws Exception {
        // convert all role objects to forms
        for (int i = 0; i < user.getRoles().size(); i++) {
            UserRole role = (UserRole) user.getRoles().get(i);
            user.getRoles().set(i, convert(role));
        }
        return convert(user);   
    }

    /**
     * @see org.appfuse.service.UserManager#getUsers(java.lang.Object)
     */
    public List getUsers(Object obj) throws Exception {
        User user = (User) convert(obj);
        List users = dao.getUsers(user);

        // loop through the list and convert all objects to forms
        for (int i = 0; i < users.size(); i++) {
            user = (User) users.get(i);
            users.set(i, convertUser(user));
        }

        return users;
    }

    /**
     * @see org.appfuse.service.UserManager#saveUser(java.lang.Object)
     */
    public Object saveUser(Object obj) throws Exception {
        User user = (User) convert(obj);

        List userRoles = user.getRoles();

        if (userRoles != null) { // removed all roles
            // convert all role objects to forms
            for (int j = 0; j < user.getRoles().size(); j++) {
                Object role = user.getRoles().get(j);
                if (!(role instanceof UserRole)) {
                    role = convert(role);
                }
                user.getRoles().set(j, role);
            }
        }

        user = dao.saveUser(user);

        return convertUser(user);
    }

    /**
     * @see org.appfuse.service.UserManager#removeUser(java.lang.Object)
     */
    public void removeUser(Object obj) throws Exception {
        User user = (User) convert(obj);

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
