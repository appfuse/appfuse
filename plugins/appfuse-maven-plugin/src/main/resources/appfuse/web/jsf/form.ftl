<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import java.io.Serializable;

import ${pojo.packageName}.${pojo.shortName};
import ${basepackage}.webapp.action.BasePage;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("${pojoNameLower}Form")
@Scope("request")
public class ${pojo.shortName}Form extends BasePage implements Serializable {
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
    private ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
    private ${identifierType} ${pojo.identifierProperty.name};

    @Autowired
<#if genericcore>
    public void set${pojo.shortName}Manager(@Qualifier("${pojoNameLower}Manager") GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager) {
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

    public void ${setIdMethodName}(${identifierType} ${pojo.identifierProperty.name}) {
        this.${pojo.identifierProperty.name} = ${pojo.identifierProperty.name};
    }

    public String delete() {
        ${pojoNameLower}Manager.remove(${pojoNameLower}.${getIdMethodName}());
        addMessage("${pojoNameLower}.deleted");

        return "list";
    }

    public String edit() {
        // Workaround for not being able to set the id using ${'#'}{param.id} when using Spring-configured managed-beans
        if (${pojo.identifierProperty.name} == null) {
            ${pojo.identifierProperty.name} = new ${identifierType}(getParameter("${pojo.identifierProperty.name}"));
        }
        // Comparison to zero (vs. null) is required with MyFaces 1.2.2, not with previous versions
        if (${pojo.identifierProperty.name} != null && ${pojo.identifierProperty.name} != 0) {
            ${pojoNameLower} = ${pojoNameLower}Manager.get(${pojo.identifierProperty.name});
        } else {
            ${pojoNameLower} = new ${pojo.shortName}();
        }

        return "edit";
    }

    public String save() {
        boolean isNew = (${pojoNameLower}.${getIdMethodName}() == null || ${pojoNameLower}.${getIdMethodName}() == 0);
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