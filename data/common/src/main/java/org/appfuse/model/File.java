package org.appfuse.model;

/**
 * Class description goes here ...
 *
 * @author <a href="mailto:david@citechnical.com">David L. Whitehurst</a>
 *         created: 4/23/15
 *         time: 12:10 AM
 * @version CHANGEME
 */

public class File extends BaseObject {
    // @Todo members?

    /**
     * Returns a multi-line String with key=value pairs.
     *
     * @return a String representation of this class.
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Compares object equality. When using Hibernate, the primary key should
     * not be a part of this comparison.
     *
     * @param o object to compare to
     * @return true/false based on equality tests
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }

    /**
     * When you override equals, you should override hashCode. See "Why are
     * equals() and hashCode() importation" for more information:
     * http://www.hibernate.org/109.html
     *
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
