package org.appfuse.webapp.pages.admin;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

import static org.springframework.util.Assert.notNull;

/**
 * Sortable users data provider.
 *
 * @author Marcin Zajączkowski, 2011-05-22
 */
public class UserDataProvider extends SortableDataProvider<User> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final UserManager userManager;
    private SortableDataProviderComparator comparator = new SortableDataProviderComparator();

    public UserDataProvider(UserManager userManager) {
        notNull(userManager);
        this.userManager = userManager;

        setSort("username", true);
    }

    private User createUser(String username, String lastName, String email, boolean isEnabled) {
        User user = new User(username);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEnabled(isEnabled);
        return user;
    }

    public Iterator<? extends User> iterator(int first, int count) {
        //TODO: MZA: How sorting works in Tapestry/Spring MVC? - also in Java?
        List<User> readUsers = userManager.getUsers();
//            Comparator<User> userComparator = UserComparatorResolver.getComparatorBySoftProperty(
//                    getSort().getProperty(), getSort().isAscending());
//            Collections.sort(readUsers, userComparator);
        Collections.sort(readUsers, comparator);
        return readUsers.subList(first, first + count).iterator();
    }

    public int size() {
        //TODO: MZA: Not very optimal...
        return userManager.getUsers().size();
    }

    public IModel<User> model(final User user) {
        //TODO: MZA: Separate class should be created
        return new LoadableDetachableModel<User>(user) {
            @Override
            protected User load() {
                return userManager.getUser(user.getId().toString());
            }
        };
    }

    //TODO: MZA: Isn't it an overkill (PropertyModel is created for every comparison)
    // AbstractUserComparator as an alternative
    @SuppressWarnings("unchecked")
    class SortableDataProviderComparator implements Comparator<User>, Serializable {

        public int compare(User o1, User o2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, getSort().getProperty());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty());

            //Compares comparable object from model1 with another from model2 - both has the same type - it's
            //the same property
            int result = (model1.getObject()).compareTo(model2.getObject());

            if (!getSort().isAscending()) {
                result = -result;
            }
            return result;
        }
    }
}
