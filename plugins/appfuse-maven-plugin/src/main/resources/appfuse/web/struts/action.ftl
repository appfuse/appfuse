<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import com.opensymphony.xwork2.Preparable;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${appfusepackage}.dao.SearchException;
import ${pojo.packageName}.${pojo.shortName};
import ${basepackage}.webapp.action.BaseAction;

import java.util.List;

public class ${pojo.shortName}Action extends BaseAction implements Preparable {
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
    private List ${util.getPluralForWord(pojoNameLower)};
    private ${pojo.shortName} ${pojoNameLower};
    private ${identifierType} ${pojo.identifierProperty.name};
    private String query;

<#if genericcore>
    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager) {
<#else>
    public void set${pojo.shortName}Manager(${pojo.shortName}Manager ${pojoNameLower}Manager) {
</#if>
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    public List get${util.getPluralForWord(pojo.shortName)}() {
        return ${util.getPluralForWord(pojoNameLower)};
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            String ${pojoNameLower}Id = getRequest().getParameter("${pojoNameLower}.${pojo.identifierProperty.name}");
            if (${pojoNameLower}Id != null && !${pojoNameLower}Id.equals("")) {
                ${pojoNameLower} = ${pojoNameLower}Manager.get(new ${identifierType}(${pojoNameLower}Id));
            }
        }
    }

    public void setQ(String q) {
        this.query = q;
    }

    public String list() {
        try {
            ${util.getPluralForWord(pojoNameLower)} = ${pojoNameLower}Manager.search(query, ${pojo.shortName}.class);
        } catch (SearchException se) {
            addActionError(se.getMessage());
            ${util.getPluralForWord(pojoNameLower)} = ${pojoNameLower}Manager.getAll();
        }
        return SUCCESS;
    }

    public void ${setIdMethodName}(${identifierType} ${pojo.identifierProperty.name}) {
        this.${pojo.identifierProperty.name} = ${pojo.identifierProperty.name};
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