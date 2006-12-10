package org.appfuse.engine.graphical;

import org.appfuse.engine.ApplicationGenerator;
import org.appfuse.ui.StartDialog;

import java.awt.*;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public class SwingApplicationGenerator extends ApplicationGenerator {

    public void createApplication() {

        /**
         * The swing dialog
         */
        StartDialog start = new StartDialog(super.getApplicationData());

        /**
         * Name the dialog
         */
        start.setTitle("AppFuse Installer");

        /**
         * The dialog widgets are placed using GridBagLayout.  The final
         * sizing of the dialog needs to be done here.
         */
        start.setSize(new Dimension(400,280));

        /**
         * Show dialog and get user inputs
         */
        start.setVisible(true);


    }
}
