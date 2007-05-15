<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.action;

import java.io.Serializable;

import ${pojo.packageName}.${pojo.shortName};
import ${appfusepackage}.webapp.action.BasePage;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>

public class ${pojo.shortName}Form extends BasePage implements Serializable {
<#if genericcore>
    private GenericManager<${pojo.shortName}, Long> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
    private ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
    private Long id;

<#if genericcore>
    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, Long> ${pojoNameLower}Manager) {
<#else>
    public void set${pojo.shortName}Manager(${pojo.shortName}Manager ${pojoNameLower}Manager) {
</#if>
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    public ${pojo.shortName} get${pojo.shortName}() {
        return ${pojoNameLower};
    }

    public void set${pojo.shortName}(${pojo.shortName} ${pojoNameLower}) {
        this.${pojoNameLower} = ${pojoNameLower};
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String delete() {
        ${pojoNameLower}Manager.remove(${pojoNameLower}.getId());
        addMessage("${pojoNameLower}.deleted");

        return "list";
    }

    public String edit() {
        if (id != null) {
            ${pojoNameLower} = ${pojoNameLower}Manager.get(id);
        } else {
            ${pojoNameLower} = new ${pojo.shortName}();
        }

        return "edit";
    }

    public String save() {
        boolean isNew = (${pojoNameLower}.getId() == null);
        ${pojoNameLower}Manager.save(${pojoNameLower});

        String key = (isNew) ? "${pojoNameLower}.added" : "${pojoNameLower}.updated";
        addMessage(key);

        if (isNew) {
            return "list";
        } else {
            return "edit";
        }
    }
} 