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
 * @version $Revision: 1.4 $ $Date: 2004/05/25 06:27:18 $
 */
public interface LookupDAO extends DAO {
    //~ Methods ================================================================

    public List getRoles();
}
