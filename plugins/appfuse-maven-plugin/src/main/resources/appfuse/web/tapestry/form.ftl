<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};
import ${appfusepackage}.webapp.pages.BasePage;

public class ${pojo.shortName}Form extends BasePage {
    @Inject
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>

    @Persist
    private ${pojo.shortName} ${pojoNameLower};

    public ${pojo.shortName} get${pojo.shortName}() {
        return ${pojoNameLower};
    }

    /**
     * Allows setting ${pojoNameLower} object from another class (i.e. ${pojo.shortName}List)
     *
     * @param ${pojoNameLower} an initialized instance
     */
    public void set${pojo.shortName}(${pojo.shortName} ${pojoNameLower}) {
        this.${pojoNameLower} = ${pojoNameLower};
    }

    @InjectPage
    private ${pojo.shortName}List ${pojoNameLower}List;

    void onActivate(${identifierType} ${pojo.identifierProperty.name}) {
        if (${pojo.identifierProperty.name} != null) {
            ${pojoNameLower} = ${pojoNameLower}Manager.get(${pojo.identifierProperty.name});
        }
    }

    Object onSuccess() {
        boolean isNew = (get${pojo.shortName}().${getIdMethodName}() == null);

        ${pojoNameLower}Manager.save(${pojoNameLower});

        String key = (isNew) ? "${pojoNameLower}.added" : "${pojoNameLower}.updated";

        if (isNew) {
            ${pojoNameLower}List.addInfo(key, true);
            return ${pojoNameLower}List;
        } else {
            addInfo(key, true);
            return this;
        }
    }

    Object onDelete() {
        ${pojoNameLower}Manager.remove(${pojoNameLower}.${getIdMethodName}());
        ${pojoNameLower}List.addInfo("${pojoNameLower}.deleted", true);
        return ${pojoNameLower}List;
    }

    Object onCancel() {
        return ${pojoNameLower}List;
    }
}