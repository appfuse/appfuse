package org.appfuse.webapp.internal;


/**
 * Menu context used to communicate between the menu and menuitem components
 *
 * @author Serge Eby
 */
public interface MenuContext {
    boolean isActive(String id);
}
