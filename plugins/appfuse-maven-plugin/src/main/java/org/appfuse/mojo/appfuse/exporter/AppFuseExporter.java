package org.appfuse.mojo.appfuse.exporter;

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

import java.io.File;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2x.POJOExporter;
import org.hibernate.tool.hbm2x.pojo.POJOClass;

/**
 * This method provides a sample on how to extend the pojo processor when processing new objects from the hbm.xml into
 * jaa objects using freemarker.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id$
 */
public class AppFuseExporter extends POJOExporter
{

    /** A static logger for the class. */
    public static final Log LOG = LogFactory.getLog( AppFuseExporter.class );

    /**
     * Creates a new AppFuseExporter object.
     * 
     * @param inConfiguration
     *            The configuration object containing useful configuration information.
     * @param inOutputDirectory
     *            The directory to output the results into.
     */
    public AppFuseExporter( final Configuration inConfiguration, final File inOutputDirectory )
    {
        super( inConfiguration, inOutputDirectory );
    }

    /**
     * Creates a new AppFuseExporter object.
     */
    public AppFuseExporter()
    {
        super();
    }

    /**
     * This method will set up the processing context for this object such as template and file pattern processing
     * information.
     */
    protected void setupContext()
    {

        if ( getProperties().containsKey( "filepattern" ) )
        {
            this.setFilePattern( getProperties().getProperty( "filepattern" ) );

            if ( log.isDebugEnabled() )
            {
                log.debug( "Setting file pattern to " + getProperties().getProperty( "filepattern" ) );
            }
        }
        else
        {
            log.fatal( "You must set the file pattern property" );
        }

        if ( getProperties().containsKey( "templatename" ) )
        {
            this.setTemplateName( getProperties().getProperty( "templatename" ) );

            if ( log.isDebugEnabled() )
            {
                log.debug( "Setting template name to " + getProperties().getProperty( "templatename" ) );
            }
        }
        else
        {
            log.fatal( "You must set the template name property" );
        }

        super.setupContext();
    }

    /**
     * This method determines if the components should be exported or not. For generation of AppFuse objects we do not
     * want to process any component objects but just the primary objects.
     * 
     * @param inAdditionalContext
     *            Any additional context objects used for processing.
     * @param inElement
     *            The object to process.
     */
    protected void exportComponent( final Map inAdditionalContext, final POJOClass inElement )
    {
        // noop - we dont want to export components just the primary classes.
    }
}
