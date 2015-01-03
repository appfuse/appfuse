package org.appfuse.webapp.client.ui.upload.views;

import org.appfuse.webapp.client.ui.upload.UploadedFileBean;

import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

public interface UploadedFileView extends IsWidget {

    public interface Delegate {
        void onDoneClick();

        void onUploadAnotherFileClick();
    }

    void setDelegate(Delegate delegate);

    void display(UploadedFileBean object);

    EditorDriver<UploadedFileBean> getEditorDriver();
}