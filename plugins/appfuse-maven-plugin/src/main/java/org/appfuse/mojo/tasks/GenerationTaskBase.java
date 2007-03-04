package org.appfuse.mojo.tasks;

/**
 * This class serves as a base class for all code generation tasks.
 *
 * @author   <a href="mailto:scott@theryansplace.com">Scott Ryan</a>
 * @version  $Id: $
 */
public abstract class GenerationTaskBase
{
    /** This is the directory to send the generated outpupt to. */
    private String outputDirectory;

    /** This is set to true is you want to use JDK 5 annotations. */
    private boolean jdk5;

    /** This is set to true if you want to use ejb3 extensions. */
    private boolean ejb3;

    /** This is the name of the freemarker template to use for code generation. */
    private String templateName;

    /**
     * This is the directory where the classes for the model objects are output to during
     * compilation.
     */
    private String ouputClassDirectory;

    /** Then package name for the model objects. */
    private String modelPackageName;

    /**
     * Creates a new GenerationTaskBase object.
     */
    public GenerationTaskBase()
    {
        super();
    }

    /**
     * This method will perform the actual generation of the code.
     */
    public abstract void execute();

    /**
     * Getter for property ejb3.
     *
     * @return  The value of ejb3.
     */
    public boolean isEjb3()
    {
        return this.ejb3;
    }

    /**
     * Setter for the ejb3.
     *
     * @param  inEjb3  The value of ejb3.
     */
    public void setEjb3(final boolean inEjb3)
    {
        this.ejb3 = inEjb3;
    }

    /**
     * Getter for property jdk5.
     *
     * @return  The value of jdk5.
     */
    public boolean isJdk5()
    {
        return this.jdk5;
    }

    /**
     * Setter for the jdk5.
     *
     * @param  inJdk5  The value of jdk5.
     */
    public void setJdk5(final boolean inJdk5)
    {
        this.jdk5 = inJdk5;
    }

    /**
     * Getter for property template name.
     *
     * @return  The value of template name.
     */
    public String getTemplateName()
    {
        return this.templateName;
    }

    /**
     * Setter for the template name.
     *
     * @param  inTemplateName  The value of template name.
     */
    public void setTemplateName(final String inTemplateName)
    {
        this.templateName = inTemplateName;
    }

    /**
     * Getter for property output directory.
     *
     * @return  The value of output directory.
     */
    public String getOutputDirectory()
    {
        return this.outputDirectory;
    }

    /**
     * Setter for the output directory.
     *
     * @param  inOutputDirectory  The value of output directory.
     */
    public void setOutputDirectory(String inOutputDirectory)
    {
        this.outputDirectory = inOutputDirectory;
    }

    /**
     * Getter for property ouput class directory.
     *
     * @return  The value of ouput class directory.
     */
    public String getOuputClassDirectory()
    {
        return this.ouputClassDirectory;
    }

    /**
     * Setter for the ouput class directory.
     *
     * @param  inOuputClassDirectory  The value of ouput class directory.
     */
    public void setOuputClassDirectory(final String inOuputClassDirectory)
    {
        this.ouputClassDirectory = inOuputClassDirectory;
    }

    /**
     * Getter for property model package name.
     *
     * @return  The value of model package name.
     */
    public String getModelPackageName()
    {
        return this.modelPackageName;
    }

    /**
     * Setter for the model package name.
     *
     * @param  inModelPackageName  The value of model package name.
     */
    public void setModelPackageName(final String inModelPackageName)
    {
        this.modelPackageName = inModelPackageName;
    }

    /**
     * This method creates a String representation of this object.
     *
     * @return  the String representation of this object
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(super.toString());
        buffer.append("GenerationTaskBase[");
        buffer.append("ejb3 = ").append(ejb3);
        buffer.append("\n jdk5 = ").append(jdk5);
        buffer.append("\n ouputClassDirectory = ").append(ouputClassDirectory);
        buffer.append("\n outputDirectory = ").append(outputDirectory);
        buffer.append("\n templateName = ").append(templateName);
        buffer.append("\n modelPackageName = ").append(modelPackageName);
        buffer.append("]");

        return buffer.toString();
    }
}
