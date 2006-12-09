#!/bin/bash
########################################################################
# Create Archetype Example Script
#   
# written by David L. Whitehurst
# dated December 2, 2006
#
########################################################################
# This script is provided as an example for you to use to create
# Maven archetypes for your AppFuse applications. Feel free to 
# modify this to suit your groupId and artifactId.  To use this
# repeatedly just copy this script in a working directory and
# use it to create new projects
#
########################################################################

mvn archetype:create -DarchetypeGroupId=org.appfuse -DarchetypeArtifactId=appfuse-archetype-basic -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=org.appfuse -DartifactId=myapp -DremoteRepositories=http://static.appfuse.org/repository 
