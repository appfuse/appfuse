package org.appfuse.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * Base class for Model objects.  Child objects should implement equals()
 * and hashCode();
 *
 * <p>
 * <a href="BaseObject.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public abstract class BaseObject implements Serializable {

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public abstract int hashCode();
    public abstract boolean equals(Object o);
}
