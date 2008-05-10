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
import org.apache.maven.plugin.MojoFailureException;
import org.apache.commons.io.FileUtils;
import org.appfuse.mojo.HibernateExporterMojo;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.tool.hbm2x.POJOExporter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.Iterator;

/**
 * Generates Java classes from existing database tables. If you want to customize the
 * reverse engineering strategy, you can modify the default <a href="http://tinyurl.com/3qqgcl">hibernate.reveng.xml</a>
 * and put it in src/test/resources. You can also override the location by specifying the "revengfile"
 * property in the &lt;configuration&gt;. For example:
 *
 * <pre>
   &lt;configuration&gt;
   &nbsp;&nbsp;&lt;componentProperties&gt;
   &nbsp;&nbsp;&nbsp;&nbsp;&lt;revengfile&gt;path/to/hibernate.reveng.xml&lt;/revengfile&gt;
   &nbsp;&nbsp;&lt;/componentProperties&gt;
   &nbsp;&nbsp;&lt;genericCore&gt;${amp.genericCore}&lt;/genericCore&gt;
   &nbsp;&nbsp;&lt;fullSource&gt;${amp.fullSource}&lt;/fullSource&gt;
   &lt;/configuration&gt;
 * </pre>
 *
 * <p><b>Oracle Users</b>: If you create a custom hibernate.reveng.xml, make sure to capitalize the table names
 * in &lt;schema-selection match-schema="MY_SCHEMA_NAME"/&gt;. You'll also need to add the following line to your
 * jdbc.properties file:</p>
 * <pre>
   hibernatetool.metadatadialect=org.hibernate.cfg.reveng.dialect.OracleMetaDataDialect
   </pre>
 *
 * @author <a href="mailto:jreyes@hiberforum.org">Johann Reyes</a>
 * @version $Id: ModelGeneratorMojo.java 3535 2007-03-07 21:02:07Z jreyes $
 * @goal gen-model
 * @phase generate-sources
 * @execute phase="process-resources"
 */
public class ModelGeneratorMojo extends HibernateExporterMojo {
    /**
     * The path where the generated artifacts will be placed. This is intentionally not set to the
     * default location for maven generated sources. This is to keep these files out of the
     * eclipse/idea generated sources directory as the intention is that these files will be copied
     * to a source directory to be edited and modified and not re generated each time the plugin is
     * run. If you want to regenerate the files each time you build the project just set this value
     * to ${basedir}/target/generated-sources or set the flag on eclipse/idea plugin to include this
     * file in your project file as a source directory.
     *
     * @parameter expression="${appfuse.destinationDirectory}" default-value="${basedir}"
     * @noinspection UnusedDeclaration
     */
    private String destinationDirectory;

    /**
     * The directory containing the source code.
     *
     * @parameter expression="${appfuse.sourceDirectory}" default-value="${basedir}/target/appfuse/generated-sources"
     * @noinspection UnusedDeclaration
     */
    private String sourceDirectory;

    /**
     * Allows disabling installation - for tests and end users that don't want to do a full installation
     *
     * @parameter expression="${appfuse.disableInstallation}" default-value="false"
     */
    private boolean disableInstallation;

    /**
     * @parameter expression="${appfuse.templateDirectory}" default-value="${basedir}/src/test/resources"
     * @noinspection UnusedDeclaration
     */
    private String templateDirectory;

    /**
     * Default constructor.
     */
    public ModelGeneratorMojo() {
        addDefaultComponent("target/appfuse/generated-sources", "configuration", false);
        addDefaultComponent("target/appfuse/generated-sources", "jdbcconfiguration", true);
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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getComponentProperties().put("implementation", "jdbcconfiguration");
        getComponentProperties().put("outputDirectory",
                (sourceDirectory != null) ? sourceDirectory : "${basedir}/target/appfuse/generated-sources");

        // default location for reveng file is src/test/resources
        File revengFile = new File("src/test/resources/hibernate.reveng.xml");
        if (revengFile.exists() && getComponentProperty("revengfile") == null) {
           getComponentProperties().put("revengfile", "src/test/resources/hibernate.reveng.xml");
        }
        
        // Check for existence of hibernate.reveng.xml and if there isn't one, create it
        // Specifying the file explicitly in pom.xml overrides default location
        if (getComponentProperty("revengfile") == null) {
            getComponentProperties().put("revengfile", "target/test-classes/hibernate.reveng.xml");
        }

        File existingConfig = new File(getComponentProperty("revengfile"));
        if (!existingConfig.exists()) {
            InputStream in = this.getClass().getResourceAsStream("/appfuse/model/hibernate.reveng.ftl");
            StringBuffer configFile = new StringBuffer();
            try {
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                while ((line = reader.readLine()) != null) {
                    configFile.append(line).append("\n");
                }
                reader.close();

                getLog().info("Writing 'hibernate.reveng.xml' to " + existingConfig.getPath());
                FileUtils.writeStringToFile(existingConfig, configFile.toString());
            } catch (IOException io) {
                throw new MojoFailureException(io.getMessage());
            }
        }

        // if package name is not configured, default to project's groupId
        if (getComponentProperty("packagename") == null) {
            getComponentProperties().put("packagename", getProject().getGroupId() + ".model");
        }
        
        if (getComponentProperty("configurationfile") == null) {
            // look for jdbc.properties and set "propertyfile" to its path
            File jdbcProperties = new File("target/classes/jdbc.properties");
            if (!jdbcProperties.exists()) {
                jdbcProperties = new File("target/test-classes/jdbc.properties");
            }
            if (jdbcProperties.exists()) {
                if (getComponentProperty("propertyfile") == null) {
                    getComponentProperties().put("propertyfile", jdbcProperties.getPath());
                    getLog().debug("Set propertyfile to '" + jdbcProperties.getPath() + "'");
                }
            } else {
                throw new MojoFailureException("Failed to find jdbc.properties in classpath.");
            }
        }

        // For some reason, the classloader created in HibernateExporterMojo does not work
        // when using jdbcconfiguration - it can't find the JDBC Driver (no suitable driver).
        // Skipping the resetting of the classloader and manually adding the dependency (with XML) works.
        // It's ugly, but it works. I wish there was a way to get get this plugin to recognize the jdbc driver
        // from the project.

        super.doExecute();

        if (System.getProperty("disableInstallation") != null) {
            disableInstallation = Boolean.valueOf(System.getProperty("disableInstallation"));
        }

        // allow installation to be supressed when testing
        if (!disableInstallation) {
            // copy the generated file to the model directory of the project
            try {
                String packageName = getComponentProperties().get("packagename");
                String packageAsDir = packageName.replaceAll("\\.", "/");
                File dir = new File(sourceDirectory + "/" + packageAsDir);
                if (dir.exists()) {
                    Iterator filesIterator = FileUtils.iterateFiles(dir, new String[] {"java"}, false);
                    while (filesIterator.hasNext()) {
                        File f = (File) filesIterator.next();
                        getLog().info("Copying generated '" + f.getName() + "' to project...");
                        FileUtils.copyFileToDirectory(f, new File(destinationDirectory + "/src/main/java/" + packageAsDir));
                    }
                } else {
                    throw new MojoFailureException("No tables found in database to generate code from.");
                }
                FileUtils.forceDelete(dir);
            } catch (IOException io) {
                throw new MojoFailureException(io.getMessage());
            }
        }
    }

    /**
     * @see HibernateExporterMojo#configureExporter(org.hibernate.tool.hbm2x.Exporter)
     */
    protected Exporter configureExporter(Exporter exp) throws MojoExecutionException {
        // add output directory to compile roots
        getProject().addCompileSourceRoot(new File(getComponent().getOutputDirectory()).getPath());

        // now set the extra properties for the POJO Exporter
        POJOExporter exporter = (POJOExporter) super.configureExporter(exp);

        // Add custom template path if specified
        String[] templatePaths;
        if (templateDirectory != null) {
            templatePaths = new String[exporter.getTemplatePaths().length + 1];
            templatePaths[0] = templateDirectory;
            if (exporter.getTemplatePaths().length > 1) {
                for (int i = 1; i < exporter.getTemplatePaths().length; i++) {
                    templatePaths[i] = exporter.getTemplatePaths()[i-1];
                }
            }
        } else {
            templatePaths = exporter.getTemplatePaths();
        }

        exporter.setTemplatePath(templatePaths);
        exporter.setTemplateName("appfuse/model/Pojo.ftl");
        exporter.getProperties().setProperty("basepackage", getProject().getGroupId());
        exporter.getProperties().setProperty("ejb3", getComponentProperty("ejb3", "true"));
        exporter.getProperties().setProperty("jdk5", getComponentProperty("jdk5", "true"));

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
