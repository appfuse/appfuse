package org.appfuse.mojo.tasks;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

import org.appfuse.mojo.appfuse.utility.AntUtilities;

import org.hibernate.tool.ant.Hbm2CfgXmlExporterTask;
import org.hibernate.tool.ant.Hbm2JavaExporterTask;
import org.hibernate.tool.ant.HibernateToolTask;
import org.hibernate.tool.ant.JDBCConfigurationTask;

import java.io.File;


/**
 * This class is the class that is used to drive the generation of the model pojo objects from a set
 * of database tables. It is built as a task to seperate it from the maven struture so it can be
 * reused by other java tasks or Ant tasks. It is currently Ant based but in the future may change
 * to a more Java based approach.
 *
 * @author   <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version  $Id: $
 */
public class GenerateModelTask extends GenerationTaskBase
{
    /**
     * The Hibernate 3 Tools reverse engineering file used to control the reverse engineering of the
     * database tables in to pojos.
     */
    private String reverseEngineeringFile;

    /** The file that describes the database connection properties for the tool to use. */
    private String databasePropertiesFile;

    /** The name of the package to use for the generated objects. */
    private String packageName;

    /** If set to true the hibernate.cfg.xml file will be generated. */
    private boolean generateConfigurationFile;

    /** The full name of the reverse strategy class. */
    private String reverseStrategyClassName;

    /** The full name of the naming strategy class. */
    private String namingStrategyClassName;

    /** The full name of the entity resolver class. */
    private String entityResolverClassName;

    /**
     * Creates a new GenerateModelTask object.
     */
    public GenerateModelTask()
    {
        super();
    }

    /**
     * This method will perform the actual reverse engineering of the database into a set of model
     * pojo objects.
     */
    public void execute()
    {
        // Create an Ant project
        Project antProject = AntUtilities.createProject();

        // Create the hibernate tool task.
        HibernateToolTask toolTask = new HibernateToolTask();

        // Set the output directory for all tasks and sub tasks
        toolTask.setDestDir(new File(this.getOutputDirectory()));

        // associate the project with the tooltask
        toolTask.setProject(antProject);

        // Configure the JDBC Configuration
        this.configureJDBC(toolTask);

        // Configure the hbmtojava task
        this.configureHbmToJava(toolTask);

        if (this.generateConfigurationFile)
        {
            // For now generate the hbm.cfg.xml file.
            this.configureHbm2CfgXml(toolTask);
        }

        // Now execute the task
        toolTask.execute();
    }

    /**
     * This method will generate the hibernate.cfg.xml file for use later in the annotation based
     * generation process.
     *
     * @param  inToolTask  The tool task that is going to run this task.
     */
    private void configureHbm2CfgXml(HibernateToolTask inToolTask)
    {
        Hbm2CfgXmlExporterTask exporterTask = (Hbm2CfgXmlExporterTask) inToolTask
            .createHbm2CfgXml();
        exporterTask.setEjb3(this.isEjb3());
    }

    /**
     * This method will configure the HBM To java task to create the model pojo objects.
     *
     * @param  inToolTask  The tool task that is going to run this task.
     */
    private void configureHbmToJava(HibernateToolTask inToolTask)
    {
        // Get a hbmtojava task and configure it
        Hbm2JavaExporterTask exporterTask = (Hbm2JavaExporterTask) inToolTask.createHbm2Java();
        exporterTask.setEjb3(this.isEjb3());
        exporterTask.setJdk5(this.isJdk5());
    }

    /**
     * This method will configure the JDBC task used to connect to the database and reverse engineer
     * the data into a set of java pojos.
     *
     * @param  inToolTask  The tool task that is going to run this task.
     */
    private void configureJDBC(final HibernateToolTask inToolTask)
    {
        // Get a jdbc configuration task for this tool task
        JDBCConfigurationTask jdbcConfigurationTask = inToolTask.createJDBCConfiguration();

        // configure the task with our properties
        // If there is a reverse engineering file then use it.
        if ((this.getReverseEngineeringFile() != null) &&
                (this.getReverseEngineeringFile().length() > 0))
        {
            Path revEngPath = new Path(inToolTask.getProject(), this.getReverseEngineeringFile());

            // Set the path to the reveng file
            jdbcConfigurationTask.setRevEngFile(revEngPath);
        }

        if ((this.getReverseStrategyClassName() != null) &&
                (this.getReverseStrategyClassName().length() > 0))
        {
            jdbcConfigurationTask.setReverseStrategy(this.getReverseStrategyClassName());
        }

        if ((this.getNamingStrategyClassName() != null) &&
                (this.getNamingStrategyClassName().length() > 0))
        {
            jdbcConfigurationTask.setNamingStrategy(this.getNamingStrategyClassName());
        }

        if ((this.getEntityResolverClassName() != null) &&
                (this.getEntityResolverClassName().length() > 0))
        {
            jdbcConfigurationTask.setEntityResolver(this.getEntityResolverClassName());
        }

        // set the path to the database properties file
        jdbcConfigurationTask.setPropertyFile(new File(this.getDatabasePropertiesFile()));

        // Set the model package name
        jdbcConfigurationTask.setPackageName(this.getPackageName());
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
     * Getter for property database properties file.
     *
     * @return  The value of database properties file.
     */
    public String getDatabasePropertiesFile()
    {
        return this.databasePropertiesFile;
    }

    /**
     * Setter for the database properties file.
     *
     * @param  inDatabasePropertiesFile  The value of database properties file.
     */
    public void setDatabasePropertiesFile(final String inDatabasePropertiesFile)
    {
        this.databasePropertiesFile = inDatabasePropertiesFile;
    }

    /**
     * Getter for property reverse engineering file.
     *
     * @return  The value of reverse engineering file.
     */
    public String getReverseEngineeringFile()
    {
        return this.reverseEngineeringFile;
    }

    /**
     * Setter for the reverse engineering file.
     *
     * @param  inReverseEngineeringFile  The value of reverse engineering file.
     */
    public void setReverseEngineeringFile(final String inReverseEngineeringFile)
    {
        this.reverseEngineeringFile = inReverseEngineeringFile;
    }

    /**
     * Getter for property generate configuration file.
     *
     * @return  The value of generate configuration file.
     */
    public boolean isGenerateConfigurationFile()
    {
        return this.generateConfigurationFile;
    }

    /**
     * Setter for the generate configuration file.
     *
     * @param  inGenerateConfigurationFile  The value of generate configuration file.
     */
    public void setGenerateConfigurationFile(final boolean inGenerateConfigurationFile)
    {
        this.generateConfigurationFile = inGenerateConfigurationFile;
    }

    /**
     * Getter for property entity resolver class name.
     *
     * @return  The value of entity resolver class name.
     */
    public String getEntityResolverClassName()
    {
        return this.entityResolverClassName;
    }

    /**
     * Setter for the entity resolver class name.
     *
     * @param  inEntityResolverClassName  The value of entity resolver class name.
     */
    public void setEntityResolverClassName(final String inEntityResolverClassName)
    {
        this.entityResolverClassName = inEntityResolverClassName;
    }

    /**
     * Getter for property naming strategy class name.
     *
     * @return  The value of naming strategy class name.
     */
    public String getNamingStrategyClassName()
    {
        return this.namingStrategyClassName;
    }

    /**
     * Setter for the naming strategy class name.
     *
     * @param  inNamingStrategyClassName  The value of naming strategy class name.
     */
    public void setNamingStrategyClassName(final String inNamingStrategyClassName)
    {
        this.namingStrategyClassName = inNamingStrategyClassName;
    }

    /**
     * Getter for property reverse strategy class name.
     *
     * @return  The value of reverse strategy class name.
     */
    public String getReverseStrategyClassName()
    {
        return this.reverseStrategyClassName;
    }

    /**
     * Setter for the reverse strategy class name.
     *
     * @param  inReverseStrategyClassName  The value of reverse strategy class name.
     */
    public void setReverseStrategyClassName(final String inReverseStrategyClassName)
    {
        this.reverseStrategyClassName = inReverseStrategyClassName;
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
        buffer.append("GenerateModelTask[");
        buffer.append("databasePropertiesFile = ").append(databasePropertiesFile);
        buffer.append("\n entityResolverClassName = ").append(entityResolverClassName);
        buffer.append("\n generateConfigurationFile = ").append(generateConfigurationFile);
        buffer.append("\n namingStrategyClassName = ").append(namingStrategyClassName);
        buffer.append("\n packageName = ").append(packageName);
        buffer.append("\n reverseEngineeringFile = ").append(reverseEngineeringFile);
        buffer.append("\n reverseStrategyClassName = ").append(reverseStrategyClassName);
        buffer.append("]");

        return buffer.toString();
    }
}
