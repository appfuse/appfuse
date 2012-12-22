package org.appfuse.webapp.pages.admin;

import org.appfuse.model.User;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A smarter way to compare elements in UserDataProvider.
 *
 * Not fully implemented. Currently not used.
 *
 * @author Marcin Zajączkowski, 2011-05-22
 */
public class UserComparatorResolver {

    public static Comparator<User> getComparatorBySoftProperty(String property, boolean isAscending) {

        //TODO: Implement resolver or fall back to original solution with a lot of object creation on every compare
        throw new IllegalStateException("Not implemented yet");
    }

    abstract class AbstractUserComparator<T extends Comparable<T>> implements Comparator<User>, Serializable {

        private final boolean ascending;

        protected AbstractUserComparator(boolean ascending) {
            this.ascending = ascending;
        }

        public int compare(User o1, User o2) {
            int result = getPropertyToCompare(o1).compareTo(getPropertyToCompare(o2));
            if (!ascending) {
                result = -result;
            }
            return result;
        }

        protected abstract T getPropertyToCompare(User user);
    }

    class UsernameUserComparator extends AbstractUserComparator<String> {

        protected UsernameUserComparator(boolean ascending) {
            super(ascending);
        }

        @Override
        protected String getPropertyToCompare(User user) {
            return user.getUsername();
        }
    }

    class LastNameUserComparator extends AbstractUserComparator<String> {

        protected LastNameUserComparator(boolean ascending) {
            super(ascending);
        }

        @Override
        protected String getPropertyToCompare(User user) {
            return user.getLastName();
        }
    }

    class EmailUserComparator extends AbstractUserComparator<String> {

        EmailUserComparator(boolean ascending) {
            super(ascending);
        }

        @Override
        protected String getPropertyToCompare(User user) {
            return user.getEmail();
        }
    }

    class EnabledUserComparator extends AbstractUserComparator<Boolean> {

        EnabledUserComparator(boolean ascending) {
            super(ascending);
        }

        @Override
        protected Boolean getPropertyToCompare(User user) {
            return user.isEnabled();
        }
    }
}
