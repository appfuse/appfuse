package org.appfuse.service;

import org.apache.struts.util.ModuleException;

/**
 * Example of an application-specific exception for which a handler
 * can be configured.
 * 
 * <p>
 * <a href="ConversionException.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version $Revision: 1.1 $ $Date: 2004/03/01 06:19:09 $
 */
public class ConversionException extends ModuleException {

    /**
     * Construct a new instance of this exception
     */
    public ConversionException() {
        super("errors.conversion");
    }

}