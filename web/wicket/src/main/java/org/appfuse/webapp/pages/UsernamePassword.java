package org.appfuse.webapp.pages;

import java.io.Serializable;

/**
 * net.sf.wd.webapp.pages.UsernamePassword
 *
 * TODO: MZA: Artificial class - try to get it off
 *
 * @deprecated Seems to be not used anymore
 *
 * @author Marcin Zajączkowski, 2010-09-03
 */
@Deprecated
public class UsernamePassword implements Serializable {

    private String username;
    private String password;

    public UsernamePassword() {
    }

    public UsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
