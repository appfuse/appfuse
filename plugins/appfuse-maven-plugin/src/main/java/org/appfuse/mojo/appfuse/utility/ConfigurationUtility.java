package org.appfuse.mojo.appfuse.utility;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tools.ant.types.FileSet;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.util.ReflectHelper;
import org.xml.sax.EntityResolver;

/**
 * This class serves as a generic configuration utility to prepare configuations to pass to the hibernate exporters.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id$
 */
public class ConfigurationUtility
{

    /** A static logger for the class. */
    public static final Log LOG = LogFactory.getLog( ConfigurationUtility.class );

    /** A list of file sets to be used during processing of the exporter. */
    private List fileSets = new ArrayList();

    /** A configuration to process. */
    private Configuration configuration;

    /** A file that might add more configuration information to this configuration. */
    private File configurationFile;

    /** The hibernate property file to use in this configuration. */
    private File propertyFile;

    /** The name of an entity resolver class to be used to resolve entities. */
    private String entityResolver;

    /** The name of a naming strategy class to be used to construct names. */
    private String namingStrategy;

    /**
     * Creates a new ConfigurationUtility object.
     */
    public ConfigurationUtility()
    {
        super();
    }

    /**
     * This method will add a new file set objects to the list of file sets to be processed.
     * 
     * @param inFileSet
     *            A file set to be processed.
     */
    public void addConfiguredFileSet( final FileSet inFileSet )
    {
        fileSets.add( inFileSet );
    }

    /**
     * This method will create a configuration and do some initial processing on that configuration.
     * 
     * @return The existing configuration or a newly created one.
     * 
     * @throws MojoExecutionException
     *             thrown if the configuration cannot be constructed.
     */
    public final Configuration getConfiguration() throws MojoExecutionException
    {

        if ( configuration == null )
        {
            configuration = createConfiguration();
            doConfiguration( configuration );
            configuration.buildMappings();
            // needed otherwise not all assocations are made!
        }

        return configuration;
    }

    /**
     * This method will jsut return a new Configuration object.
     * 
     * @return A hibernate configuration object.
     */
    protected Configuration createConfiguration()
    {
        return new Configuration();
    }

    /**
     * This method will run some basic preparation and procssing tasks on the configuration such as loading properties
     * files from the file system into a properties object, loading naming strategy classes and entity resolver classes.
     * 
     * @param inConfiguration
     *            The configuration to configure.
     * 
     * @throws MojoExecutionException
     *             Thrown if the configuration properties cannot be loaded.
     */
    protected void doConfiguration( final Configuration inConfiguration ) throws MojoExecutionException
    {
        validateParameters();

        if ( propertyFile != null )
        {
            Properties properties = new Properties();

            try
            {
                properties.load( new FileInputStream( propertyFile ) );
            }
            catch ( FileNotFoundException ex )
            {
                throw new MojoExecutionException( propertyFile + " not found.", ex );
            }
            catch ( IOException ex )
            {
                throw new MojoExecutionException( "Problem while loading " + propertyFile, ex );
            }

            inConfiguration.setProperties( properties );
        }

        if ( entityResolver != null )
        {

            try
            {
                Class resolver = ReflectHelper.classForName( entityResolver, this.getClass() );
                Object object = resolver.newInstance();
                inConfiguration.setEntityResolver( ( EntityResolver ) object );

                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "Using " + entityResolver + " as entity resolver" );
                }
            }
            catch ( Exception e )
            {
                throw new MojoExecutionException( "Could not create or find " + entityResolver
                                + " class to use for entity resolvement" );
            }
        }

        if ( namingStrategy != null )
        {

            try
            {
                Class resolver = ReflectHelper.classForName( namingStrategy, this.getClass() );
                Object object = resolver.newInstance();
                inConfiguration.setNamingStrategy( ( NamingStrategy ) object );

                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "Using " + namingStrategy + " as naming strategy" );
                }
            }
            catch ( Exception e )
            {
                throw new MojoExecutionException( "Could not create or find " + namingStrategy
                                + " class to use for naming strategy" );
            }
        }

        if ( configurationFile != null )
        {
            inConfiguration.configure( configurationFile );
        }

        addMappings( getFiles() );
    }

    /**
     * This method will validate any parameters that need to be validated.
     */
    protected void validateParameters()
    {
        // noop
    }

    /**
     * This method will add mappings to a list of files for the configuration to process.
     * 
     * @param inFiles
     *            The files to add to the configuration to process.
     * 
     * @throws MojoExecutionException
     *             thrown if one of the files could not be added to the configuration.
     */
    private void addMappings( final File[] inFiles ) throws MojoExecutionException
    {

        for ( int i = 0; i < inFiles.length; i++ )
        {
            File filename = inFiles[i];
            boolean added = addFile( filename );

            if ( !added && LOG.isDebugEnabled() )
            {
                LOG.debug( filename + " not added to Configuration" );
            }

        }
    }

    /**
     * This method will add a file to the configuration depending on the type of file.
     * 
     * @param inFilename
     *            the file name to add to the configuration.
     * 
     * @return true if the file was added successfully to the configuration.
     * 
     * @throws MojoExecutionException
     *             thrown if the file could not be added to the configuration.
     */
    protected boolean addFile( final File inFilename ) throws MojoExecutionException
    {

        try
        {

            if ( inFilename.getName().endsWith( ".jar" ) )
            {
                configuration.addJar( inFilename );

                return true;
            }
            else
            {
                configuration.addFile( inFilename );

                return true;
            }
        }
        catch ( HibernateException ex )
        {
            throw new MojoExecutionException( "Failed in building configuration when adding " + inFilename, ex );
        }
    }

    /**
     * This method will get a list of files based on a pattern to be processed in the exporter that uses this
     * configuration.
     * 
     * @return The files that match the input pattern.
     */
    private File[] getFiles()
    {
        List files = new LinkedList();

        /*
         * for (Iterator i = fileSets.iterator(); i.hasNext();) { FileSet fs = (FileSet) i.next(); DirectoryScanner ds =
         * fs.getDirectoryScanner(getProject()); String[] dsFiles = ds.getIncludedFiles();
         * 
         * for (int j = 0; j < dsFiles.length; j++) { File f = new File(dsFiles[j]);
         * 
         * if (!f.isFile()) { f = new File(ds.getBasedir(), dsFiles[j]); }
         * 
         * files.add(f); }}
         */
        return ( File[] ) files.toArray( new File[files.size()] );
    }

    /**
     * Getter for property configuration file.
     * 
     * @return The value of configuration file.
     */
    public File getConfigurationFile()
    {
        return this.configurationFile;
    }

    /**
     * Setter for the configuration file.
     * 
     * @param inConfigurationFile
     *            The value of configuration file.
     */
    public void setConfigurationFile( final File inConfigurationFile )
    {
        this.configurationFile = inConfigurationFile;
    }

    /**
     * Getter for property property file.
     * 
     * @return The value of property file.
     */
    public File getPropertyFile()
    {
        return this.propertyFile;
    }

    /**
     * Setter for the property file.
     * 
     * @param inPropertyFile
     *            The value of property file.
     */
    public void setPropertyFile( final File inPropertyFile )
    {
        this.propertyFile = inPropertyFile;
    }

    /**
     * Setter for the entity resolver.
     * 
     * @param inEntityResolverName
     *            The value of entity resolver.
     */
    public void setEntityResolver( final String inEntityResolverName )
    {
        this.entityResolver = inEntityResolverName;
    }

    /**
     * Setter for the naming strategy.
     * 
     * @param inNamingStrategy
     *            The value of naming strategy.
     */
    public void setNamingStrategy( final String inNamingStrategy )
    {
        this.namingStrategy = inNamingStrategy;
    }
}
