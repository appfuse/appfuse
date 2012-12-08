package org.appfuse.webapp.pages.admin;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.model.Model;
import org.appfuse.webapp.listener.StartupListener;
import org.appfuse.webapp.AbstractWebPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * Page for reloading an application configuration.
 *
 * @author Marcin Zajączkowski, 2011-02-12
 */
@MountPath("admin/reload")
public class Reload extends AbstractWebPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();

        StartupListener.setupContext(getServletContext());
        
        getSession().info(createDefaultInfoNotificationMessage(Model.of(getString("reload.succeeded"))));
        throw new RestartResponseException(getApplication().getHomePage());
    }
}
