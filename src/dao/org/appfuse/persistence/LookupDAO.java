package org.appfuse.persistence;

import java.util.List;


/**
 * Lookup Data Access Object (DAO) interface.  This is used to lookup values in
 * the database (i.e. for drop-downs).
 *
 * <p>
 * <a href="LookupDAO.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:05 $
 */
public interface LookupDAO extends DAO {
    //~ Methods ================================================================

    public List getRoles() throws DAOException;
}
