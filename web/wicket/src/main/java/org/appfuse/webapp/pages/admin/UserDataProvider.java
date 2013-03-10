package org.appfuse.webapp.pages.admin;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.appfuse.webapp.util.NumberRangeUtil.checkIfLongWithinIntegerRange;
import static org.springframework.util.Assert.notNull;

/**
 * Sortable users data provider.
 *
 * @author Marcin Zajączkowski, 2011-05-22
 */
public class UserDataProvider extends SortableDataProvider<User, String> {

    private static final String NO_SEARCH_FILTER = null;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final UserManager userManager;
    private String searchFilter;

    public UserDataProvider(UserManager userManager) {
        this(userManager, NO_SEARCH_FILTER);
    }

    public UserDataProvider(UserManager userManager, String searchFilter) {
        notNull(userManager);
        this.userManager = userManager;
        this.searchFilter = searchFilter;

        setSort("username", SortOrder.ASCENDING);
    }

    public void setSearchFilter(String searchFilter) {
        this.searchFilter = searchFilter;
    }

    public Iterator<? extends User> iterator(long first, long count) {
        //TODO: MZA: How sorting works in Tapestry/Spring MVC? - also in Java?
        List<User> readUsers = userManager.search(searchFilter);
//            Comparator<User> userComparator = UserComparatorResolver.getComparatorBySoftProperty(
//                    getSort().getProperty(), getSort().isAscending());
//            Collections.sort(readUsers, userComparator);
        Collections.sort(readUsers, getSortableUserDataProviderComparator());

        checkIfLongWithinIntegerRange(first, count, first + count);
        return readUsers.subList((int)first, (int)first + (int)count).iterator();
    }

    private SortableDataProviderComparator<User> getSortableUserDataProviderComparator() {
        return SortableDataProviderComparator.getInstanceForSortParam(getSort());
    }

    public long size() {
        //TODO: MZA: Not very optimal...
        return userManager.search(searchFilter).size();
    }

    public IModel<User> model(final User user) {
        //TODO: MZA: Separate class should be created
        return new LoadableDetachableModel<User>(user) {
            @Override
            protected User load() {
                User loadedUser = userManager.getUser(user.getId().toString());
                //TODO: An ugly hack required to not force user to enter his password on each edition.
                // Will be fixed in APF-1370
                loadedUser.setConfirmPassword(loadedUser.getPassword());
                return loadedUser;
            }
        };
    }
}
