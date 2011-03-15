package org.appfuse.webapp.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.ConfigurationException;
import org.compass.spring.LocalCompassBeanPostProcessor;

/**
 * Compass Post Processor that allows for adding scan mappings for more than
 * one root package.
 */
public class CompassConfigurationPostProcessor implements LocalCompassBeanPostProcessor {
    Log log = LogFactory.getLog(CompassConfigurationPostProcessor.class);

    public void process(CompassConfiguration config) throws ConfigurationException {
        // Look at current class's package and add it if it's not the AppFuse default
        String classPackage = this.getClass().getPackage().getName();
        String rootPackage = classPackage.substring(0, classPackage.indexOf("webapp") - 1);
        if (!rootPackage.equals("org.appfuse")) {
            log.debug("Adding scan for package: " + rootPackage);
            config.addScan(rootPackage);
        }
    }
}
