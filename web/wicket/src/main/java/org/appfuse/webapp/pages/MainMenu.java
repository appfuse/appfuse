package org.appfuse.webapp.pages;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * Welcome page for authorized users.
 *
 * @author Marcin Zajączkowski, 2010-09-05
 */
@MountPath(path = "mainMenu")
@AuthorizeInstantiation({"ROLE_ADMIN", "ROLE_USER"})
public class MainMenu extends WebPage {

    @Override
    protected void onInitialize() {
        super.onInitialize();

        FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
    }
}
