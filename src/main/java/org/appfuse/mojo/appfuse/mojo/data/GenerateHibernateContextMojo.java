package org.appfuse.mojo.appfuse.mojo.data;

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

/**
 * This mojo class will create context xml entries for the hibernate dao implementations and dao interfaces from a set
 * of hbm.xml files.
 * 
 * @author <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version $Id: $
 * @description Generate one or more hibernate context entries for the applicationContext-persist.xml file from input
 *              hbm.xml files.
 
 */
public class GenerateHibernateContextMojo extends PojoMojoBase
{

    /**
     * Creates a new GenerateHibernateContextMojo object.
     */
    public GenerateHibernateContextMojo()
    {
        super();
        this.setMojoName( "GenerateHibernateContextMojo" );
    }

    /**
     * Getter for property file pattern.
     * 
     * @return The value of file pattern.
     */
    public String getOutputPattern()
    {
        return buildOutputPattern( org.appfuse.mojo.appfuse.utility.AppFuseProperties.HIBERNATE_CONTEXT_OUTPUT_PATTERN,
                                   org.appfuse.mojo.appfuse.utility.AppFuseProperties.HIBERNATE_CONTEXT_OUTPUT_PATTERN_PROPERTY_KEY,
                                   this.getHibernaeDaoPackageName() );
    }

    /**
     * This method implments the abstract method in the base class to allow a different template name to be processed.
     * 
     * @return The value of template name.
     */
    public String getTemplateName()
    {
        return locateTemplate( org.appfuse.mojo.appfuse.utility.AppFuseProperties.HIBERNATE_CONTEXT_TEMPLATE_NAME,
                               org.appfuse.mojo.appfuse.utility.AppFuseProperties.HIBERNATE_CONTEXT_TEMPLATE_NAME_PROPERTY_KEY );
    }

    /**
     * This method will return the full package name to be used for generating output for the dao objects.
     * 
     * @return The full package name for all dao objects.
     * 
     */
    protected String getHibernaeDaoPackageName()
    {
        return buildPackageName( org.appfuse.mojo.appfuse.utility.AppFuseProperties.DEFAULT_HIBERNATE_DAO_PACKAGE_EXTENSION,
                                 org.appfuse.mojo.appfuse.utility.AppFuseProperties.HIBERNATE_DAO_PACKAGE_EXTENSION_PROPERTY_KEY );
    }

    /**
     * This method will return the name to use for the session factory.
     * 
     * @return The name of the session factory.
     */
    protected String getSessionFactoryName()
    {
        // See if a session factory name was passed in otherwise use the default.
        String sessionFactoryName = org.appfuse.mojo.appfuse.utility.AppFuseProperties.DEFAULT_SESSION_FACTORY_NAME;

        // See if there is a sessionFactoryName in the properties
        if ( this.getProcessingProperties() != null
                        && this.getProcessingProperties().containsKey(
                           org.appfuse.mojo.appfuse.utility.AppFuseProperties.SESSION_FACTORY_NAME_PROPERTY_KEY ) )
        {
            sessionFactoryName =
                ( String ) this.getProcessingProperties().get( 
                           org.appfuse.mojo.appfuse.utility.AppFuseProperties.SESSION_FACTORY_NAME_PROPERTY_KEY );
        }
        return sessionFactoryName;
    }

    /**
     * This method is used to make sure the proper properties are set to be used in processing this mojo. These
     * properties are passed in to the generator to be used within any freemarker templates. *
     * 
     * @param inProperties
     *            The project properties that already have been populated.
     */
    protected void validateProperties( final Properties inProperties )
    {
        // See if there is a model package extension
        // add the model package name in the properties for access inside the template.
        inProperties.put( "hibernatedaopackagename", this.getHibernaeDaoPackageName() );
        inProperties.put( "modelpackagename", this.getModelPackageName() );
        // Get the name of the template helper.
        inProperties.put( "hibernatetool.assist.toolclass", this.getTemplateHelperClassName() );
        // Get the name of the session factory to inject.
        inProperties.put( "sessionfactoryname", this.getSessionFactoryName() );

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
        buffer.append( "GenerateHibernateContextMojo[" );
        buffer.append( "]" );

        return buffer.toString();
    }
}
