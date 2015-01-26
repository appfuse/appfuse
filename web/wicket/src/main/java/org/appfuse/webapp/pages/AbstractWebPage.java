package org.appfuse.webapp.pages;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestParameters;
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

    private static final String LOCALE_REQUEST_PARAMETER_NAME = "locale";

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
        addCommonStyles(response);
        addCommonScripts(response);
    }

    private void addCommonStyles(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl("styles/style.css"));
    }

    private void addCommonScripts(IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forUrl("scripts/script.js"));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        changeLocaleIfRequestedByRequestParameter();
    }

    private void changeLocaleIfRequestedByRequestParameter() {
        IRequestParameters queryParameters = getRequest().getQueryParameters();
        if (queryParameters.getParameterNames().contains(LOCALE_REQUEST_PARAMETER_NAME)) {
            //with "locale" GET parameter available LocaleFilter overrides getLocale() for request with that value
            getSession().setLocale(getRequest().getLocale());
        }
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
