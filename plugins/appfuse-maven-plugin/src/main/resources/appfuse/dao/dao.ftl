package ${basepackage}.dao;

<#assign classbody>
<#assign pojoName = pojo.importType(pojo.getDeclarationName())>
import ${basepackage}.${pojoName};

/**
 * An interface that provides a data management interface to the ${pojoName} table.
 */
public interface ${pojoName}Dao extends ${pojo.importType("org.appfuse.dao.GenericDao")}<${pojoName}, Long> {
</#assign>
${pojo.generateImports()
}
${classbody}
}