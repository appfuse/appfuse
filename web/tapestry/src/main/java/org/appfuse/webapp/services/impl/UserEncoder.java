package org.appfuse.webapp.services.impl;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;


public class UserEncoder implements ValueEncoder<User> {

    private UserManager userManager;

    public UserEncoder(UserManager userManager) {
        this.userManager = userManager;
    }

    public String toClient(User value) {
        return value != null ? String.valueOf(value.getId()) : null;
    }

    public User toValue(String clientValue) {

        if (clientValue == null || "null".equals(clientValue)) {
            return null;
        }

        Long id = null;
        try {
            id = Long.valueOf(clientValue);
        }
        catch (Exception ex) {
            return new User();
        }
        User user = userManager.get(id);
        // Reset confirmed password
        if (user != null) {
            user.setConfirmPassword(user.getPassword());
        }

        return user;
    }

}
