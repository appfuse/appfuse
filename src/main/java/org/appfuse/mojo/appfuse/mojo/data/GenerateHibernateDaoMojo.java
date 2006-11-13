package org.appfuse.mojo.appfuse.mojo.data;

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

import java.util.Properties;

import org.appfuse.mojo.appfuse.mojo.PojoMojoBase;
import org.appfuse.mojo.appfuse.utility.AppFuseProperties;

/**
 * This mojo class will create hibernate dao implementations of the dao interfaces from a set of hbm.xml files.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 * @description Generate one or more hibernate dao implementations of the dao interfaces from the input hbm.xml files.
 * @goal genhibernatedao
 */
public class GenerateHibernateDaoMojo extends PojoMojoBase
{

    /**
     * Creates a new GenerateHibernateDaoMojo object.
     */
    public GenerateHibernateDaoMojo()
    {
        super();
        this.setMojoName( "GenerateHibernateDaoMojo" );
    }

    /**
     * This method will return the full package name to be used for generating output for the hibernate dao objects.
     * 
     * @return The full package name for all dao objects.
     * 
     */
    protected String getPackageName()
    {
        return buildPackageName( AppFuseProperties.DEFAULT_HIBERNATE_DAO_PACKAGE_EXTENSION,
                                 AppFuseProperties.HIBERNATE_DAO_PACKAGE_EXTENSION_PROPETY_KEY );
    }

    /**
     * This method is used to make sure the proper properties are set to be used in processing this mojo. These
     * properties are passed in to the generator to be used within any freemarker templates.
     * 
     * @param inProperties
     *            The project properties that already have been populated.
     */
    protected void validateProperties( final Properties inProperties )
    {
        // add the model package name in the properties for access inside the template.
        inProperties.put( "hibernatedaopackagename", this.getPackageName() );
        inProperties.put( "modelpackagename", this.getModelPackageName() );

    }

    /**
     * This method implments the abstract method in the base class to allow a different template name to be processed.
     * 
     * @return The value of template name.
     */
    public String getTemplateName()
    {
        return locateTemplate( AppFuseProperties.HIBERNATE_DAO_TEMPLATE_NAME,
                               AppFuseProperties.HIBERNATE_DAO_TEMPLATE_NAME_PROPERTY_KEY );

    }

    /**
     * This method implements the abstract method in the base class allowing a different pattern to be used for this
     * generation.
     * 
     * @return The value of output pattern.
     */
    public String getOutputPattern()
    {
        return buildOutputPattern( AppFuseProperties.HIBERNATE_DAO_OUTPUT_PATTERN,
                                   AppFuseProperties.HIBERNATE_DAO_OUTPUT_PATTERN_PROPERTY_KEY, this.getPackageName() );
    }

    /**
     * This method creates a String representation of this object.
     * 
     * @return the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "GenerateHibernateDaoMojo[" );
        buffer.append( "]" );

        return buffer.toString();
    }
}
