package org.appfuse.webapp.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

class DesktopNavigationBar extends Composite {
	interface Binder extends UiBinder<Widget, DesktopNavigationBar> {	}
	private static final Binder BINDER = GWT.create(Binder.class);
	
	/**
	 * 
	 */
	public DesktopNavigationBar() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	
}