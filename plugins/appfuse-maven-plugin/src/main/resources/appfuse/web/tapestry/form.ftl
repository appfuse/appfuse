<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};
import ${appfusepackage}.webapp.pages.BasePage;

public abstract class ${pojo.shortName}Form extends BasePage implements PageBeginRenderListener {
<#if genericcore>
    public abstract GenericManager<${pojo.shortName}, ${identifierType}> get${pojo.shortName}Manager();
<#else>
    public abstract ${pojo.shortName}Manager get${pojo.shortName}Manager();
</#if>
    public abstract void set${pojo.shortName}(${pojo.shortName} ${pojoNameLower});
    public abstract ${pojo.shortName} get${pojo.shortName}();

    public void pageBeginRender(PageEvent event) {
        if (get${pojo.shortName}() == null) {
            set${pojo.shortName}(new ${pojo.shortName}());
        }
    }

    public ILink cancel(IRequestCycle cycle) {
        log.debug("Entering 'cancel' method...");
        return getEngineService().getLink(false, "${pojo.shortName}List");
    }

    public ILink delete(IRequestCycle cycle) {
        log.debug("entered 'delete' method...");

        get${pojo.shortName}Manager().remove(get${pojo.shortName}().${getIdMethodName}());

        ${pojo.shortName}List nextPage = (${pojo.shortName}List) cycle.getPage("${pojo.shortName}List");
        nextPage.setMessage(getText("${pojoNameLower}.deleted"));
        return getEngineService().getLink(false, nextPage.getPageName());
    }

    public ILink save(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return null;
        }

        boolean isNew = (get${pojo.shortName}().${getIdMethodName}() == null);

        get${pojo.shortName}Manager().save(get${pojo.shortName}());

        String key = (isNew) ? "${pojoNameLower}.added" : "${pojoNameLower}.updated";

        if (isNew) {
            ${pojo.shortName}List nextPage = (${pojo.shortName}List) cycle.getPage("${pojo.shortName}List");
            nextPage.setMessage(getText(key));
            return getEngineService().getLink(false, nextPage.getPageName());
        } else {
            setMessage(getText(key));
            return null; // return to current page
        }
    }
}