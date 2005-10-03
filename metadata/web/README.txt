XDoclet has a concept called "merge points" that allows you to create files
that will be included into the final XML artifact you are creating.  This is a
list of possible files (in merged order) that can be used as merge-points in 
struts-resume.

For the filenames with a "{0}", this is not a typo. When XDoclet looks for the 
merge file, it will first substitute {0} with the unqualified name of the 
current class.

struts-config.xml
====================================
struts-data-sources.xml
struts-forms.xml
global-exceptions.xml
global-forwards.xml
struts-actions.xml
actions.xml
struts-controller.xml
struts-message-resources.xml
struts-plugins.xml

validation.xml
====================================
validation-global.xml

