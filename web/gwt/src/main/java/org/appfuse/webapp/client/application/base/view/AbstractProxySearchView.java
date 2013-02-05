package org.appfuse.webapp.client.application.base.view;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

/**
 * Abstract implementation of ProxyListView.
 *
 * @param <P> the type of the proxy
 */
public abstract class AbstractProxySearchView<P extends EntityProxy, S extends BaseProxy> extends Composite implements ProxySearchView<P, S> {

	protected ProxySearchView.Delegate<P> delegate;

	
	@UiField(provided=true)
	public Integer pageSize = 25;

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
		getCellTable().setVisibleRange(getCellTable().getVisibleRange().getStart(), pageSize);
	}
}
