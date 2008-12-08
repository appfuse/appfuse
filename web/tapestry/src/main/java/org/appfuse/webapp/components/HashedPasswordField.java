package org.appfuse.webapp.components;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.corelib.base.AbstractTextField;

/**
 * A version of {@link TextField}, but rendered out as an &lt;input type="password"&gt; element. Further, the output
 * value for a HashedPasswordField is always encrypted.
 * <p/>
 * Includes the <code>size</code> attribute, if a {@link org.apache.tapestry5.beaneditor.Width} annotation is present on
 * the property bound to the value parameter.
 * 
 * @author Serge Eby
 * @version $Id: HashedPasswordField.java 5 2008-08-30 09:59:21Z serge.eby $
 * 
 */
public class HashedPasswordField extends AbstractTextField  { 


    @Override
    protected final void writeFieldTag(MarkupWriter writer, String value) {
        writer.element("input",
                       "type", "password",
                       "name", getControlName(),
                       "id", getClientId(),
                       "value", value,
                       "size", getWidth());
    }

    final void afterRender(MarkupWriter writer) {
        writer.end(); // input
    }
}

