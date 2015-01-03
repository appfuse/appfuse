package org.appfuse.webapp.server.locators;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.web.bindery.requestfactory.shared.Locator;

@Component
public class UserLocator extends Locator<User, Long> {

    @Autowired
    private UserManager userManager;

    public User create(Class<? extends User> clazz) {
        return new User();
    }

    public User find(Class<? extends User> clazz, Long id) {
        User user = userManager.get(id);
        user.setConfirmPassword(user.getPassword());
        return user;
    }

    public Class<User> getDomainType() {
        return User.class;
    }

    public Long getId(User user) {
        return user.getId();
    }

    public Class<Long> getIdType() {
        return Long.class;
    }

    public Object getVersion(User user) {
        return user.getVersion();
    }

    @Override
    public boolean isLive(User domainObject) {
        return true;
    }
}
