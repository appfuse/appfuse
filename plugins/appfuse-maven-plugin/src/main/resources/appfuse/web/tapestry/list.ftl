<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import java.util.List;

import ${appfusepackage}.dao.SearchException;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.slf4j.Logger;

public class ${pojo.shortName}List {
    @Inject
    private Logger log;

    @Inject
    @Service("${pojoNameLower}Manager")
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>

    @Property
    private ${pojo.shortName} ${pojoNameLower};

    @InjectPage
    private ${pojo.shortName}Form form;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String q;

    @Property
    private String errorMessage;

    public List<${pojo.shortName}> get${util.getPluralForWord(pojo.shortName)}() {
        try {
            return ${pojoNameLower}Manager.search(q, ${pojo.shortName}.class);
        } catch (SearchException se) {
            errorMessage = se.getMessage();
            return ${pojoNameLower}Manager.getAll();
        }
    }

    Object onAdd() {
        form.set${pojo.shortName}(new ${pojo.shortName}());
        return form;
    }

    Object onDone() {
        return <#if useMainMenu>MainMenu<#else>Home</#if>.class;
    }

    Object onActionFromEdit(${identifierType} ${pojo.identifierProperty.name}) {
        log.debug("fetching ${pojoNameLower} with ${pojo.identifierProperty.name}: {}", ${pojo.identifierProperty.name});
        ${pojoNameLower} =  ${pojoNameLower}Manager.get(new ${identifierType} (${pojo.identifierProperty.name}));
        form.set${pojo.shortName}(${pojoNameLower});
        return form;
    }

    Object onSubmit() {
        return this;
    }
}
