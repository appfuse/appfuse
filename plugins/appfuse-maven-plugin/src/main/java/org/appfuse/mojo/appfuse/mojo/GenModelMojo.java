package org.appfuse.mojo.appfuse.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.hibernate.tool.hbm2x.HibernateMappingExporter;
import org.hibernate.tool.hbm2x.POJOExporter;
import org.hibernate.cfg.Configuration;
import org.appfuse.mojo.appfuse.utility.JDBCConfigurationUtility;

import java.util.Properties;
import java.util.List;
import java.io.File;

/**
 * This mojo class will create model or value objects from a set of hbm.xml
 * files.
 * 
 * @author <a href="mailto:dlwhitehurst@gmail.com">David L. Whitehurst</a>
 * @version $Id: $
 * @description Generate one or more model/value objects from the input hbm.xml
 *              files.

 */
public class GenModelMojo extends PojoMojoBase {

	/**
	 * Creates a new GenModelMojo object.
	 */
	public GenModelMojo() {
		super();
		this.setMojoName("GenModelMojo");
	}

	/**
	 * This method will run the database conversion to hbm file mojo task.
	 * 
	 * @throws org.apache.maven.plugin.MojoExecutionException
	 *             Thrown if we fail to obtain an appfuse resource.
	 */
	public void execute() throws MojoExecutionException {

		if (getLog().isInfoEnabled()) {
			getLog().info(
					"Running the " + this.getMojoName()
							+ " Mojo with properties " + this);
		}

		// Get a Hibernate Mapping Exporter
		HibernateMappingExporter exporter = new HibernateMappingExporter();
		Properties properties = new Properties();
		// Load the ejb3 and jdk5 flags
		properties.put("ejb3", this.isEjb3());
		properties.put("jdk5", this.isJdk5());

		// Set any custom properties that might have been passed in.
		exporter.setProperties(properties);

		// create a new JDBC configuration object.
		JDBCConfigurationUtility configurationUtility = new JDBCConfigurationUtility();

		// call create to set up the configuration.
		Configuration configuration = configurationUtility
				.createConfiguration();

		// Set some parameters in the configuration class.
		if ((this.getModelPackageName() != null)
				&& (this.getModelPackageName().length() > 0)) {
			configurationUtility.setPackageName(this.getModelPackageName());
		} else {
			throw new MojoExecutionException(
					"Model package name cannot be null or empty");
		}

		if ((this.getDatabasePropertiesFile() != null)
				&& (this.getDatabasePropertiesFile().length() > 0)) {
			configurationUtility.setPropertyFile(new File(this
					.getDatabasePropertiesFile()));
		} else {
			throw new MojoExecutionException(
					"Hibernate properties file cannot be null or empty");
		}

		// Load the reverse engineering configuration xml files.
		if ((this.getReverseEngineeringConfigurationFile() == null)
				|| (this.getReverseEngineeringConfigurationFile().length() == 0)) {
			throw new MojoExecutionException(
					"There must be at least one reverse engineering xml file defined");
		}

		configurationUtility.addRevEngFile(this
				.getReverseEngineeringConfigurationFile());

		// Set the reverse engineering strategy class
		if ((this.getReverseStrategyClass() != null)
				&& (this.getReverseStrategyClass().length() > 0)) {
			configurationUtility.setReverseStrategy(this
					.getReverseStrategyClass());
		}

		// complete the configuration processing
		configurationUtility.doConfiguration(configuration);
		configuration.buildMappings();

		// set the configurator into the exporter
		exporter.setConfiguration(configuration);

		// Set the destination directory
		if ((this.getOutputDirectory() != null)
				&& (this.getOutputDirectory().length() > 0)) {
			exporter.setOutputDirectory(new File(this.getOutputDirectory()));
		} else {
			throw new MojoExecutionException(
					"Output directory cannot be null or empty");
		}

		// Set the template information.
		exporter.setTemplateName(this.getHbmTemplateName());

		// run the exporter.
		exporter.start();

		// run second part
		// ###########################################################

		// Get a Hibernate Mapping Exporter
		POJOExporter pojoExporter = new POJOExporter();
		// Allow the class to set, update or delete property settings before
		// they are handed into the exporter. We use
		// the passed back property set so that the original on remains intact.
		Properties updatedProperties = null;
		// Make sure we have a properties object.
		if (this.getProcessingProperties() == null) {
			updatedProperties = new Properties();
		} else {
			updatedProperties = this.getProcessingProperties();
		}
		// Allow the mojo to modify the properties
		validateProperties(updatedProperties);

		// Set any custom properties that might have been passed in.
		exporter.setProperties(updatedProperties);

		// call create to set up the configuration.
		Configuration pojoConfiguration = new Configuration();

		List files = this.getListOfFilesToProcess();
		getLog()
				.info(
						"Processing " + files.size()
								+ " files based on pattern match ");

		for (int j = 0; j < files.size(); j++) {
			String fileName = (String) files.get(j);

			if (getLog().isDebugEnabled()) {
				getLog().debug(
						"Adding file " + fileName
								+ " to Pojo the processing list");
			}

			pojoConfiguration.addFile(fileName);
		}

		// set the configuratino into the exporter
		pojoExporter.setConfiguration(configuration);

		// Set the destination directory
		if ((this.getOutputDirectory() != null)
				&& (getOutputDirectory().length() > 0)) {
			pojoExporter.setOutputDirectory(new File(getOutputDirectory()));
		} else {
			throw new MojoExecutionException(
					"output directory cannot be null or empty");
		}

		// Set the file pattern model for the ouput files
		pojoExporter.setFilePattern(getOutputPattern());

		// Set the template information.
		pojoExporter.setTemplateName(getTemplateName());

		// run the exporter.
		pojoExporter.start();
	}

	protected void validateProperties(final Properties inProperties) {
		// add the model package name in the properties for access inside the
		// template.
		inProperties.put("modelpackagename", this.getModelPackageName());

	}

	protected String getTemplateName() {
		return locateTemplate(
				org.appfuse.mojo.appfuse.utility.AppFuseProperties.MODEL_TEMPLATE_NAME,
				org.appfuse.mojo.appfuse.utility.AppFuseProperties.MODEL_TEMPLATE_NAME_PROPERTY_KEY);

	}

	public String getOutputPattern() {
		return buildOutputPattern(
				org.appfuse.mojo.appfuse.utility.AppFuseProperties.MODEL_OUTPUT_PATTERN,
				org.appfuse.mojo.appfuse.utility.AppFuseProperties.MODEL_OUTPUT_PATTERN_PROPERTY_KEY,
				this.getModelPackageName());

	}

	/**
	 * toString method: creates a String representation of the object
	 * 
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append("GenModelMojo[");
		buffer.append("]");
		return buffer.toString();
	}

}
