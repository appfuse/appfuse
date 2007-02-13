package org.appfuse.mojo.appfuse.mojo;

/*
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

import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * This class is a base class for all appfuse plugin components to use for access to shared resources.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id$
 * @description Base mojo for use by appfuse mojos.
 */
public abstract class MojoBase extends AbstractMojo
{

    public String getDatabasePropertiesFile() {
        return databasePropertiesFile;
    }

    public void setDatabasePropertiesFile(String databasePropertiesFile) {
        this.databasePropertiesFile = databasePropertiesFile;
    }

    public String getReverseEngineeringConfigurationFile() {
        return reverseEngineeringConfigurationFile;
    }

    public void setReverseEngineeringConfigurationFile(String reverseEngineeringConfigurationFile) {
        this.reverseEngineeringConfigurationFile = reverseEngineeringConfigurationFile;
    }

    public String getReverseStrategyClass() {
        return reverseStrategyClass;
    }

    public void setReverseStrategyClass(String reverseStrategyClass) {
        this.reverseStrategyClass = reverseStrategyClass;
    }

    public String getHbmTemplateName() {
        return hbmTemplateName;
    }

    public void setHbmTemplateName(String hbmTemplateName) {
        this.hbmTemplateName = hbmTemplateName;
    }

    /**
     * The full name of the location of the hibernate format database properties file.
     *
     * @parameter expression="${appfuse.database.properties}" default-value="${basedir}/target/classes/jdbc.properties"
     */
    private String databasePropertiesFile;

    /**
     * The location and name of the file that defines the attrbutes and other data that will be used to control the
     * reverse engineering process. This file controls such things as the schema and table names that will be reverse
     * engineered.
     *
     * @parameter expression="${appfuse.reveng.file}" default-value = "${basedir}/src/main/resources/hibernate.reveng.xml"
     */
    private String reverseEngineeringConfigurationFile;

    /**
     * The full package and classname for any additional ReverseEngineeringDelegator class that might be used to alter
     * the way the reverse engineering might be handled. A sample is included in the code for this plugin and to use
     * that class you would set this value to org.codehaus.mojo.appfuse.reveng.AppFuseReverseEngineeringDelegator.
     *
     * @parameter expression="${appfuse.revstrategy.class}" default-value="org.appfuse.mojo.appfuse.reveng.AppFuseReverseEngineeringDelegator"
     */
    private String reverseStrategyClass;

    /**
     * The name of the freemarker template that will be used to reverse engineer the hbm files from the database.
     *
     * @parameter expression="${appfuse.hbm.template}" default-value="hbm/hibernate-mapping.hbm.ftl"
     */
    private String hbmTemplateName;

    /**
     * This is the name of the mojo that is used when outputing logging information for any mojos that extend this based
     * class.
     */
    private String mojoName;

    /**
     * An ant format expression defining which files you would like to process with the mojo. The pattern should not
     * include the suffix as that will be determined by the mojo as to what type of file is to be processed. The default
     * is to process all pojo's. A trick to allow you to dynamically define this on the command line is to assign this
     * value in the pom to a property value such as ${appfuse.file.pattern} Then you can either define a property in a
     * pom or settings.xml file or on the command line like -Dappfuse.file.pattern=**forwardslashUser* and this will
     * process all objects in the that begin with User.
     * 
     * @parameter expression="${appfuse.file.pattern}" default-value = ""
     */
    private String filePattern;

    /**
     * The name of the base package to be used for all object generation.
     * 
     * @parameter expression="${appfuse.base.package}" default-value="org.appfuse"
     */
    private String basePackageName;

    /**
     * This is the location of the directory where the model hbm.xml files will be placed during the copy operation for
     * inclusion in the project.*
     * 
     * @parameter expression="${appfuse.model.directory}" default-value="${basedir}/src/main/resources"
     */
    private String modelDirectory;

    /**
     * This is the location of the directory where the source java files will be placed for inclusion in the project.*
     * 
     * @parameter expression="${appfuse.source.directory}" default-value="${basedir}/src/main/java"
     */
    private String sourceDirectory;

    /**
     * The path where the generated artifacts will be placed. This is intentionally not set to the default location for
     * maven generated sources. This is to keep these files out of the eclipse/idea generated sources directory as the
     * intention is that these files will be copied to a source directory to be edited and modified and not re generated
     * each time the plugin is run. If you want to regenerate the files each time you build the project just set this
     * value to ${basedir}/target/generated-sources or set the flag on eclipse/idea plugin to include this file in your
     * project file as a source directory.
     * 
     * @parameter expression="${appfuse.output.directory}" default-value="${basedir}/target/appfuse/generated-sources"
     */
    private String outputDirectory;

    /**
     * This parameter will indicate how to manage any copy operations that take place in the mojo set. During context
     * creation the master context files will always be updated and the existing entries replaced. When running a copy
     * operation on any of the generated java files this flag will determine what type of copy to execute. If set to
     * true the existing java files in the target directory will be overwritten and if set to false (default) the new
     * files will be added to the target directory but existing files will not be overwritten by new files.
     * 
     * @parameter expression="${appfuse.copy.overwrite}" default-value="false"
     */
    private boolean copyOverWrite;

    /**
     * A properties object passed to the exporter to be influence the object that could be used to process the data or to
     * create objects to be used inside the templates for complex processing. In general the user should never set these
     * properties. The design is to use default over configuration. If you accept the defaults you will generate code
     * that is in alignment with the current appfuse standard including naming and file locations. This capability it
     * provided for advanced developers who understand the appfuse structure and the plugin function and wish to change
     * the default behavour of the plugin.
     * 
     * @parameter expression="${appfuse.processing.properties}" default-value = ""
     */
    private Properties processingProperties;


    /**
     * The destination file used with config mojos.  This parameter purposely has no default because it will be used
     * with -Dparameter most commonly when adding snippets to configuration files specific to a single POJO.
     *
     * @parameter expression="${appfuse.destination.file}" default-value = ""
     */
    private String destinationFile;

    /**
     * The parent Spring configuration file.  While Spring can be configured in multiple files, this parameter is used
     * to add configuration snippets into the configuration file.  This parameter is used with the config mojos and
     * can be overridden if needed.
     * 
     * @parameter expression="${appfuse.application.context.file}"
     * default-value = "${basedir}/src/main/webapp/WEB-INF/applicationContext.xml"
     */
    private String applicationContextFile;


    /**
     * The ANT LoadFile property name.  This parameter is used with the config mojos and will commonly be overridden. It
     * has no default value because it will be used with -Dparameter most commonly when adding snippets to configuration
     * files specific to a single POJO.
     *
     * @parameter expression="${appfuse.config.property}" default-value = ""
     */
    private String configPropertyName;

    /**
     * @parameter expression="${appfuse.config.infile}" default-value = ""
     */
    private String configInFile;
    
    /**
     * This method will run an appfuse mojo.
     * 
     * @throws MojoExecutionException
     *             Thrown if we fail to obtain an appfuse resource.
     */
    public void execute() throws MojoExecutionException
    {

        if ( getLog().isInfoEnabled() )
        {
            getLog().info( "Running the AppFuse Mojo Base without an implementation" );
        }

        throw new MojoExecutionException( "Unimplemented Mojo Base" );
    }

    /**
     * Getter for the configuration property name
     *
     * @return The value for the configuration property name
     */
    public String getConfigPropertyName() {
        return configPropertyName;
    }

    /**
     * Setter for the configuration property name
     *
     * @param configPropertyName
     */
    public void setConfigPropertyName(String configPropertyName) {
        this.configPropertyName = configPropertyName;
    }

    /**
     * Getter for the configuration infile
     *
     * @return The value for the configuration infile
     */
    public String getConfigInFile() {
        return configInFile;
    }

    /**
     * Setter for the configuration infile
     *
     * @param configInFile
     */
    public void setConfigInFile(String configInFile) {
        this.configInFile = configInFile;
    }

    /**
     * Getter for the Spring applicationContext file
     *
     * @return  The value for the Spring applicationContext file
     */
    public String getApplicationContextFile() {
        return applicationContextFile;
    }

    /**
     * Setter for the Spring applicationContext file
     *
     * @param applicationContextFile
     */
    public void setApplicationContextFile(String applicationContextFile) {
        this.applicationContextFile = applicationContextFile;
    }

    /**
     * Getter for property destination file
     *
     * @return The value of destination file
     */
    public String getDestinationFile() {
        return destinationFile;
    }

    /**
     * Setter for the destination file
     *
     * @param destinationFile
     */
    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    /**
     * Getter for property base package name.
     * 
     * @return The value of base package name.
     */
    public String getBasePackageName()
    {
        return this.basePackageName;
    }

    /**
     * Setter for the base package name.
     * 
     * @param inBasePackageName
     *            The value of base package name.
     */
    public void setBasePackageName( final String inBasePackageName )
    {
        this.basePackageName = inBasePackageName;
    }

    /**
     * Getter for property output directory.
     * 
     * @return The value of output directory.
     */
    public String getOutputDirectory()
    {
        return this.outputDirectory;
    }

    /**
     * Setter for the output directory.
     * 
     * @param inOutputDirectory
     *            The value of output directory.
     */
    public void setOutputDirectory( final String inOutputDirectory )
    {
        this.outputDirectory = inOutputDirectory;
    }

    /**
     * Getter for property file pattern.
     * 
     * @return The value of file pattern.
     */
    public String getFilePattern()
    {
        return this.filePattern;
    }

    /**
     * Setter for the file pattern.
     * 
     * @param inFilePattern
     *            The value of file pattern.
     */
    public void setFilePattern( final String inFilePattern )
    {
        this.filePattern = inFilePattern;
    }

    /**
     * Getter for property copy overwrite flag.
     * 
     * @return The value of the copy overwrite flag.
     */
    public boolean getCopyOverWrite()
    {
        return this.copyOverWrite;
    }

    /**
     * Setter for the copy overwrite flag.
     * 
     * @param inCopyOverWrite
     *            The value of the copy overwrite flag.
     */
    public void setCopyOverWrite( final boolean inCopyOverWrite )
    {
        this.copyOverWrite = inCopyOverWrite;
    }

    /**
     * Getter for property mojoName.
     * 
     * @return The value of the mojo name.
     */
    public String getMojoName()
    {
        return this.mojoName;
    }

    /**
     * Setter for the mojo name.
     * 
     * @param inMojoName
     *            The value of the mojo name.
     */
    public void setMojoName( final String inMojoName )
    {
        this.mojoName = inMojoName;
    }

    /**
     * Getter for property source directory.
     * 
     * @return The value of source directory.
     */
    public String getSourceDirectory()
    {
        return this.sourceDirectory;
    }

    /**
     * Setter for the source directory.
     * 
     * @param inSourceDirectory
     *            The value of source directory.
     */
    public void setSourceDirectory( final String inSourceDirectory )
    {
        this.sourceDirectory = inSourceDirectory;
    }

    /**
     * Getter for property model directory.
     * 
     * @return The value of model directory.
     */
    public String getModelDirectory()
    {
        return this.modelDirectory;
    }

    /**
     * Setter for the model directory.
     * 
     * @param inModelDirectory
     *            The value of model directory.
     */
    public void setModelDirectory( final String inModelDirectory )
    {
        this.modelDirectory = inModelDirectory;
    }

    /**
     * Getter for property processing properties.
     * 
     * @return The value of processing properties.
     */
    public Properties getProcessingProperties()
    {
        return this.processingProperties;
    }

    /**
     * Setter for the processing properties.
     * 
     * @param inProcessingProperties
     *            The value of processing properties.
     */
    public void setProcessingProperties( final Properties inProcessingProperties )
    {
        this.processingProperties = inProcessingProperties;
    }

    /**
     * This method will return the full package name to be used for generating output for the model objects.
     * 
     * @return The full package name for all model objects.
     * 
     */
    protected String getModelPackageName()
    {
        String modelPackageExtension = org.appfuse.mojo.appfuse.utility.AppFuseProperties.DEFAULT_MODEL_PACKAGE_EXTENSION;
        // See if there is a model package extension in the properties
        if ( this.getProcessingProperties() != null
                        && this.getProcessingProperties().containsKey(
                                                                       org.appfuse.mojo.appfuse.utility.AppFuseProperties.MODEL_PACKAGE_EXTENSION_PROPERTY_KEY ) )
        {
            modelPackageExtension =
                ( String ) this.getProcessingProperties().get( org.appfuse.mojo.appfuse.utility.AppFuseProperties.MODEL_PACKAGE_EXTENSION_PROPERTY_KEY );
        }
        return this.getBasePackageName() + "." + modelPackageExtension;
    }


    /**
     * toString methode: creates a String representation of the object
     * 
     * @return the String representation
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "MojoBase[" );
        buffer.append( "mojoName = " ).append( mojoName );
        buffer.append( ", filePattern = " ).append( filePattern );
        buffer.append( ", basePackageName = " ).append( basePackageName );
        buffer.append( ", modelDirectory = " ).append( modelDirectory );
        buffer.append( ", sourceDirectory = " ).append( sourceDirectory );
        buffer.append( ", outputDirectory = " ).append( outputDirectory );
        buffer.append( ", copyOverWrite = " ).append( copyOverWrite );
        buffer.append( ", processingProperties = " ).append( processingProperties );
        buffer.append( ", modelPackageName = " ).append( this.getModelPackageName() );
        buffer.append( "]" );
        return buffer.toString();
    }

}
