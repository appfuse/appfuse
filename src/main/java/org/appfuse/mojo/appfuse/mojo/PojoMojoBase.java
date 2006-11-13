package org.appfuse.mojo.appfuse.mojo;

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

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.appfuse.mojo.appfuse.utility.AntUtilities;
import org.appfuse.mojo.appfuse.utility.FileUtilities;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.POJOExporter;

/**
 * This class will serve as a base class for all mojos that process java objects using freemarker and the hbm.xml file.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 */
public abstract class PojoMojoBase extends MojoBase
{
    /**
     * Creates a new PojoMojoBase object.
     */
    protected PojoMojoBase()
    {
        super();
        this.setMojoName( "PojoMojoBase" );
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
            getLog().info( "Running the " + this.getMojoName() + " mojo with properties " + this );
        }

        // Get a Hibernate Mapping Exporter
        POJOExporter exporter = new POJOExporter();
        // Allow the class to set, update or delete property settings before they are handed into the exporter. We use
        // the passed back property set so that the original on remains intact.
        Properties updatedProperties = null;
        // Make sure we have a properties object.
        if ( this.getProcessingProperties() == null )
        {
            updatedProperties = new Properties();
        }
        else
        {
            updatedProperties = this.getProcessingProperties();
        }
        // Allow the mojo to modify the properties
        validateProperties( updatedProperties );

        // Set any custom properties that might have been passed in.
        exporter.setProperties( updatedProperties );

        // call create to set up the configuration.
        Configuration configuration = new Configuration();

        List files = this.getListOfFilesToProcess();
        getLog().info( "Processing " + files.size() + " files based on pattern match " );

        for ( int j = 0; j < files.size(); j++ )
        {
            String fileName = ( String ) files.get( j );

            if ( getLog().isDebugEnabled() )
            {
                getLog().debug( "Adding file " + fileName + " to the processing list" );
            }

            configuration.addFile( fileName );
        }

        // set the configuratino into the exporter
        exporter.setConfiguration( configuration );

        // Set the destination directory
        if ( ( this.getOutputDirectory() != null ) && ( getOutputDirectory().length() > 0 ) )
        {
            exporter.setOutputDirectory( new File( getOutputDirectory() ) );
        }
        else
        {
            throw new MojoExecutionException( "output directory cannot be null or empty" );
        }

        // Set the file pattern model for the ouput files
        exporter.setFilePattern( getOutputPattern() );

        // Set the template information.
        exporter.setTemplateName( getTemplateName() );

        // run the exporter.
        exporter.start();
    }

    /**
     * This method is called before properties are sent to the exporter for processing. This gives the class a chance to
     * review the properties that were passed in by the user and modify, delete or update them to match what is required
     * by the particular mojo. These properties are passed in to the generator and are accessable within the processing
     * templates. The new property list is handed back so the original one is still intact.
     * 
     * @param inProperties
     *            The project properties that already have been populated.
     * 
     */
    protected abstract void validateProperties( final Properties inProperties );

    /**
     * Getter for property template name to be used in processing the mojo. An example might be appfusepojo/Pojo.ftl. By
     * implementing this getter the mojo can use a unique name to indentify their template in the configuration file
     * without intefereing with this generic method.
     * 
     * @return The value of template name.
     */
    protected abstract String getTemplateName();

    /**
     * Getter for the file output pattern to be used when generating output files. An example might be like
     * com/appfuse/servicekit/manager/{class-name}Manager.java. By implementing this getter the mojo can use a unique
     * name to indentify their template in the configuration file without intefereing with this generic method.
     * 
     * @return The value of output pattern.
     */
    public abstract String getOutputPattern();

    /**
     * This method will look in the mojo properties to see if the user passed in a new template name and if so pass that
     * back otherwise it passes the default back to the caller.
     * 
     * @param inDefaultTemplateName
     *            The name of the default template.
     * @param inTemplatePropertyKey
     *            The key in the property map to look for a new template name possibly passed in by the user on the
     *            maven properties.
     * @return The name of the template to use.
     */
    public String locateTemplate( final String inDefaultTemplateName, final String inTemplatePropertyKey )
    {
        // See if a template name was passed in otherwise use the default.
        String templateName = inDefaultTemplateName;

        // See if there is a model package extension in the properties
        if ( this.getProcessingProperties() != null
                        && this.getProcessingProperties().containsKey( inTemplatePropertyKey ) )
        {
            templateName = ( String ) this.getProcessingProperties().get( inTemplatePropertyKey );
        }
        return templateName;
    }

    /**
     * This method will build up the output pattern for a generated artifact. It will locate the output pattern and
     * combine that with the package name to build up a location (dot notation) and then convert that location to a file
     * location and pass that back to the caller.
     * 
     * @param inDefaultOutputPattern
     *            The default output pattern for the artifact in question.
     * @param inOutputPatternPropertyKey
     *            The property key to use to see if the user passed it in with the maven properties for the project.
     * @param inPackageName
     *            The package name to prepend to the output pattern to construct the full location.
     * @return The file location of the output for this set of artifacts.
     */
    public String buildOutputPattern( final String inDefaultOutputPattern, final String inOutputPatternPropertyKey,
                                      final String inPackageName )
    {
        // See if an output pattern was passed in otherwise use the default.
        String outputPattern = inDefaultOutputPattern;

        // See if there is a model package extension in the properties
        if ( this.getProcessingProperties() != null
                        && this.getProcessingProperties().containsKey( inOutputPatternPropertyKey ) )
        {
            outputPattern = ( String ) this.getProcessingProperties().get( inOutputPatternPropertyKey );
        }

        // We need to build up the entire file pattern from the package name and the file pattern
        String packageLocation = FileUtilities.convertPackageNameToFileLocation( inPackageName );
        // Combine the package name and the file pattern
        String pattern = packageLocation + "/" + outputPattern;
        return pattern;
    }

    /**
     * This method will create a full package name for an artifact based on the base package name and an extension and
     * will return the full package name for the artifact.
     * 
     * @param inDefaultPackageExtension
     *            The default package extension to use for this artifact.
     * @param inPackageExtensionPropertyKey
     *            The key in the maven properties file where a user can override the default value.
     * @return The full package name for the artifact.
     */
    public String buildPackageName( final String inDefaultPackageExtension, final String inPackageExtensionPropertyKey )
    {
        String packageExtension = inDefaultPackageExtension;
        // See if there is a dao package extension in the properties
        if ( this.getProcessingProperties() != null
                        && this.getProcessingProperties().containsKey( inPackageExtensionPropertyKey ) )
        {
            packageExtension = ( String ) this.getProcessingProperties().get( inPackageExtensionPropertyKey );
        }
        return this.getBasePackageName() + "." + packageExtension;
    }

    /**
     * This class will locate the helper class to be used to provide additional support inside the template environment.
     * This clas has a default that can be overriden by the user but the class must always extend the appfuse base
     * class.
     * 
     * @return The name of the template helper class.
     */
    public String getTemplateHelperClassName()
    {
        // See if a class name was passed in otherwise use the default.
        String className = org.appfuse.mojo.appfuse.utility.AppFuseProperties.DEFAULT_TEMPLATE_HELPER_CLASS;

        // See if there is a model package extension in the properties
        if ( this.getProcessingProperties() != null
                        && this.getProcessingProperties().containsKey(
                                                                       org.appfuse.mojo.appfuse.utility.AppFuseProperties.TEMPLATE_HELPER_CLASS_PROPERTY_KEY ) )
        {
            className =
                ( String ) this.getProcessingProperties().get( org.appfuse.mojo.appfuse.utility.AppFuseProperties.TEMPLATE_HELPER_CLASS_PROPERTY_KEY );
        }
        return className;
    }

    /**
     * This method takes the ant based file pattern and adds the proper suffix onto it for the type of processing the
     * mojo needs to do. The default is to add hbm.xml to the end however if you need a different suffix or need to
     * manipulate the pattern in some way you can overload this method.
     * 
     * @param inFilePattern
     *            The initial file pattern requested for processing.
     * @return An augmented file pattern with the type hbm.xml added to it.
     */
    public String augmentFilePattern( final String inFilePattern )
    {
        return inFilePattern + ".hbm.xml";
    }

    /**
     * This method will use the file pattern or a default file pattern appended with a suffix specific to the mojo to
     * locate all the files that need to be processed by the mojo.
     * 
     * @return A list of fully qualified filename strings to be processed based on the file pattern and suffix.
     */
    public List getListOfFilesToProcess()
    {
        // If no file pattern set then use the default
        String pattern = this.getFilePattern();
        if ( ( pattern == null ) || ( pattern.length() == 0 ) )
        {

            String defaultFilePattern = "**/*";
            getLog().info(
                           "The attribute filePattern for this mojo was not set so using default value of "
                                           + defaultFilePattern );
            pattern = defaultFilePattern;

        }
        // Call the method to allow the mojo to add the proper suffix onto the pattern.
        pattern = augmentFilePattern( pattern );
        getLog().info( "Using final file pattern of " + pattern + " to process the data" );

        // get the list of files to process based on the list of patterns
        List files = AntUtilities.generateFileNameListFromPattern( this.getModelDirectory(), pattern );

        return files;
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
        buffer.append( "PojoMojoBase[" );
        buffer.append( "templateName = " ).append( this.getTemplateName() );
        buffer.append( ", outputPattern = " ).append( this.getOutputPattern() );
        buffer.append( "]" );
        return buffer.toString();
    }
}
