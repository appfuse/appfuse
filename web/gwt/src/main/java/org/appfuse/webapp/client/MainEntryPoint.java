/**
 * 
 */
package org.appfuse.webapp.client;

import org.appfuse.webapp.client.application.ioc.ClientInjector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * @author ivangsa
 *
 */
public class MainEntryPoint implements EntryPoint {

    final private ClientInjector injectorWrapper = GWT.create(ClientInjector.class);

    public void onModuleLoad() {
        /* Get and run platform specific app */
        injectorWrapper.getApplication().run();
    }

}
