package org.appfuse.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.html.BasePage;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;

public abstract class UserList extends BasePage {   
    private final Log log = LogFactory.getLog(UserList.class);
    
    public abstract UserManager getUserManager();
    public abstract void setUserManager(UserManager manager);
    
    public void add(IRequestCycle cycle) {
        if (log.isDebugEnabled()) {
            log.debug("activating new user page");
        }
        UserForm nextPage = (UserForm) cycle.getPage("userForm");
        nextPage.setUser(new User());
        cycle.activate(nextPage);
    }
    
    public void edit(IRequestCycle cycle) {
        UserForm nextPage = (UserForm) cycle.getPage("userForm");
        Object[] parameters = cycle.getServiceParameters();
        String username = (String) parameters[0];
        if (log.isDebugEnabled()) {
            log.debug("fetching user with username: " + username);
        }
        User user = getUserManager().getUser(username);
        nextPage.setUser(user);
        cycle.activate(nextPage);
    }
    
    public String getAddLink() {
        IComponent directLinkComponent = getComponent("addUserLink");

        IEngineService service = 
            getPage().getEngine().getService(Tapestry.DIRECT_SERVICE);
        ILink link = 
            service.getLink(getPage().getRequestCycle(), directLinkComponent, null);
        return link.getURL();   
    }
    
    private String message;
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
