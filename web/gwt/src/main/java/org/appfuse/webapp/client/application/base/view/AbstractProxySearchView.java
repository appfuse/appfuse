package org.appfuse.webapp.client.application.base.view;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.requestfactory.shared.BaseProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

/**
 * Abstract implementation of ProxyListView.
 *
 * @param <P> the type of the proxy
 */
public abstract class AbstractProxySearchView<P extends EntityProxy, S extends BaseProxy> extends Composite implements ProxySearchView<P, S> {
	private HasData<P> display;
	protected ProxySearchView.Delegate<P> delegate;

	
	@UiField(provided=true)
	public Integer pageSize = 25;

	@Override
	public AbstractProxySearchView<P, S> asWidget() {
		return this;
	}

	public HasData<P> asHasData() {
		return display;
	}

	@Override
	public void setDelegate(final Delegate<P> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void setPageSize(Integer pageSize) {
		display.setVisibleRange(display.getVisibleRange().getStart(), pageSize);
	}

	protected void init(Widget root, HasData<P> display) {
		super.initWidget(root);
		this.display = display;
	}
	
	protected void initWidget(Widget widget) {
		throw new UnsupportedOperationException("AbstractRecordListView must be initialized via init(Widget, HasData<P>) ");
	}
}
