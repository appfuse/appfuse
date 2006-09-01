package org.appfuse.dao;

import org.appfuse.model.Role;

import java.util.List;


/**
 * Lookup Data Access Object (Dao) interface.  This is used to lookup values in
 * the database (i.e. for drop-downs).
 *
 * <p>
 * <a href="LookupDao.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface LookupDao extends Dao {
    //~ Methods ================================================================

    public List<Role> getRoles();
}
