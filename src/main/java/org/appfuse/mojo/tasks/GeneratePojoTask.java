package org.appfuse.mojo.tasks;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Environment;
import org.apache.tools.ant.types.Path;

import org.appfuse.mojo.appfuse.utility.AntUtilities;
import org.appfuse.mojo.appfuse.utility.FileUtilities;

import org.hibernate.tool.ant.AnnotationConfigurationTask;
import org.hibernate.tool.ant.GenericExporterTask;
import org.hibernate.tool.ant.HibernateToolTask;

import java.io.File;

import java.util.Enumeration;
import java.util.Properties;


/**
 * This class is the class that is used to drive the generation of the pojo objects from a set of
 * java 5 annotated model objects. It is built as a task to seperate it from the maven struture so
 * it can be reused by other java tasks or Ant tasks. It is currently Ant based but in the future
 * may change to a more Java based approach.
 *
 * @author   <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version  $Id: $
 */
public class GeneratePojoTask extends GenerationTaskBase
{
    /** The name of the class to use as an exporter. */
    protected static final String EXPORTER_CLASSNAME = "org.hibernate.tool.hbm2x.POJOExporter";

    /** The properties that will be passed into the freemarker template. */
    private Properties templateProperties;

    /** The name of the template to use to generate the output. */
    private String templateName;

    /** The file pattern for the output files. */
    private String outputFilePattern;

    /** The file pattern used to determine the files to process. */
    private String inputFilePattern;

    /** The name of the package for the generated file. */
    private String packageName;

    /**
     * If this is set it will be used to drive the generation otherwise the pattern will be used.
     */
    private String hibernateConfigurationFile;

    /**
     * Creates a new GenerateModelTask object.
     */
    public GeneratePojoTask()
    {
        super();
    }

    /**
     * This method will run a template against a set of annotated model objects and produce a set of
     * pojos.
     */
    public void execute()
    {
        // Create an Ant project
        Project antProject = AntUtilities.createProject();

        // Create the hibernate tool task.
        HibernateToolTask toolTask = new HibernateToolTask();

        // associate the project with the tooltask
        toolTask.setProject(antProject);

        // Configure the output directory
        toolTask.setDestDir(new File(this.getOutputDirectory()));

        // Add the classpath for the output model classes which are harvested for their annotations.
        Path newPath = new Path(antProject);
        newPath.setLocation(new File(this.getOuputClassDirectory()));
        toolTask.setClasspath(newPath);

        // Configure the Annotation Configuration
        this.configureAnnotation(toolTask);

        // Configure the hbm to Java task
        this.configureHbm2Java(toolTask);

        // Now execute the task
        toolTask.execute();
    }

    /**
     * This method will configure the HBM To java task to create the pojo objects.
     *
     * @param  inToolTask  The tool task that is going to run this task.
     */
    private void configureHbm2Java(final HibernateToolTask inToolTask)
    {
        // Get a hbm template which is actually just a generic exporter task and configure it with a
        // pojo exporter  so we can access class stuff internally.
        GenericExporterTask exporterTask = (GenericExporterTask) inToolTask.createHbmTemplate();
        exporterTask.setExporterClass(GeneratePojoTask.EXPORTER_CLASSNAME);

        // The file pattern will be the package name combined with the file pattern
        String packageLocation = FileUtilities.convertPackageNameToFileLocation(this
                .getPackageName());
        exporterTask.setFilePattern(packageLocation + File.separator + this.getOutputFilePattern());
        exporterTask.setTemplate(this.getTemplateName());
        loadProperties(exporterTask, this.getTemplateProperties());
    }

    /**
     * This method will load a set of properties into the exporter so they can be accessed inside of
     * a freemarker template.
     *
     * @param  inExporterTask  The exporter task that contains the exporter to be used.
     * @param  inProperties    The set of properties to be made availble inside the freemarker
     *                         template.
     */
    protected void loadProperties(final GenericExporterTask inExporterTask,
        final Properties inProperties)
    {
        Enumeration names = inProperties.propertyNames();

        while (names.hasMoreElements())
        {
            String propertyName = (String) names.nextElement();
            String property = (String) inProperties.get(propertyName);
            Environment.Variable exporterProperty = new Environment.Variable();
            exporterProperty.setKey(propertyName);
            exporterProperty.setValue(property);
            inExporterTask.addConfiguredProperty(exporterProperty);
        }
    }

    /**
     * This method will configure the Annotation task used to generate pojos from annotated model
     * objects.
     *
     * @param  inToolTask  The tool task that is going to run this task.
     */
    private void configureAnnotation(final HibernateToolTask inToolTask)
    {
        // Get a annotation configuration task for this tool task
        AnnotationConfigurationTask annotationConfigurationTask = inToolTask
            .createAnnotationConfiguration();
        annotationConfigurationTask.setConfigurationFile(new File(
                this.getHibernateConfigurationFile()));
        annotationConfigurationTask.setProject(inToolTask.getProject());
    }

    /**
     * Getter for property template name.
     *
     * @return  The value of template name.
     */
    public String getTemplateName()
    {
        return this.templateName;
    }

    /**
     * Setter for the template name.
     *
     * @param  inTemplateName  The value of template name.
     */
    public void setTemplateName(final String inTemplateName)
    {
        this.templateName = inTemplateName;
    }

    /**
     * Getter for property template properties.
     *
     * @return  The value of template properties.
     */
    public Properties getTemplateProperties()
    {
        return this.templateProperties;
    }

    /**
     * Setter for the template properties.
     *
     * @param  inTemplateProperties  The value of template properties.
     */
    public void setTemplateProperties(final Properties inTemplateProperties)
    {
        this.templateProperties = inTemplateProperties;
    }

    /**
     * Getter for property input file pattern.
     *
     * @return  The value of input file pattern.
     */
    public String getInputFilePattern()
    {
        return this.inputFilePattern;
    }

    /**
     * Setter for the input file pattern.
     *
     * @param  inInputFilePattern  The value of input file pattern.
     */
    public void setInputFilePattern(final String inInputFilePattern)
    {
        this.inputFilePattern = inInputFilePattern;
    }

    /**
     * Getter for property output file pattern.
     *
     * @return  The value of output file pattern.
     */
    public String getOutputFilePattern()
    {
        return this.outputFilePattern;
    }

    /**
     * Setter for the output file pattern.
     *
     * @param  inOutputFilePattern  The value of output file pattern.
     */
    public void setOutputFilePattern(final String inOutputFilePattern)
    {
        this.outputFilePattern = inOutputFilePattern;
    }

    /**
     * Getter for property hibernate configuration file.
     *
     * @return  The value of hibernate configuration file.
     */
    public String getHibernateConfigurationFile()
    {
        return this.hibernateConfigurationFile;
    }

    /**
     * Setter for the hibernate configuration file.
     *
     * @param  inHibernateConfigurationFile  The value of hibernate configuration file.
     */
    public void setHibernateConfigurationFile(final String inHibernateConfigurationFile)
    {
        this.hibernateConfigurationFile = inHibernateConfigurationFile;
    }

    /**
     * Getter for property package name.
     *
     * @return  The value of package name.
     */
    public String getPackageName()
    {
        return this.packageName;
    }

    /**
     * Setter for the package name.
     *
     * @param  inPackageName  The value of package name.
     */
    public void setPackageName(final String inPackageName)
    {
        this.packageName = inPackageName;
    }

    /**
     * This method creates a String representation of this object.
     *
     * @return  the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append("GeneratePojoTask[");
        buffer.append("EXPORTER_CLASSNAME = ").append(EXPORTER_CLASSNAME);
        buffer.append("\n hibernateConfigurationFile = ").append(hibernateConfigurationFile);
        buffer.append("\n inputFilePattern = ").append(inputFilePattern);
        buffer.append("\n outputFilePattern = ").append(outputFilePattern);
        buffer.append("\n packageName = ").append(packageName);
        buffer.append("\n templateName = ").append(templateName);
        buffer.append("\n templateProperties = ").append(templateProperties);
        buffer.append("]");

        return buffer.toString();
    }
}
