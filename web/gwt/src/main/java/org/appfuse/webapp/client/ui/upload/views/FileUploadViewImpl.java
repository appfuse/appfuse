/**
 * 
 */
package org.appfuse.webapp.client.ui.upload.views;

import org.appfuse.webapp.client.ui.upload.FileUploadBean;

import com.github.gwtbootstrap.client.ui.FileUpload;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
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
public class FileUploadViewImpl extends Composite implements FileUploadView, Editor<FileUploadBean> {

	interface Binder extends UiBinder<Widget, FileUploadViewImpl> {}
	private static final Binder BINDER = GWT.create(Binder.class);

	interface Driver extends SimpleBeanEditorDriver<FileUploadBean, FileUploadViewImpl> { }	
	private Driver driver = GWT.create(Driver.class);	
	

	@UiField Form form;
	@UiField TextBox name;
	@UiField FileUpload file;
	
	/**
	 * 
	 */
	public FileUploadViewImpl() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		driver.initialize(this);
	}
	
	@Override
	public void setDelegate(Delegate delegate) {
		form.addSubmitHandler(delegate);
		form.addSubmitCompleteHandler(delegate);
	}
	
	@Override
	public void edit(FileUploadBean object) {
		driver.edit(object);
	}
	
	@Override
	public EditorDriver<FileUploadBean> getEditorDriver() {
		return driver;
	}
}
