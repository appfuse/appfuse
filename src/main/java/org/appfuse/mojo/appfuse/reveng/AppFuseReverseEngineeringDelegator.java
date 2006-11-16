package org.appfuse.mojo.appfuse.reveng;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

/**
 * This class is a sample of a reverse engineering strategy processing class. This class will add Value to all the model
 * objects and the hbm.xml files as well as using the column name OPTIMISTIC_VERSION as the version column within the
 * hbm.xml files.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id$
 */
public class AppFuseReverseEngineeringDelegator extends DelegatingReverseEngineeringStrategy
{

    /** A static logger for the class. */
    public static final Log LOG = LogFactory.getLog( AppFuseReverseEngineeringDelegator.class );

    /**
     * Creates a new AuroraReverseEngineeringDelegator object.
     * 
     * @param inDelegate
     *            A delegat for this object.
     */
    public AppFuseReverseEngineeringDelegator( final ReverseEngineeringStrategy inDelegate )
    {
        super( inDelegate );
    }

    /**
     * This method will add Value to the table name to create the model objects.
     * 
     * @param inTableIdentifier
     *            The table name that is being processed.
     * 
     * @return The class name to use for the model and hbm file for this table. It consists of the table name appended
     *         with Value. So for table USER_DATA the model object created would be UserDataValue.
     */
    public String tableToClassName( final TableIdentifier inTableIdentifier )
    {
        String tableName = super.tableToClassName( inTableIdentifier );

        if ( LOG.isDebugEnabled() )
        {
            LOG.debug( "Processing table " + tableName );
        }

        return tableName;  //+ "Value";
    }

    /**
     * This method is called to determine a generic column name to use for the optimistic lock or version entry.
     * 
     * @param inTableIdentifier
     *            The Table identifier to locate the optimistic column name for.
     * 
     * @return The value of the optimistic lock column name for the requested table.
     */
    public String getOptimisticLockColumnName( final TableIdentifier inTableIdentifier )
    {
        return "OPTIMISTIC_VERSION";
    }
}
