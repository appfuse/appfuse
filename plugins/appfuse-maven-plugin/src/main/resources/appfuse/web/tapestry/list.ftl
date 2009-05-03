<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import java.util.List;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};
import ${basepackage}.webapp.pages.BasePage;
import ${basepackage}.webapp.pages.MainMenu;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.EventLink;
import org.slf4j.Logger;

public class ${pojo.shortName}List extends BasePage {
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

    @Component(parameters = {"event=add"})
    private EventLink addTop, addBottom;

    @Component(parameters = {"event=done"})
    private EventLink doneTop, doneBottom;

    public List<${pojo.shortName}> get${util.getPluralForWord(pojo.shortName)}() {
        return ${pojoNameLower}Manager.getAll();
    }

    Object onAdd() {
        form.set${pojo.shortName}(new ${pojo.shortName}());
        return form;
    }

    Object onDone() {
        return MainMenu.class;
    }

    Object onActionFromEdit(${identifierType} ${pojo.identifierProperty.name}) {
        log.debug("fetching ${pojoNameLower} with ${pojo.identifierProperty.name}: {}", ${pojo.identifierProperty.name});
        return form;
    }
}