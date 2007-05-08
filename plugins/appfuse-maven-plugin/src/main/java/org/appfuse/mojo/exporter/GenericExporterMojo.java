package org.appfuse.mojo.exporter;

/*
 * Copyright 2005 Johann Reyes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.MojoExecutionException;
import org.appfuse.mojo.HibernateExporterMojo;
import org.codehaus.mojo.hibernate3.HibernateUtils;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.hbm2x.GenericExporter;

/**
 * Generic exporter that can be controlled by a user provided template or class.
 *
 * @author <a href="mailto:jreyes@hiberforum.org">Johann Reyes</a>
 * @version $Id$
 * @goal hbmtemplate
 */
public class GenericExporterMojo extends HibernateExporterMojo {
    /**
     * Default constructor.
     */
    public GenericExporterMojo() {
        addDefaultComponent("target/appfuse/generic", "jdbcconfiguration", false);
        addDefaultComponent("target/appfuse/generic", "jdbcconfiguration", true);
    }

// --------------------- Interface ExporterMojo ---------------------

    /**
     * Returns <b>hbmtemplate</b>.
     *
     * @return String goal's name
     */
    public String getName() {
        return "hbmtemplate";
    }

    /**
     * @see org.appfuse.mojo.HibernateExporterMojo#configureExporter(org.hibernate.tool.hbm2x.Exporter)
     */
    protected Exporter configureExporter(Exporter exporter) throws MojoExecutionException {
        super.configureExporter(exporter);

        if (exporter instanceof GenericExporter) {
            GenericExporter ge = (GenericExporter) exporter;
            ge.setFilePattern(getComponentProperty("filepattern"));
            ge.setTemplateName(getComponentProperty("template"));
        }

        return exporter;
    }

    /**
     * Instantiates a org.hibernate.tool.hbm2x.Exporter object.
     *
     * @return Exporter
     */
    protected Exporter createExporter() {
        String exporterClass = getComponentProperty("exporterclass");
        if (exporterClass != null) {
            Exporter exporter = (Exporter) HibernateUtils.getClass(exporterClass);
            if (exporter != null) {
                getLog().info("Using exporter class " + exporterClass);
                return exporter;
            } else {
                getLog().error("Could not create custom exporter class: " + exporterClass);
            }
        }
        getLog().info("Using exporter class org.hibernate.tool.hbm2x.GenericExporter");
        return new GenericExporter();
    }
}
