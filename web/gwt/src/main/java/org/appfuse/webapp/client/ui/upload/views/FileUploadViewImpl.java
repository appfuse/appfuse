/**
 * 
 */
package org.appfuse.webapp.client.ui.upload.views;

import java.util.List;

import org.appfuse.webapp.client.ui.upload.FileUploadBean;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.FileUpload;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class FileUploadViewImpl extends Composite implements FileUploadView, Editor<FileUploadBean> {

    interface Binder extends UiBinder<Widget, FileUploadViewImpl> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    interface Driver extends SimpleBeanEditorDriver<FileUploadBean, FileUploadViewImpl> {
    }

    private Driver driver = GWT.create(Driver.class);

    private Delegate delegate;

    @UiField
    FlowPanel errorsPanel;

    @UiField
    Form form;
    @UiField
    TextBox name;
    @UiField
    FileUpload file;

    @UiField
    HasClickHandlers cancelButton;

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
        this.delegate = delegate;
        form.addSubmitHandler(delegate);
        form.addSubmitCompleteHandler(delegate);
    }

    @Override
    public void edit(FileUploadBean object) {
        driver.edit(object);
        errorsPanel.clear();
    }

    @Override
    public EditorDriver<FileUploadBean> getEditorDriver() {
        return driver;
    }

    @Override
    public void showErrorsMessages(List<String> errors) {
        errorsPanel.clear();
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        for (String error : errors) {
            sb.appendEscaped(error);
            sb.appendHtmlConstant("<br />");
        }
        errorsPanel.add(new Alert(sb.toSafeHtml().asString(), AlertType.ERROR));
    }

    @UiHandler("cancelButton")
    void onCancelClick(ClickEvent event) {
        delegate.onCancelClick();
    }
}
