<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.action;

import java.io.Serializable;
import java.util.List;

import ${appfusepackage}.dao.SearchException;
import ${pojo.packageName}.${pojo.shortName};
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.webapp.action.BasePage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("${pojoNameLower}List")
@Scope("session")
public class ${pojo.shortName}List extends BasePage implements Serializable {
    private String query;
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>

    @Autowired
<#if genericcore>
    public void set${pojo.shortName}Manager(@Qualifier("${pojoNameLower}Manager") GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager) {
<#else>
    public void set${pojo.shortName}Manager(${pojo.shortName}Manager ${pojoNameLower}Manager) {
</#if>
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public ${pojo.shortName}List() {
        setSortColumn("${pojo.identifierProperty.name}"); // sets the default sort column
    }

    public List<${pojo.shortName}> get${util.getPluralForWord(pojo.shortName)}() {
        try {
            return ${pojoNameLower}Manager.search(query, ${pojo.shortName}.class);
        } catch (SearchException se) {
            addError(se.getMessage());
            return sort(${pojoNameLower}Manager.getAll());
        }
    }

    public String search() {
        return "success";
    }
}