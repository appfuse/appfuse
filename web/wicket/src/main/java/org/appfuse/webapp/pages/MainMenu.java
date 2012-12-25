package org.appfuse.webapp.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.appfuse.webapp.AbstractWebPage;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * Welcome page for authorized users.
 *
 * @author Marcin Zajączkowski, 2010-09-05
 */
@MountPath("mainMenu")
@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class MainMenu extends AbstractWebPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createFeedbackPanel());
        add(createPageTitleTag("mainMenu.title"));
        add(createPageHeading("mainMenu.heading"));
        add(createPageMessage("mainMenu.message"));
    }
}
