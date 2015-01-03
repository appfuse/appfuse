/**
 * 
 */
package org.appfuse.webapp.client.ui.upload;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * @author ivangsa
 *
 */
public class UploadedFileBean extends JavaScriptObject {

    protected UploadedFileBean() {
    };

    public final native JsArrayString getErrorMessages() /*-{ return this.errorMessages; }-*/;

    public final native String getName() /*-{ return this.name; }-*/;

    public final native String getFileName() /*-{ return this.fileName; }-*/;

    public final native String getContentType() /*-{ return this.contentType; }-*/;

    public final native String getSize() /*-{ return this.size; }-*/;

    public final native String getLocation() /*-{ return this.location; }-*/;

    public final native String getLink() /*-{ return this.link; }-*/;

}
