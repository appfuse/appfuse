package org.appfuse.mojo;

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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.appfuse.mojo.exporter.Component;
import org.codehaus.mojo.hibernate3.ExporterMojo;
import org.codehaus.mojo.hibernate3.HibernateUtils;
import org.codehaus.mojo.hibernate3.configuration.ComponentConfiguration;
import org.hibernate.tool.hbm2x.Exporter;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Base class for the different appfuse goals based on the Ant tasks of hibernate tools.
 *
 * @author <a href="mailto:jreyes@hiberforum.org">Johann Reyes</a>
 * @version $Id: HibernateExporterMojo.java 3535 2007-03-07 21:02:07Z jreyes $
 * @requiresDependencyResolution test
 */
public abstract class HibernateExporterMojo extends AbstractMojo implements ExporterMojo {
    /**
     * Parameter that holds components definitions specified by the user.
     *
     * @parameter
     * @noinspection MismatchedQueryAndUpdateOfCollection
     */
    private List<Component> components = new ArrayList<Component>();

    /**
     * Map holding the default component values for this goal.
     */
    private Map<String, Component> defaultComponents = new HashMap<String, Component>();

    /**
     * Parameter that holds component properties defined by the user.
     *
     * @parameter
     * @noinspection MismatchedQueryAndUpdateOfCollection
     */
    private Map<String, String> componentProperties = new HashMap<String, String>();

    /**
     * @component role="org.codehaus.mojo.hibernate3.configuration.ComponentConfiguration"
     * @noinspection MismatchedQueryAndUpdateOfCollection
     */
    private List<ComponentConfiguration> componentConfigurations = new ArrayList<ComponentConfiguration>();

    /**
     * <i>Maven Internal</i>: Project to interact with.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     * @noinspection UnusedDeclaration
     */
    private MavenProject project;

    /**
     * @parameter expression="${appfuse.fullSource}" default-value="false"
     * @noinspection UnusedDeclaration
     */
    private boolean fullSource;

    public boolean isFullSource() {
        return fullSource;
    }

    // --------------------- Interface ExporterMojo ---------------------

    /**
     * @see ExporterMojo#getComponentProperty(String)
     */
    public String getComponentProperty(String key) {
        return getComponentProperty(key, null);
    }

    /**
     * @see ExporterMojo#getComponentProperty(String,boolean)
     */
    public boolean getComponentProperty(String key, boolean defaultValue) {
        String s = getComponentProperty(key);
        if (s == null) {
            return defaultValue;
        } else {
            //noinspection UnnecessaryUnboxing
            return Boolean.valueOf(s).booleanValue();
        }
    }

    /**
     * @see ExporterMojo#getProject()
     */
    public MavenProject getProject() {
        return project;
    }

// --------------------- Interface Mojo ---------------------

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute() throws MojoExecutionException, MojoFailureException {

        Thread currentThread = Thread.currentThread();
        ClassLoader oldClassLoader = currentThread.getContextClassLoader();

        try {
            currentThread.setContextClassLoader(getClassLoader());
            if (getComponentProperty("skip", false)) {
                getLog().info("skipping plugin execution");
            } else {
                doExecute();
            }
        }
        finally {
            currentThread.setContextClassLoader(oldClassLoader);
        }
    }

    /**
     * Adds a default goal.
     *
     * @param outputDirectory Default output directory
     * @param implementation  Default configuration implementation
     * @param jdk5            Is this goal being setup for jdk15?
     * @noinspection unchecked
     */
    public void addDefaultComponent(String outputDirectory, String implementation, boolean jdk5) {
        Component component = new Component();
        component.setName(getName());
        component.setOutputDirectory(outputDirectory);
        component.setImplementation(implementation);
        defaultComponents.put((jdk5) ? "jdk15" : "jdk14", component);
    }

    /**
     * Configures the Exporter.
     *
     * @param exporter Exporter to configure
     * @return Exporter
     * @throws MojoExecutionException if there is an error configuring the exporter
     * @noinspection unchecked
     */
    protected Exporter configureExporter(Exporter exporter) throws MojoExecutionException {
        String implementation = getComponentProperty("implementation", getComponent().getImplementation());

        ComponentConfiguration componentConfiguration = getComponentConfiguration(implementation);
        getLog().debug("using " + componentConfiguration.getName() + " task.");

        Properties properties = new Properties();
        properties.putAll(componentProperties);

        exporter.setProperties(properties);
        exporter.setConfiguration(componentConfiguration.getConfiguration(this));
        exporter.setOutputDirectory(new File(getComponent().getOutputDirectory()));

        return exporter;
    }

    /**
     * @see ExporterMojo#getComponentProperty(String,String)
     */
    public String getComponentProperty(String key, String defaultValue) {
        String value = componentProperties.get(key);
        if (value == null || "".equals(value.trim())) {
            return defaultValue;
        }
        return value;
    }

    /**
     * Returns the ComponentConfiguration for this maven goal.
     *
     * @param name Configuration task name
     * @return ComponentConfiguration
     * @throws MojoExecutionException if there is an error finding the ConfigurationTask
     * @noinspection ForLoopReplaceableByForEach
     */
    protected ComponentConfiguration getComponentConfiguration(String name) throws MojoExecutionException {
        for (Iterator<ComponentConfiguration> it = componentConfigurations.iterator(); it.hasNext();) {
            ComponentConfiguration componentConfiguration = it.next();
            if (componentConfiguration.getName().equals(name)) {
                return componentConfiguration;
            }
        }
        throw new MojoExecutionException("Could not get ConfigurationTask.");
    }

    /**
     * Gets the hibernate tool exporter based on the goal that is being called.
     *
     * @return Goal exporter
     */
    protected abstract Exporter createExporter();

    /**
     * Executes the plugin in an isolated classloader.
     *
     * @throws MojoExecutionException When there is an erro executing the plugin
     */
    protected void doExecute() throws MojoExecutionException {
        configureExporter(createExporter()).start();
    }

    /**
     * Returns the an isolated classloader.
     *
     * @return ClassLoader
     * @noinspection unchecked
     */
    private ClassLoader getClassLoader() {
        try {
            List classpathElements = project.getCompileClasspathElements();
            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());
            URL urls[] = new URL[classpathElements.size()];
            for (int i = 0; i < classpathElements.size(); ++i) {
                urls[i] = new File((String) classpathElements.get(i)).toURL();
            }
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        }
        catch (Exception e) {
            getLog().debug("Couldn't get the classloader.");
            return this.getClass().getClassLoader();
        }
    }

    /**
     * Returns the parsed goal to the exporter.
     *
     * @return Component
     * @noinspection ForLoopReplaceableByForEach
     */
    protected Component getComponent() {
        Component defaultGoal = defaultComponents.get(HibernateUtils.getJavaVersion());
        if (!components.isEmpty()) {
            for (Iterator<Component> it = components.iterator(); it.hasNext();) {
                Component component = it.next();
                if (getName().equals(component.getName())) {
                    if (component.getImplementation() == null) {
                        component.setImplementation(defaultGoal.getImplementation());
                    }
                    if (component.getOutputDirectory() == null) {
                        component.setOutputDirectory(defaultGoal.getOutputDirectory());
                    }
                    return component;
                }
            }
        }
        return defaultGoal;
    }

    public Map<String, String> getComponentProperties() {
        return componentProperties;
    }

    // Allow setting project from tests (AbstractAppFuseMojoTestCase)
    void setProject(MavenProject project) {
        this.project = project;
    }
}