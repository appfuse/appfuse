/**
 * 
 */
package org.appfuse.webapp.client.proxies;

import org.appfuse.model.LabelValue;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

/**
 * @author ivangsa
 *
 */
@ProxyFor(LabelValue.class)
public interface LabelValueProxy extends ValueProxy {

    String getLabel();

    void setLabel(String label);

    String getValue();

    void setValue(String value);

}
