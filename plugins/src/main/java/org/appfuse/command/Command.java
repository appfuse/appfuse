package org.appfuse.command;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public abstract class Command {
    private boolean status = false;
    private String name;

    public Command(String name) {
        this.name = name;
    }
    
    public boolean getStatus() {
        return status;

    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString() {
        return name;
    }
}
