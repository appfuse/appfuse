package org.appfuse.webapp.pages.admin;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.appfuse.model.User;
import org.appfuse.webapp.pages.Home;

import java.util.Set;

/**
 * Lists all active users
 *
 * @author Serge Eby
 * @version $Id: ActiveUsers.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ActiveUsers {

    @Inject
    private Messages messages;

    @Property
    private User currentUser;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private PageRenderLinkSource pageRenderLinkSource;

    @Inject
    private Context context;


    public BeanModel<User> getModel() {
        final BeanModel<User> model = beanModelSource.createDisplayModel(User.class, messages);
        model.include("username");
        model.add("fullname");
        // Set labels
        model.get("username").label(messages.get("user.username"));
        model.get("fullname").label(messages.get("activeUsers.fullName"));

        return model;
    }

    @SuppressWarnings("unchecked")
    public Set<User> getActiveUsers() {
        return (Set<User>) context.getAttribute("userNames");
    }

    public String getHomeLink() {
        return pageRenderLinkSource.createPageRenderLink(Home.class).toURI();
    }
}
