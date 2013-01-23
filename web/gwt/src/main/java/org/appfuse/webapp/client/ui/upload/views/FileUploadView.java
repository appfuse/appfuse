package org.appfuse.webapp.client.ui.upload.views;

import org.appfuse.webapp.client.ui.upload.FileUploadBean;

import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteHandler;
import com.github.gwtbootstrap.client.ui.Form.SubmitHandler;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface FileUploadView extends IsWidget {

	public interface Delegate extends SubmitHandler, SubmitCompleteHandler {
		
	}
	
	void setDelegate(Delegate delegate);
	void edit(FileUploadBean fileUpload);
	EditorDriver<FileUploadBean> getEditorDriver();
}