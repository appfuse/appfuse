package org.appfuse;
/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */

import org.appfuse.ui.StartDialog;

import java.awt.*;

/**
 * This class is used to start the AppFuse QuickStart application
 */
public class ApplicationMain {

    public static void main(String[] args) {
        StartDialog start = new StartDialog();
        start.setTitle("Voila Installer v.0.0.1 Beta");
        start.setSize(new Dimension(400,280));
        start.setVisible(true);
        System.exit(0);
    }
}
