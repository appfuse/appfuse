package org.appfuse.mojo.appfuse.mojo.service;

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
import org.appfuse.mojo.appfuse.utility.AppfuseProperties;

/**
 * This mojo class will create manager implementations implementations of the manager interfaces from a set of hbm.xml
 * files.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 * @description Generate one or more manager implementations of the manager interfaces from the input hbm.xml files.
 * @goal genmanagerimpl
 */
public class GenerateManagerImplMojo extends PojoMojoBase
{

    /**
     * Creates a new GenerateManagerImplMojo object.
     */
    public GenerateManagerImplMojo()
    {
        super();
        this.setMojoName( "GenerateManagerImplMojo" );
    }

    /**
     * Getter for property file pattern.
     * 
     * @return The value of file pattern.
     */
    public String getOutputPattern()
    {
        return buildOutputPattern( AppfuseProperties.MANAGER_IMPL_OUTPUT_PATTERN,
                                   AppfuseProperties.MANAGER_IMPL_OUTPUT_PATTERN_PROPERTY_KEY, this.getPackageName() );
    }

    /**
     * This method implments the abstract method in the base class to allow a different template name to be processed.
     * 
     * @return The value of template name.
     */
    public String getTemplateName()
    {
        return locateTemplate( AppfuseProperties.MANAGER_IMPL_TEMPLATE_NAME,
                               AppfuseProperties.MANAGER_IMPL_TEMPLATE_NAME_PROPERTY_KEY );
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
        // See if there is a model package extension
        // add the model package name in the properties for access inside the template.
        inProperties.put( "managerimplpackagename", this.getPackageName() );
        inProperties.put( "modelpackagename", this.getModelPackageName() );

    }

    /**
     * This method will return the full package name to be used for generating output for the objects.
     * 
     * @return The full package name for all objects.
     * 
     */
    protected String getPackageName()
    {
        return buildPackageName( AppfuseProperties.DEFAULT_MANAGER_IMPL_PACKAGE_EXTENSION,
                                 AppfuseProperties.MANAGER_IMPL_PACKAGE_EXTENSION_PROPETY_KEY );
    }

    /**
     * This method creates a String representation of this object.
     * 
     * @return the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( "GenerateManagerImplMojo[" );
        buffer.append( "]" );

        return buffer.toString();
    }
}
