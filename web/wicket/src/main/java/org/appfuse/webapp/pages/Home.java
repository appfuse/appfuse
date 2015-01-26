package org.appfuse.webapp.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * Welcome page for authorized users.
 *
 * @author Marcin Zajączkowski, 2010-09-05
 */
@MountPath("home")
@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class Home extends AbstractWebPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createFeedbackPanel());
        add(createPageTitleTag("home.title"));
        add(createPageHeading("home.heading"));
        add(createPageMessage("home.message"));
    }
}
