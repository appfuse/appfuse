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
 * This mojo class will create Java Server Faces (JSF) backing beans for List style web
 * web pages using a set of pre-existing hbm.xml files. These hbm.xml Hibernate mapping
 * files can be generated as well using the dbtoxml goal. Once the backing beans are 
 * created they can be copied into the working project using the copywebfiles goal.
 * 
 * @author <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version $Id: $
 * @description Generate one or more JSF Backing Bean List modules from the input hbm.xml files.
 * @goal genjsflistweb
 */
public class GenerateJSFListWebMojo extends PojoMojoBase
{

    /**
     * Creates a new GenerateJSFListWebMojo object.
     */
    public GenerateJSFListWebMojo()
    {
        super();
        this.setMojoName( "GenerateJSFListWebMojo" );
    }

    /**
     * Getter for property file pattern.
     * 
     * @return The value of file pattern.
     */
    public String getOutputPattern()
    {
        return buildOutputPattern( AppFuseProperties.WEB_LIST_OUTPUT_PATTERN,
                                   AppFuseProperties.WEB_OUTPUT_PATTERN_PROPERTY_KEY, this.getPackageName() );
    }

    /**
     * This method implments the abstract method in the base class to allow a different template name to be processed.
     * 
     * @return The value of template name.
     */
    public String getTemplateName()
    {
        return locateTemplate( AppFuseProperties.WEB_JSF_LIST_TEMPLATE_NAME,
                               AppFuseProperties.WEB_TEMPLATE_NAME_PROPERTY_KEY );
    }

    /**
     * This method will return the full package name to be used for generating output for the JSF Backing Bean objects.
     * 
     * @return The full package name for all dao objects.
     * 
     */
    protected String getPackageName()
    {
        return buildPackageName( AppFuseProperties.DEFAULT_WEB_PACKAGE_EXTENSION,
                                 AppFuseProperties.WEB_PACKAGE_EXTENSION_PROPERTY_KEY );
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
        // See if there is a web and a model package extension
        // add the package names in the properties for access inside the template.
        inProperties.put( "webpackagename", this.getPackageName() );
        inProperties.put( "modelpackagename", this.getModelPackageName() );

    }

    /**
     * This method creates a String representation of this object.
     * 
     * @return the String representation of this object
     * 
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( "GenerateJSFListWebMojo[" );
        buffer.append( "]" );
        return buffer.toString();
    }
}

