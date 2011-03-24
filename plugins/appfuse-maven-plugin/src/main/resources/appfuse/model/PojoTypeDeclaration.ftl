<#include "Ejb3TypeDeclaration.ftl"/>
@Searchable
@XmlRootElement
${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()} extends BaseObject implements Serializable