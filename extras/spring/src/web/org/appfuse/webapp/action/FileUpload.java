package org.appfuse.webapp.action;

/**
 * Command class to handle uploading of a file.
 *
 * <p>
 * <a href="FileUpload.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/05/04 06:09:57 $
 */
public class FileUpload {
    private String name;
    private byte[] file;
    
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
    public void setFile(byte[] file) {
        this.file = file;
    }
    
    public byte[] getFile() {
        return file;
    }
}
