/**
 * 
 */
package org.appfuse.webapp.client.ui.upload.views;

import org.appfuse.webapp.client.ui.upload.FileUploadBean;
import org.appfuse.webapp.client.ui.upload.UploadedFileBean;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class UploadedFileViewImpl extends Composite implements UploadedFileView, Editor<UploadedFileBean> {

	interface Binder extends UiBinder<Widget, UploadedFileViewImpl> {}
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Driver extends SimpleBeanEditorDriver<UploadedFileBean, UploadedFileViewImpl> { }	
	private Driver driver = GWT.create(Driver.class);		
	
	private UploadedFileView.Delegate delegate;
	
	@UiField Label name;
	@UiField Label fileName;
	@UiField Label contentType;
	@UiField Label size;
	@UiField Label location;
	@UiField Label link;
	
	/**
	 * 
	 */
	public UploadedFileViewImpl() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		driver.initialize(this);
	}
	
	@Override
	public void setDelegate(UploadedFileView.Delegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void edit(UploadedFileBean object) {
		driver.edit(object);
	}

	@Override
	public EditorDriver<UploadedFileBean> getEditorDriver() {
		return driver;
	}
	
}
