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
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.hbm2x.POJOExporter;

import java.io.File;

/**
 * Generates Java classes from set of *.hbm.xml files
 *
 * @author <a href="mailto:jreyes@hiberforum.org">Johann Reyes</a>
 * @version $Id: ModelGeneratorMojo.java 3535 2007-03-07 21:02:07Z jreyes $
 * @goal gen-model
 * @phase generate-sources
 * @execute phase="process-resources"
 */
public class ModelGeneratorMojo extends HibernateExporterMojo {
    /**
     * Default constructor.
     */
    public ModelGeneratorMojo() {
        addDefaultComponent("target/appfuse/generated-sources", "configuration", false);
        addDefaultComponent("target/appfuse/generated-sources", "annotationconfiguration", true);
    }

// --------------------- Interface ExporterMojo ---------------------

    /**
     * Returns <b>gen-model</b>.
     *
     * @return String goal's name
     */
    public String getName() {
        return "gen-model";
    }

    /**
     * @see HibernateExporterMojo#configureExporter(org.hibernate.tool.hbm2x.Exporter)
     */
    protected Exporter configureExporter(Exporter exp) throws MojoExecutionException {
        // add output directory to compile roots
        getProject().addCompileSourceRoot(new File(getComponent().getOutputDirectory()).getPath());

        // now set the extra properties for the POJO Exporter
        POJOExporter exporter = (POJOExporter) super.configureExporter(exp);
        exporter.setTemplateName("appfuse/model/Pojo.ftl");
        exporter.getProperties().setProperty("ejb3", getComponentProperty("ejb3", "false"));
        exporter.getProperties().setProperty("jdk5", getComponentProperty("jdk5", "false"));

        if (isFullSource()) {
            exporter.getProperties().setProperty("appfusepackage", getProject().getGroupId());
        } else {
            exporter.getProperties().setProperty("appfusepackage", "org.appfuse");
        }

        
        return exporter;
    }

    /**
     * Instantiates a org.hibernate.tool.hbm2x.POJOExporter object.
     *
     * @return POJOExporter
     */
    protected Exporter createExporter() {
        return new POJOExporter();
    }
}