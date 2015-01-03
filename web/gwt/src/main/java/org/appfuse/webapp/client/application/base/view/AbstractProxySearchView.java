package org.appfuse.webapp.client.application.base.view;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.appfuse.webapp.client.application.ApplicationResources;

import com.github.gwtbootstrap.client.ui.DataGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

/**
 * Abstract implementation of ProxyListView.
 *
 * @param <P>
 *            the type of the proxy
 */
public abstract class AbstractProxySearchView<P extends EntityProxy, S> extends Composite implements ProxySearchView<P, S> {

    public @UiField(provided = true) ApplicationResources i18n = GWT.create(ApplicationResources.class);
    protected ProxySearchView.Delegate<P> delegate;

    @UiField(provided = true)
    public Integer pageSize = 25;

    @UiField
    public CellTable<P> table;

    public Set<String> paths = new HashSet<String>();

    protected abstract SimpleBeanEditorDriver<S, ?> getEditorDriver();

    @Override
    public S getSearchCriteria() {
        return getEditorDriver() != null ? getEditorDriver().flush() : null;
    }

    @Override
    public void setSearchCriteria(S searchCriteria) {
        if (getEditorDriver() != null) {
            getEditorDriver().edit(searchCriteria);
        }
    }

    @Override
    public boolean setConstraintViolations(Iterable<ConstraintViolation<S>> violations) {
        return getEditorDriver() != null ?
                getEditorDriver().setConstraintViolations((Set) violations) : false;
    }

    @Override
    public HasData<P> asHasData() {
        return table;
    }

    @Override
    public ColumnSortList getColumnSortList() {
        return table.getColumnSortList();
    }

    @Override
    public void addColumnSortHandler(Handler clientSideSortHandler) {
        table.addColumnSortHandler(clientSideSortHandler);
    }

    @Override
    public String[] getPaths() {
        return paths.toArray(new String[paths.size()]);
    }

    @Override
    public AbstractProxySearchView<P, S> asWidget() {
        return this;
    }

    @Override
    public void setDelegate(final Delegate<P> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setPageSize(Integer pageSize) {
        asHasData().setVisibleRange(asHasData().getVisibleRange().getStart(), pageSize);
    }

}
