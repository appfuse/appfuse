<#include "Ejb3TypeDeclaration.ftl"/>
@Indexed
@XmlRootElement
${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()} extends BaseObject implements Serializable