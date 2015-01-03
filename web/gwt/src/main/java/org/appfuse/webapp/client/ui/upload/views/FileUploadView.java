package org.appfuse.webapp.client.ui.upload.views;

import java.util.List;

import org.appfuse.webapp.client.ui.upload.FileUploadBean;

import com.github.gwtbootstrap.client.ui.Form.SubmitCompleteHandler;
import com.github.gwtbootstrap.client.ui.Form.SubmitHandler;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface FileUploadView extends IsWidget {

    public interface Delegate extends SubmitHandler, SubmitCompleteHandler {
        void onCancelClick();
    }

    void setDelegate(Delegate delegate);

    void edit(FileUploadBean fileUpload);

    EditorDriver<FileUploadBean> getEditorDriver();

    void showErrorsMessages(List<String> errors);
}