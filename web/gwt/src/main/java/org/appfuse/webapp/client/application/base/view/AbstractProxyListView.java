package org.appfuse.webapp.client.application.base.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.requestfactory.shared.EntityProxy;

/**
 * Abstract implementation of ProxyListView.
 *
 * @param <P> the type of the proxy
 */
public abstract class AbstractProxyListView<P extends EntityProxy> extends Composite implements ProxyListView<P> {
	private HasData<P> display;
	protected ProxyListView.Delegate<P> delegate;


	@Override
	public AbstractProxyListView<P> asWidget() {
		return this;
	}

	public HasData<P> asHasData() {
		return display;
	}

	public void setDelegate(final Delegate<P> delegate) {
		this.delegate = delegate;
	}

	protected void init(Widget root, HasData<P> display) {
		super.initWidget(root);
		this.display = display;
	}
	
	protected void initWidget(Widget widget) {
		throw new UnsupportedOperationException("AbstractRecordListView must be initialized via init(Widget, HasData<P>) ");
	}
}
