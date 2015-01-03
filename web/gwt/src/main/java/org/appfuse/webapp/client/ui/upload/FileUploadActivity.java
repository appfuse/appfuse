/**
 * 
 */
package org.appfuse.webapp.client.ui.upload;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.appfuse.webapp.client.application.Application;
import org.appfuse.webapp.client.application.base.activity.AbstractBaseActivity;
import org.appfuse.webapp.client.ui.home.HomePlace;
import org.appfuse.webapp.client.ui.upload.views.FileUploadView;
import org.appfuse.webapp.client.ui.upload.views.UploadedFileView;

import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteEvent;
import com.github.gwtbootstrap.client.ui.Form.SubmitEvent;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

/**
 * @author ivangsa
 *
 */
public class FileUploadActivity extends AbstractBaseActivity implements FileUploadView.Delegate, UploadedFileView.Delegate {

    private final FileUploadView formView;
    private final UploadedFileView resultsView;
    private AcceptsOneWidget panel;

    @Inject
    public FileUploadActivity(final Application application, final FileUploadView formView, final UploadedFileView resultsView) {
        super(application);
        this.formView = formView;
        this.resultsView = resultsView;
        setTitle(i18n.upload_title());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.google.gwt.activity.shared.Activity#start(com.google.gwt.user.client
     * .ui.AcceptsOneWidget, com.google.gwt.event.shared.EventBus)
     */
    @Override
    public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
        this.panel = panel;
        showForm();
        setDocumentTitleAndBodyAttributtes();
    }

    private void showForm() {
        formView.setDelegate(this);
        formView.edit(new FileUploadBean());
        panel.setWidget(formView);
    }

    @Override
    public void onSubmit(final SubmitEvent event) {
        if (formView != null) {
            final FileUploadBean fileUpload = formView.getEditorDriver().flush();
            final Set violations = getValidator().validate(fileUpload);
            formView.getEditorDriver().setConstraintViolations(violations);
            if (!violations.isEmpty()) {
                event.cancel();
            }
        }
    }

    @Override
    public void onSubmitComplete(final SubmitCompleteEvent event) {
        final UploadedFileBean uploadedFile = parseResponse(event.getResults());
        if (uploadedFile.getErrorMessages() != null && uploadedFile.getErrorMessages().length() > 0) {
            final List<String> errorMessages = new ArrayList<String>();
            for (int i = 0; i < uploadedFile.getErrorMessages().length(); i++) {
                errorMessages.add(uploadedFile.getErrorMessages().get(i));
            }
            formView.showErrorsMessages(errorMessages);
        } else {
            shell.addMessage(uploadedFile.getLocation(), AlertType.SUCCESS);
            showResults(uploadedFile);
        }
    }

    private void showResults(final UploadedFileBean uploadedFile) {
        resultsView.setDelegate(this);
        resultsView.display(uploadedFile);
        panel.setWidget(resultsView);
    }

    @Override
    public void onDoneClick() {
        placeController.goTo(new HomePlace());
    }

    @Override
    public void onCancelClick() {
        placeController.goTo(new HomePlace());
    }

    @Override
    public void onUploadAnotherFileClick() {
        showForm();
    }

    private final native UploadedFileBean parseResponse(String json) /*-{
                                                                     return eval('(' + json + ')');
                                                                     }-*/;
}
