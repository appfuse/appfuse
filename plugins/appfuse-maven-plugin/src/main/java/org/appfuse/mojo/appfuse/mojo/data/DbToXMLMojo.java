package org.appfuse.mojo.appfuse.mojo.data;

/*
 * Copyright 2006 The Apache Software Foundation.
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
import org.appfuse.mojo.appfuse.mojo.MojoBase;
import org.appfuse.mojo.appfuse.utility.FileUtilities;
import org.appfuse.mojo.appfuse.utility.JDBCConfigurationUtility;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.HibernateMappingExporter;

import java.io.File;
import java.util.Properties;

/**
 * This mojo class will reverse engineer a set of hbm.xml files from a database connection.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 * @description Generate xml files from a database.
 * @goal dbtoxml
 */
public class DbToXMLMojo extends MojoBase
{

    /**
     * The full name of the location of the hibernate format database properties file.
     * 
     * @parameter expression="${basedir}/src/main/resources/database.properties"
     */
    private String databasePropertiesFile;

    /**
     * The location and name of the file that defines the attrbutes and other data that will be used to control the
     * reverse engineering process. This file controls such things as the schema and table names that will be reverse
     * engineered.
     * 
     * @parameter expression = "${basedir}/src/main/resources/hibernate.reveng.xml"
     */
    private String reverseEngineeringConfigurationFile;

    /**
     * The full package and classname for any additional ReverseEngineeringDelegator class that might be used to alter
     * the way the reverse engineering might be handled. A sample is included in the code for this plugin and to use
     * that class you would set this value to org.codehaus.mojo.appfuse.reveng.AppFuseReverseEngineeringDelegator.
     * 
     * @parameter default-value=""
     */
    private String reverseStrategyClass;

    /**
     * The name of the freemarker template that will be used to reverse engineer the hbm files from the database.
     * 
     * @parameter default-value="hbm/hibernate-mapping.hbm.ftl"
     */
    private String hbmTemplateName;

    /**
     * Creates a new DbToXMLMojo object.
     */
    public DbToXMLMojo()
    {
        super();
        this.setMojoName( "DbToXMLMojo" );
    }

    /**
     * Getter for property hbm template name.
     * 
     * @return The value of hbm template name.
     */
    public String getHbmTemplateName()
    {
        return this.hbmTemplateName;
    }

    /**
     * Setter for the hbm template name.
     * 
     * @param inHbmTemplateName
     *            The value of hbm template name.
     */
    public void setHbmTemplateName( final String inHbmTemplateName )
    {
        this.hbmTemplateName = inHbmTemplateName;
    }

    /**
     * Getter for property database properties file.
     * 
     * @return The value of database properties file.
     */
    public String getDatabasePropertiesFile()
    {
        return this.databasePropertiesFile;
    }

    /**
     * Setter for the database properties file.
     * 
     * @param inDatabasePropertiesFile
     *            The value of database properties file.
     */
    public void setDatabasePropertiesFile( final String inDatabasePropertiesFile )
    {
        this.databasePropertiesFile = inDatabasePropertiesFile;
    }

    /**
     * Getter for property reverse engineering configuration file.
     * 
     * @return The value of reverse engineering configuration file.
     */
    public String getReverseEngineeringConfigurationFile()
    {
        return this.reverseEngineeringConfigurationFile;
    }

    /**
     * Setter for the reverse engineering configuration file.
     * 
     * @param inReverseEngineeringConfigurationFile
     *            The value of reverse engineering configuration file.
     */
    public void setReverseEngineeringConfigurationFile( final String inReverseEngineeringConfigurationFile )
    {
        this.reverseEngineeringConfigurationFile = inReverseEngineeringConfigurationFile;
    }

    /**
     * Getter for property reverse strategy class.
     * 
     * @return The value of reverse strategy class.
     */
    public String getReverseStrategyClass()
    {
        return this.reverseStrategyClass;
    }

    /**
     * Setter for the reverse strategy class.
     * 
     * @param inReverseStrategyClass
     *            The value of reverse strategy class.
     */
    public void setReverseStrategyClass( final String inReverseStrategyClass )
    {
        this.reverseStrategyClass = inReverseStrategyClass;
    }

    /**
     * This method will run the database conversion to hbm file mojo task.
     * 
     * @throws MojoExecutionException
     *             Thrown if we fail to obtain an appfuse resource.
     */
    public void execute() throws MojoExecutionException
    {

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( "Running the " + this.getMojoName() + " Mojo with properties " + this );
        }

        // Get a Hibernate Mapping Exporter
        HibernateMappingExporter exporter = new HibernateMappingExporter();
        Properties properties = new Properties();

        // Set any custom properties that might have been passed in.
        exporter.setProperties( properties );

        // create a new JDBC configuration object.
        JDBCConfigurationUtility configurationUtility = new JDBCConfigurationUtility();

        // call create to set up the configuration.
        Configuration configuration = configurationUtility.createConfiguration();

        // Set some parameters in the configuration class.
        if ( ( this.getModelPackageName() != null ) && ( this.getModelPackageName().length() > 0 ) )
        {
            configurationUtility.setPackageName( this.getModelPackageName() );
        }
        else
        {
            throw new MojoExecutionException( "Model package name cannot be null or empty" );
        }

        if ( ( this.getDatabasePropertiesFile() != null ) && ( this.getDatabasePropertiesFile().length() > 0 ) )
        {
            // TODO - temporary hack to resolve database properties issue
            //configurationUtility.setPropertyFile( new File( this.getDatabasePropertiesFile() ) );
            configurationUtility.setPropertyFile(FileUtilities.getDatabaseProperties());
        }
        else
        {
            throw new MojoExecutionException( "Hibernate properties file cannot be null or empty" );
        }

        // Load the reverse engineering configuration xml files.
        if ( ( this.getReverseEngineeringConfigurationFile() == null )
                        || ( this.getReverseEngineeringConfigurationFile().length() == 0 ) )
        {
            throw new MojoExecutionException( "There must be at least one reverse engineering xml file defined" );
        }

        configurationUtility.addRevEngFile( this.getReverseEngineeringConfigurationFile() );

        // Set the reverse engineering strategy class
        if ( ( this.getReverseStrategyClass() != null ) && ( this.getReverseStrategyClass().length() > 0 ) )
        {
            configurationUtility.setReverseStrategy( this.getReverseStrategyClass() );
        }

        // complete the configuration processing
        configurationUtility.doConfiguration( configuration );
        configuration.buildMappings();

        // set the configurator into the exporter
        exporter.setConfiguration( configuration );

        // Set the destination directory
        if ( ( this.getOutputDirectory() != null ) && ( this.getOutputDirectory().length() > 0 ) )
        {
            exporter.setOutputDirectory( new File( this.getOutputDirectory() ) );
        }
        else
        {
            throw new MojoExecutionException( "Output directory cannot be null or empty" );
        }

        // Set the template information.
        exporter.setTemplateName( this.getHbmTemplateName() );

        // run the exporter.
        exporter.start();
    }

    /**
     * toString methode: creates a String representation of the object
     * 
     * @return the String representation
     * 
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( "DbToXMLMojo[" );
        buffer.append( "databasePropertiesFile = " ).append( databasePropertiesFile );
        buffer.append( ", reverseEngineeringConfigurationFile = " ).append( reverseEngineeringConfigurationFile );
        buffer.append( ", reverseStrategyClass = " ).append( reverseStrategyClass );
        buffer.append( ", hbmTemplateName = " ).append( hbmTemplateName );
        buffer.append( "]" );
        return buffer.toString();
    }
}
