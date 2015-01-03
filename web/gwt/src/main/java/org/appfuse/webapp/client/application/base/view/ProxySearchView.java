package org.appfuse.webapp.client.application.base.view;

import javax.validation.ConstraintViolation;

import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

/**
 * A view of a list of {@link EntityProxy}s, which declares which properties it
 * is able to display.
 * <p/>
 * It is expected that such views will typically (eventually) be defined largely
 * in ui.xml files which declare the properties of interest, which is why the
 * view is a source of a property set rather than a receiver of one.
 *
 * @param <P>
 *            the type of the records to display
 */
public interface ProxySearchView<P extends EntityProxy, S> extends IsWidget {

    /**
     * Implemented by the owner of a RecordTableView.
     *
     * @param <P>
     *            the type of the records to display
     */
    interface Delegate<P> {

        void addClicked();

        void searchClicked();

        void cancelClicked();

        void showDetails(Class<? extends EntityProxy> proxyClass, String entityId);

        void deleteClicked(Class<? extends EntityProxy> proxyClass, String entityId);
    }

    /**
     * Sets the delegate.
     */
    void setDelegate(Delegate<P> delegate);

    void setSearchCriteria(S searchCriteria);

    S getSearchCriteria();

    boolean setConstraintViolations(Iterable<ConstraintViolation<S>> violations);

    HasData<P> asHasData();

    ColumnSortList getColumnSortList();

    void addColumnSortHandler(Handler clientSideSortHandler);

    /**
     * @return the set of properties this view displays
     */
    String[] getPaths();

    /**
     * 
     * @param pageSize
     */
    void setPageSize(Integer pageSize);
}
