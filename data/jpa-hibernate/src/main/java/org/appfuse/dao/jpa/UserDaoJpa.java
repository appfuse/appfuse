package org.appfuse.dao.jpa;

import java.util.List;

import javax.persistence.Query;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *   Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *   Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *   Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with 
 *   the new BaseDaoHibernate implementation that uses generics.
*/
public class UserDaoJpa extends GenericDaoJpa<User, Long> implements UserDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoJpa() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        Query q = this.entityManager.createQuery("select u from User u order by upper(u.username)");
        return q.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Query q = this.entityManager.createQuery("select u from User u where username=?");
        q.setParameter(1, username);
        List<User> users = q.getResultList();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return users.get(0);
        }
    }

    /**
     * Save user and flush entityManager
     * @param user the user to save
     * @return the updated user
     */
    public User saveUser(User user) {
        User u = super.save(user);
        entityManager.flush();
        return u;
    }
}
