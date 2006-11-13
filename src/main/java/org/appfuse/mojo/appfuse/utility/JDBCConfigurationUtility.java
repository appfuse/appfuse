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
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.util.ReflectHelper;

/**
 * This class is a generic class for setting up the configuration of various exporters in the hibernate tools library.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id$
 */
public class JDBCConfigurationUtility extends ConfigurationUtility
{

    /** A static logger for the class. */
    public static final Log LOG = LogFactory.getLog( JDBCConfigurationUtility.class );

    /** This field indiciates if basic composite Ids should be used rather than complex ones. */
    private boolean preferBasicCompositeIds = true;

    /**
     * This is an optional class that can be used to alter the way the system converts database tables to hbm files.
     */
    private String reverseEngineeringStrategyClass;

    /** This is the package name to be used in converting the tables to classes. */
    private String packageName;

    /**
     * This is a list of reverse engineering xml files used to control the reverse engineering process such as which
     * schemas and tables to process.
     */
    private List revengFiles = new ArrayList();

    /**
     * Creates a new JDBCConfigurationUtility object.
     */
    public JDBCConfigurationUtility()
    {
        super();
    }

    /**
     * This method will create a new configuration object.
     * 
     * @return A JDBC configuration object.
     */
    public Configuration createConfiguration()
    {
        return new JDBCMetaDataConfiguration();
    }

    /**
     * This method will set up the aspects of the configuration for use by the hibernate exporters.
     * 
     * @param inConfiguration
     *            The JDBC configuration to set up.
     * 
     * @throws MojoExecutionException
     *             Thrown if a class or value cannot be located.
     */
    public void doConfiguration( final Configuration inConfiguration ) throws MojoExecutionException
    {
        JDBCMetaDataConfiguration jmdc = ( JDBCMetaDataConfiguration ) inConfiguration;
        super.doConfiguration( jmdc );
        jmdc.setPreferBasicCompositeIds( preferBasicCompositeIds );

        DefaultReverseEngineeringStrategy defaultStrategy = new DefaultReverseEngineeringStrategy();
        ReverseEngineeringSettings qqsettings = new ReverseEngineeringSettings().setDefaultPackageName( packageName );

        if ( packageName != null )
        {
            defaultStrategy.setSettings( qqsettings );
        }

        ReverseEngineeringStrategy strategy = defaultStrategy;

        if ( revengFiles.size() == 0 )
        {
            throw new MojoExecutionException( "You must specify at least one ReverseEngineeringStrategy File" );
        }

        if ( revengFiles.size() > 0 )
        {

            // Create an override repository and load the overrides into it.
            OverrideRepository or = new OverrideRepository();

            for ( int i = 0; i < revengFiles.size(); i++ )
            {
                or.addFile( new File( ( String ) revengFiles.get( i ) ) );
            }

            strategy = or.getReverseEngineeringStrategy( defaultStrategy );
        }

        if ( reverseEngineeringStrategyClass != null )
        {
            strategy = loadreverseEngineeringStrategy( reverseEngineeringStrategyClass, strategy );
        }

        strategy.setSettings( qqsettings );
        jmdc.setReverseEngineeringStrategy( strategy );
        jmdc.readFromJDBC();
    }

    /**
     * Setter for the package name.
     * 
     * @param inPackageName
     *            The value of package name.
     */
    public void setPackageName( final String inPackageName )
    {
        this.packageName = inPackageName;
    }

    /**
     * Setter for the reverse strategy.
     * 
     * @param inReverseStrategy
     *            The value of reverse strategy.
     */
    public void setReverseStrategy( final String inReverseStrategy )
    {
        this.reverseEngineeringStrategyClass = inReverseStrategy;
    }

    /**
     * Setter for the rev eng file list.
     * 
     * @param inFileName
     *            The value of rev eng file to be added to the list.
     */
    public void addRevEngFile( final String inFileName )
    {
        this.revengFiles.add( inFileName );
    }

    /**
     * Setter for the prefer basic composite ids.
     * 
     * @param inFlag
     *            The value of prefer basic composite ids.
     */
    public void setPreferBasicCompositeIds( final boolean inFlag )
    {
        this.preferBasicCompositeIds = inFlag;
    }

    /**
     * This method will load the reverse engineering strategy and make sure the class can be constructed for use.
     * 
     * @param inClassName
     *            The class name of the strategy class to be constructed.
     * @param inDelegate
     *            Any delegate that might be used for this class.
     * 
     * @return A loaded class representing the strategy or it delegate.
     * 
     * @throws MojoExecutionException
     *             Thrown if the engeineering structure could not be created.
     */
    private ReverseEngineeringStrategy loadreverseEngineeringStrategy( final String inClassName,
                                                                       final ReverseEngineeringStrategy inDelegate )
        throws MojoExecutionException
    {

        try
        {

            // see if we are using a clas with a delegate
            Class clazz = ReflectHelper.classForName( inClassName );
            Constructor constructor = clazz.getConstructor( new Class[] { ReverseEngineeringStrategy.class } );

            return ( ReverseEngineeringStrategy ) constructor.newInstance( new Object[] { inDelegate } );
        }
        catch ( NoSuchMethodException ex )
        {

            try
            {

                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "Could not find public " + inClassName
                                    + "(ReverseEngineeringStrategy delegate) constructor on "
                                    + " ReverseEngineeringStrategy. Trying no-arg version." );
                }

                Class clazz = ReflectHelper.classForName( inClassName );
                ReverseEngineeringStrategy rev = ( ReverseEngineeringStrategy ) clazz.newInstance();

                if ( LOG.isDebugEnabled() )
                {
                    LOG.debug( "Using non-delegating strategy, thus packagename and revengfile will be ignored." );
                }

                return rev;
            }
            catch ( Exception ex1 )
            {
                throw new MojoExecutionException( "Could not create or find " + inClassName
                                + " with default no-arg constructor", ex1 );
            }
        }
        catch ( Exception ex )
        {
            throw new MojoExecutionException( "Could not create or find " + inClassName
                            + " with one argument delegate constructor", ex );
        }
    }
}
