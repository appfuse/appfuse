There are several archetypes in this directory that are used to create new
AppFuse-based projects. Many of the files in these archetypes are copies or
symlinks (using svn:externals) of files that already exist in other AppFuse
directories. 

If you need to update files in these projects, make sure and update them in
AppFuse core first, then copy the file into the archetype.

Basic Archetypes
=============================================================================
* In the Struts and JSF archetypes, files in src/main/resources are copies
  from web/common/src/main/resources.
* In the Spring and Tapestry archetypes, the src/main/resources directory
  is linked (via svn:externals) to src/main/resources in the JSF archetype.
* The src/test/resources directory is linked (via svn:externals) to the
  src/test/resources directory for each web framework.
  
This means if you need to edit any files in src/main/resources, you should do
it in both the Struts and JSF modules. The reason for this is because the 
Struts module needs some more files (i.e. struts.xml) in its src/main/resources
and SVN doesn't support svn:externals on files at this time.

Modular Archetypes
=============================================================================
* The Struts archetype contains the editable copy of the "core" module. All
  other modular archetypes are linked to this one.
* The "web" modules in each modular archetype are linked to their basic
  archetype's "src" folder.
* The "src" directory in each modular archetype is linked to the "src"
  directory in appfuse-modular-struts.