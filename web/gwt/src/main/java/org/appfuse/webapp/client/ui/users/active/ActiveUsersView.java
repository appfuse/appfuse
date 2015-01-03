/**
 * 
 */
package org.appfuse.webapp.client.ui.users.active;

import org.appfuse.webapp.client.proxies.UserProxy;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author ivangsa
 *
 */
public interface ActiveUsersView extends IsWidget {

    interface Delegate {
        void cancelClicked();
    }

    /**
     * Sets the delegate.
     */
    void setDelegate(Delegate delegate);

    /**
     * 
     * @return
     */
    CellTable<UserProxy> getCellTable();

    /**
     * @return the set of properties this view displays
     */
    String[] getPaths();

}
