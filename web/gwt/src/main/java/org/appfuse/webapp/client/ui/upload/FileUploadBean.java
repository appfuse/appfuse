/**
 * 
 */
package org.appfuse.webapp.client.ui.upload;

import javax.validation.constraints.NotNull;

/**
 * @author ivangsa
 *
 */
public class FileUploadBean {

    @NotNull
    private String name;

    @NotNull
    private String file;

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
