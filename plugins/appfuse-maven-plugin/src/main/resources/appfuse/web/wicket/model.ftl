<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
    <#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
    <#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import ${pojo.packageName}.${pojo.shortName};

import org.apache.wicket.model.LoadableDetachableModel;

public class ${pojo.shortName}Model extends LoadableDetachableModel {
    <#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
    <#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
    </#if>
    private ${identifierType} ${pojo.identifierProperty.name};

    public ${pojo.shortName}Model(${managerClass} ${pojoNameLower}Manager) {
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    /**
    * @param ${pojoNameLower} object this model will represent
    * @param ${pojoNameLower}Manager the ${pojoNameLower}Manager
    */
    public ${pojo.shortName}Model(${pojo.shortName} ${pojoNameLower}, ${managerClass} ${pojoNameLower}Manager) {
        super(${pojoNameLower});
        this.${pojo.identifierProperty.name} = ${pojoNameLower}.${getIdMethodName}();
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    protected Object load() {
        return ${pojoNameLower}Manager.get(${pojo.identifierProperty.name});
    }
}
