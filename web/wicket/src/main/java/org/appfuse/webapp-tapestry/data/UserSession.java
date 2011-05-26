package org.appfuse.webapp.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.appfuse.model.User;

import java.io.Serializable;

public class UserSession implements Serializable {
    private static final long serialVersionUID = 1932462707656603990L;
    private User currentUser;
    private boolean cookieLogin;

    /**
     * @return Returns the cookieLogin.
     */
    public boolean isCookieLogin() {
        return cookieLogin;
    }

    /**
     * @param cookieLogin The cookieLogin to set.
     */
    public void setCookieLogin(boolean cookieLogin) {
        this.cookieLogin = cookieLogin;
    }

    /**
     * @return Returns the currentUser.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param currentUser The currentUser to set.
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
