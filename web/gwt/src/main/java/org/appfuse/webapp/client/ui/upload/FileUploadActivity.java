/**
 * 
 */
package org.appfuse.webapp.client.ui.upload;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;
import org.appfuse.webapp.client.ui.upload.views.FileUploadView;
import org.appfuse.webapp.client.ui.upload.views.UploadedFileView;

import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteEvent;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * @author ivangsa
 *
 */
public class FileUploadActivity extends AbstractBaseActivity implements FileUploadView.Delegate, UploadedFileView.Delegate{

	private FileUploadView formView;
	private UploadedFileView resultsView;
	private AcceptsOneWidget panel;

	public FileUploadActivity(Application application) {
		super(application);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client.ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.panel = panel;
		showForm();
	}

	private void showForm() {
		formView = viewFactory.getView(FileUploadView.class);
		formView.setDelegate(this);
		formView.edit(new FileUploadBean());
		panel.setWidget(formView);
	}
	

	
	@Override
	public void onSubmit(SubmitEvent event) {
		if(formView != null) {
			FileUploadBean fileUpload = formView.getEditorDriver().flush();
			Set violations = validatorFactory.getValidator().validate(fileUpload);
			formView.getEditorDriver().setConstraintViolations(violations);
			if(!violations.isEmpty()) {
				event.cancel();
			}
		}
	}

	@Override
	public void onSubmitComplete(SubmitCompleteEvent event) {
		UploadedFileBean uploadedFile = parseResponse(event.getResults());
		if(uploadedFile.getErrorMessages() != null && uploadedFile.getErrorMessages().length() > 0) {
			for (int i = 0; i < uploadedFile.getErrorMessages().length(); i++) {
				String error = uploadedFile.getErrorMessages().get(i);
				shell.addMessage(error, AlertType.ERROR);
			}
		} else {
			shell.addMessage(uploadedFile.getLocation(), AlertType.SUCCESS);
			showResults(uploadedFile);
		}
	}

	private void showResults(UploadedFileBean uploadedFile) {
		resultsView = viewFactory.getView(UploadedFileView.class);
		resultsView.setDelegate(this);
		resultsView.edit(uploadedFile);
		panel.setWidget(resultsView);
	}
	
	@Override
	public void onDoneClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUploadAnotherFileClick() {
		// TODO Auto-generated method stub
		
	}

	private final native UploadedFileBean parseResponse(String json) /*-{
	    return eval('(' + json + ')');
	}-*/;
}
