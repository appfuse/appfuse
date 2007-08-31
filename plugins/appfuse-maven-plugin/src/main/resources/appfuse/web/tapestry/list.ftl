<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.pages;

import java.util.List;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};
import ${appfusepackage}.webapp.pages.BasePage;
import org.apache.tapestry.IRequestCycle;

public abstract class ${pojo.shortName}List extends BasePage {
<#if genericcore>
    public abstract GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> get${pojo.shortName}Manager();
<#else>
    public abstract ${pojo.shortName}Manager get${pojo.shortName}Manager();
</#if>

    public List get${util.getPluralForWord(pojo.shortName)}() {
        return get${pojo.shortName}Manager().getAll();
    }

    public void edit(IRequestCycle cycle) {
        Object[] parameters = cycle.getListenerParameters();
        ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)} ${pojo.identifierProperty.name} = (${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}) parameters[0];

        if (log.isDebugEnabled()) {
            log.debug("fetching ${pojoNameLower} with ${pojo.identifierProperty.name}: " + ${pojo.identifierProperty.name});
        }

        ${pojo.shortName} person = get${pojo.shortName}Manager().get(${pojo.identifierProperty.name});

        ${pojo.shortName}Form nextPage = (${pojo.shortName}Form) cycle.getPage("${pojo.shortName}Form");
        nextPage.set${pojo.shortName}(person);
        cycle.activate(nextPage);
    }
}