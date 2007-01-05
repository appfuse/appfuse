There are several archetypes in this directory that are used to create new
AppFuse-based projects. Many of the files in these archetypes are copies or
symlinks (using svn:externals) of files that already exist in other AppFuse
directories. 

If you need to update files in these projects, make sure and update them in
AppFuse core first, then copy the file into the archetype.

* In the Struts and JSF archetypes, files in src/main/resources are copies
  from web/common/src/main/resources.
* In the Spring and Tapestry archetypes, the src/main/resources directory
  is linked (via svn:externals) to src/main/resources in the JSF archetype.
* The src/test/resources directory is linked (via svn:externals) to the
  src/test/resources directory for each web framework.

