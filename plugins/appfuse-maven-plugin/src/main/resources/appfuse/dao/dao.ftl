package ${basepackage}.dao;

<#assign classbody>
<#assign pojoName = pojo.importType(pojo.getDeclarationName())>
import ${pojo.packageName}.${pojoName};

/**
 * An interface that provides a data management interface to the ${pojoName} table.
 */
public interface ${pojoName}Dao extends ${pojo.importType("${appfusepackage}.dao.GenericDao")}<${pojoName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> {
</#assign>
${pojo.generateImports()
}
${classbody}
}