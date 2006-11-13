package org.appfuse.mojo.appfuse.mojo.data;

import org.appfuse.mojo.appfuse.mojo.CopyMojoBase;

/*
 * Copyright 2005-2006 The Apache Software Foundation.
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


/**
 * This mojo class will copy the Hibernate Dao java interface files from the generation directory to the target
 * directory.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 * @description Copy hibernate dao java implemenation files from the generation directory to the destination directory.
 * @goal copydaohibernatefiles
 */
public class CopyDaoHibernateFilesMojo extends CopyMojoBase
{

    /**
     * Creates a new CopyHibernateDaoFilesMojo object.
     */
    public CopyDaoHibernateFilesMojo()
    {
        super();
        this.setMojoName( "CopyHibernateDaoFilesMojo" );
    }

    /**
     * This method takes the ant based file pattern and adds the proper suffix onto it for the type of processing the
     * mojo needs to do. The default is to add .java to the end however if you need a different suffix or need to
     * manipulate the pattern in some way you can overload this method.
     * 
     * @param inFilePattern
     *            The initial file pattern requested for processing.
     * @return An augmented file pattern with the type .java added to it.
     */
    protected String augmentFilePattern( final String inFilePattern )
    {
        return inFilePattern + ".java";
    }

    /**
     * This method creates a String representation of this object.
     * 
     * @return the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "CopyHibernateDaoFilesMojo[" );

        buffer.append( "]" );

        return buffer.toString();
    }
}
