package org.appfuse.mojo.appfuse.mojo;

import java.util.Properties;

/**
 * This mojo class will create DAOs from a set of POJO files.
 *
 * @author <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version $Id: $
 * @description Generate one or more model/value objects from the input hbm.xml files.
 
 */
public class GenDaoMojo extends PojoMojoBase {

    /**
     * Creates a new GenDaoMojo object.
     */
    public GenDaoMojo()
    {
        super();
        this.setMojoName( "GenDaoMojo" );
    }

    /**
     * Getter for property file pattern.
     *
     * @return The value of file pattern.
     */
    public String getOutputPattern()
    {
        return buildOutputPattern( org.appfuse.mojo.appfuse.utility.AppFuseProperties.DAO_OUTPUT_PATTERN,
                                   org.appfuse.mojo.appfuse.utility.AppFuseProperties.DAO_OUTPUT_PATTERN_PROPERTY_KEY, this.getPackageName() );
    }

    /**
     * This method implments the abstract method in the base class to allow a different template name to be processed.
     *
     * @return The value of template name.
     */
    public String getTemplateName()
    {
        return locateTemplate( org.appfuse.mojo.appfuse.utility.AppFuseProperties.DAO_TEMPLATE_NAME, org.appfuse.mojo.appfuse.utility.AppFuseProperties.DAO_TEMPLATE_NAME_PROPERTY_KEY );
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
        inProperties.put( "daopackagename", this.getPackageName() );
        inProperties.put( "modelpackagename", this.getModelPackageName() );

    }

    /**
     * This method will return the full package name to be used for generating output for the dao objects.
     *
     * @return The full package name for all dao objects.
     *
     */
    protected String getPackageName()
    {
        return buildPackageName( org.appfuse.mojo.appfuse.utility.AppFuseProperties.DEFAULT_DAO_PACKAGE_EXTENSION,
                                 org.appfuse.mojo.appfuse.utility.AppFuseProperties.DAO_PACKAGE_EXTENSION_PROPERTY_KEY );
    }

    /**
     * toString methode: creates a String representation of the object
     *
     * @return the String representation
     *
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append( super.toString() );
        buffer.append( "GenDaoMojo[" );
        buffer.append( "]" );
        return buffer.toString();
    }
}
