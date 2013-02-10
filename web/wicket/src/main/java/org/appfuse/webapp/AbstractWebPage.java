package org.appfuse.webapp;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

/**
 * Abstract WebPage for every page in a project.
 *
 * @author Marcin Zajączkowski, 2011-02-07
 */
public abstract class AbstractWebPage extends WebPage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected ServletContext getServletContext() {
        return ((WebApplication)getApplication()).getServletContext();
    }

    protected NotificationPanel createFeedbackPanel() {
        return new NotificationPanel("feedback");
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        Bootstrap.renderHead(response);
    }

    protected NotificationMessage createDefaultInfoNotificationMessage(IModel<String> messageModel) {
        return new NotificationMessage(messageModel)
                .hideAfter(Duration.seconds(5));
    }

    protected Label createPageTitleTag(String resourceKey) {
        return new Label("pageTitle", new ResourceModel(resourceKey));
    }

    protected Label createPageHeading(String resourceKey) {
        return new Label("pageHeading", new ResourceModel(resourceKey));
    }

    protected Label createPageMessage(String resourceKey) {
        return new Label("pageMessage", new ResourceModel(resourceKey));
    }
}
