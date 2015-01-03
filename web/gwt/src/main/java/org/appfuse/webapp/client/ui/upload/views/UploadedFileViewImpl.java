/**
 * 
 */
package org.appfuse.webapp.client.ui.upload.views;

import org.appfuse.webapp.client.ui.upload.UploadedFileBean;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author ivangsa
 *
 */
public class UploadedFileViewImpl extends Composite implements UploadedFileView, Editor<UploadedFileBean> {

    interface Binder extends UiBinder<Widget, UploadedFileViewImpl> {
    }

    private static final Binder BINDER = GWT.create(Binder.class);

    interface Driver extends SimpleBeanEditorDriver<UploadedFileBean, UploadedFileViewImpl> {
    }

    private Driver driver = GWT.create(Driver.class);

    private UploadedFileView.Delegate delegate;

    @UiField
    Label name;
    @UiField
    Label fileName;
    @UiField
    Label contentType;
    @UiField
    Label size;
    @UiField
    AnchorElement location;

    @UiField
    Button doneButton;
    @UiField
    Button uploadAnotherFileButton;

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
    public void display(UploadedFileBean object) {
        driver.edit(object);
        location.setHref(object.getLink());
        location.setInnerSafeHtml(SafeHtmlUtils.fromSafeConstant(object.getLocation()));
    }

    @Override
    public EditorDriver<UploadedFileBean> getEditorDriver() {
        return driver;
    }

    @UiHandler("doneButton")
    public void onDoneClick(ClickEvent event) {
        delegate.onDoneClick();
    }

    @UiHandler("uploadAnotherFileButton")
    public void onUploadAnotherFileClick(ClickEvent event) {
        delegate.onUploadAnotherFileClick();
    }

}
