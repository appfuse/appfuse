package org.appfuse;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.InvalidDependencyVersionException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Allows war artifacts to be used as fully fledged dependencies by including the WEB-INF/classes
 * directory on the compile classpath.
 * </p>
 *
 * <p>
 * It does this by creating a &quot;fake&quot; dependency on the project
 * that points to the jar'ed contents of the WEB-INF/classes directory of any war file included on the project
 * as a dependency of type warpath. The introduced dependency has scope system to prevent it being included in
 * war files.
 * </p>
 *
 * @author Michael Horwitz
 *
 * @goal add-classes
 * @phase generate-sources
 * @requiresDependencyResolution compile
 */
public class AddClassesMojo extends AbstractMojo
{

  /**
   * Location of the warpath working directory. The contents of the WEB-INF/classes directory
   * from all warpath dependencies will be extracted to jar files located in this directory.
   *
   * @parameter expression="${project.build.directory}/warpath"
   * @required
   */
  private File workDirectory;

  /**
   * The list of reactor projects.
   *
   * @parameter expression="${reactorProjects}"
   * @readonly
   */
  private List reactorProjects;

 /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;


  /**
   * Maven's artifact factory
   *
   * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
   * @required
   * @readonly
   */
  private ArtifactFactory artifactFactory;

  /**
   * The filter that determines the resources, from the dependent war's WEB-INF/classes directory, to
   * include on the classpath. Default is &quot;**&quot;.
   *
   * @parameter expression="**"
   */
  private String warpathIncludes;

  /**
   * The filter that determines the resources, from the dependent war's WEB-INF/classes directory, to
   * exclude from the classpath. Note that excludes takes priority over includes. The default is the empty
   * string, i.e. exclude nothing.
   *
   * @parameter
   */
  private String warpathExcludes;


  public void execute()
          throws MojoExecutionException
  {
    File f = workDirectory;

    if (!f.exists())
    {
      f.mkdirs();
    }

    //process all warpath dependencies, extracting their classpath elements to suitably named jar files in a sub-directory.
    Set artifacts = project.getArtifacts();

    List duplicates = findDuplicates(artifacts);

    Set newDependencies = new HashSet();

    for (Iterator iterator = artifacts.iterator(); iterator.hasNext();)
    {
      Artifact artifact = (Artifact) iterator.next();
      ScopeArtifactFilter filter = new ScopeArtifactFilter(Artifact.SCOPE_COMPILE);
      if ("warpath".equals(artifact.getType()) && filter.include(artifact) && !artifact.isOptional())
      {
        //need to include classes directory.
        String warWorkingDir = getDefaultFinalName(artifact);

        getLog().debug("Processing war dependency " + warWorkingDir);

        if (duplicates.contains(warWorkingDir))
        {
          getLog().debug("Duplicate war dependency found:" + warWorkingDir);
          warWorkingDir = artifact.getGroupId() + "-" + warWorkingDir;
          getLog().debug("Deplicate war dependency renamed to " + warWorkingDir);
        }

        File warClassesDirectory = new File(workDirectory, warWorkingDir);
        try
        {
          WarPathUtils.unpackWarClassesIfNewer(artifact.getFile(), warClassesDirectory, warpathIncludes, warpathExcludes);
        }
        catch (IOException e)
        {
          throw new MojoExecutionException("I/O error while processing WAR dependencies.", e);
        }
        getLog().debug("Adding new dependenvy artifact entry for " + warWorkingDir);
        try
        {
          newDependencies.add(getWarClassesDependency(artifact, warClassesDirectory));
        }
        catch (OverConstrainedVersionException e)
        {
          throw new MojoExecutionException("Failed to created war classes dependency for artifact " + warWorkingDir, e);
        }
      }
    }

    if (newDependencies.size() > 0)
    {
      //set the dependencies as well for ide plugins
      addDepenciesToProject(project, newDependencies);

      //Check through the reactor projects and add dependencies to the execution project
      //in case this project is run in parallel. Fix need to ensure project dependencies
      //properly resolved for Eclipse plugin. Ugly, any better suggestions welcome....
      for (int i = 0; i < reactorProjects.size(); i++)
      {
        MavenProject mavenProject = (MavenProject) reactorProjects.get(i);
        if (mavenProject.getArtifactId().equals(project.getArtifactId()) &&
                mavenProject.getGroupId().equals(project.getGroupId()) &&
                mavenProject != project)
        {
          getLog().debug("Adding dependencies to reactor project: " + mavenProject.getGroupId() + "-" + mavenProject.getArtifactId());
          addDepenciesToProject(mavenProject, newDependencies);
        }
      }
    }

  }

  private void addDepenciesToProject(MavenProject project, Set dependencies)
          throws MojoExecutionException
  {
    List amalgamatedDependencies = new ArrayList();
    amalgamatedDependencies.addAll(dependencies);
    amalgamatedDependencies.addAll(project.getDependencies());
    project.setDependencies(amalgamatedDependencies);
    try
    {
      project.setDependencyArtifacts(project.createArtifacts(artifactFactory, null, null));
    }
    catch (InvalidDependencyVersionException e)
    {
      throw new MojoExecutionException("Failed to resolve dependency artifacts.", e);
    }
  }

  /**
   * Build a dependency with scope system for the extracted war class files.
   *
   * @param artifact            The original war artifact.
   * @param warClassesDirectory The directory in which the class files have been extracted.
   * @return A new dependency, set to scope system, to include in the project's compile classpath.
   */
  private Dependency getWarClassesDependency(Artifact artifact, File warClassesDirectory) throws OverConstrainedVersionException
  {
    Dependency dependency = new Dependency();
    dependency.setArtifactId(artifact.getArtifactId());
    dependency.setGroupId(artifact.getGroupId());
    dependency.setType("classes");
    dependency.setScope(Artifact.SCOPE_SYSTEM);
    dependency.setOptional(true);
    dependency.setVersion(artifact.getSelectedVersion().toString());
    dependency.setSystemPath(warClassesDirectory.getPath());
    return dependency;
  }

  /**
   * Searches a set of artifacts for duplicate filenames and returns a list of duplicates.
   *
   * @param artifacts set of artifacts
   * @return List of duplicated artifacts
   */
  private List findDuplicates(Set artifacts)
  {
    List duplicates = new ArrayList();
    List identifiers = new ArrayList();
    for (Iterator iter = artifacts.iterator(); iter.hasNext();)
    {
      Artifact artifact = (Artifact) iter.next();
      String candidate = getDefaultFinalName(artifact);
      if (identifiers.contains(candidate))
      {
        duplicates.add(candidate);
      }
      else
      {
        identifiers.add(candidate);
      }
    }
    return duplicates;
  }

  /**
   * Converts the filename of an artifact to artifactId-version.type format.
   *
   * @param artifact
   * @return converted filename of the artifact
   */
  private String getDefaultFinalName(Artifact artifact)
  {
    if ("warpath".equals(artifact.getType()))
    {
      return artifact.getArtifactId() + "-" + artifact.getVersion() + "." +
             artifact.getType() + ".jar";
    }
    else
    {
      return artifact.getArtifactId() + "-" + artifact.getVersion() + "." +
              artifact.getArtifactHandler().getExtension();
    }
  }

}
