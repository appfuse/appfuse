<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.model.${pojo.shortName};
import ${appfusepackage}.webapp.action.BaseAction;

import java.util.List;

public class ${pojo.shortName}Action extends BaseAction {
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
    private List ${pojoNameLower}s;
    private ${pojo.shortName} ${pojoNameLower};
    private ${identifierType}  ${pojo.identifierProperty.name};

<#if genericcore>
    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager) {
<#else>
    public void set${pojo.shortName}Manager(${pojo.shortName}Manager ${pojoNameLower}Manager) {
</#if>
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    public List get${pojo.shortName}s() {
        return ${pojoNameLower}s;
    }

    public String list() {
        ${pojoNameLower}s = ${pojoNameLower}Manager.getAll();
        return SUCCESS;
    }

    public void ${setIdMethodName}(${identifierType}  ${pojo.identifierProperty.name}) {
        this. ${pojo.identifierProperty.name} =  ${pojo.identifierProperty.name};
    }

    public ${pojo.shortName} get${pojo.shortName}() {
        return ${pojoNameLower};
    }

    public void set${pojo.shortName}(${pojo.shortName} ${pojoNameLower}) {
        this.${pojoNameLower} = ${pojoNameLower};
    }

    public String delete() {
        ${pojoNameLower}Manager.remove(${pojoNameLower}.${getIdMethodName}());
        saveMessage(getText("${pojoNameLower}.deleted"));

        return SUCCESS;
    }

    public String edit() {
        if (${pojo.identifierProperty.name} != null) {
            ${pojoNameLower} = ${pojoNameLower}Manager.get(${pojo.identifierProperty.name});
        } else {
            ${pojoNameLower} = new ${pojo.shortName}();
        }

        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = (${pojoNameLower}.${getIdMethodName}() == null);

        ${pojoNameLower}Manager.save(${pojoNameLower});

        String key = (isNew) ? "${pojoNameLower}.added" : "${pojoNameLower}.updated";
        saveMessage(getText(key));

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}