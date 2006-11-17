package org.appfuse.mojo.appfuse.mojo.web;

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

import org.appfuse.mojo.appfuse.mojo.PojoMojoBase;
import org.appfuse.mojo.appfuse.utility.AppFuseProperties;

/**
 * This mojo class will create Struts Action classes for web pages using a set of
 * pre-existing hbm.xml files. These hbm.xml Hibernate mapping files can be generated
 * as well using the dbtoxml goal. Once the backing beans are created they can be copied
 * into the working project using the copywebfiles goal.
 *
 * @author <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version $Id: $
 * @description Generate Struts Action module for the AppFuse Pojo List.
 * @goal genstrutsaction
 */
public class GenerateStrutsActionMojo extends PojoMojoBase {
    /**
     * Creates a new GenerateJSFFormMojo object.
     */
    public GenerateStrutsActionMojo()
    {
        super();
        this.setMojoName( "GenerateStrutsActionMojo" );
    }

    /**
     * Getter for property file pattern.
     *
     * @return The value of file pattern.
     */
    public String getOutputPattern()
    {
        return buildOutputPattern( AppFuseProperties.WEB_FORM_OUTPUT_PATTERN,
                                   AppFuseProperties.WEB_OUTPUT_PATTERN_PROPERTY_KEY, this.getPackageName() );
    }

    /**
     * This method implments the abstract method in the base class to allow a different template name to be processed.
     *
     * @return The value of template name.
     */
    public String getTemplateName()
    {
        return locateTemplate( AppFuseProperties.WEB_STRUTS_ACTION_TEMPLATE_NAME,
                               AppFuseProperties.WEB_TEMPLATE_NAME_PROPERTY_KEY );
    }

    /**
     * This method will return the full package name to be used for generating output for the JSF Backing Bean objects.
     *
     * @return The full package name for all web objects.
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
        // See if there is a model and web package extension
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
        buffer.append( "GenerateStrutsActionMojo[" );
        buffer.append( "]" );
        return buffer.toString();
    }
}
