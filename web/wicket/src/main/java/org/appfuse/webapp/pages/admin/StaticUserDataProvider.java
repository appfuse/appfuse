package org.appfuse.webapp.pages.admin;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.appfuse.model.User;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.appfuse.webapp.util.NumberRangeUtil.checkIfLongWithinIntegerRange;

public class StaticUserDataProvider extends SortableDataProvider<User, String> {

    private final List<User> users;

    public StaticUserDataProvider(List<User> users) {
        this.users = users;
        setSort("username", SortOrder.ASCENDING);
    }

    @Override
    public Iterator<? extends User> iterator(long first, long count) {
        Collections.sort(users, getSortableUserDataProviderComparator());

        checkIfLongWithinIntegerRange(first, count, first + count);
        return users.subList((int)first, (int)first + (int)count).iterator();
    }

    private SortableDataProviderComparator<User> getSortableUserDataProviderComparator() {
        return SortableDataProviderComparator.getInstanceForSortParam(getSort());
    }

    @Override
    public long size() {
        return users.size();
    }

    @Override
    public IModel<User> model(final User user) {
        //TODO: MZA: Verification needed
        return new LoadableDetachableModel<User>(user) {
            @Override
            protected User load() {
                return users.get(users.indexOf(user));
            }
        };
    }
}
