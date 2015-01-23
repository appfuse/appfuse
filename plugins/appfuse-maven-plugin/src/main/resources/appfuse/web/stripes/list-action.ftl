<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import java.util.List;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.model.${pojo.shortName};

@UrlBinding("/${util.getPluralForWord(pojoNameLower)}.action")
public class ${pojo.shortName}ListBean extends BaseActionBean {
    @SpringBean
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
    private List<${pojo.shortName}> ${util.getPluralForWord(pojoNameLower)};
    
    public List<${pojo.shortName}> get${util.getPluralForWord(pojo.shortName)}() {
        return ${util.getPluralForWord(pojoNameLower)};
    }

    @DefaultHandler
    public final Resolution execute() {
        ${util.getPluralForWord(pojoNameLower)} = ${pojoNameLower}Manager.getAll();
        return new ForwardResolution("/${pojoNameLower}List.jsp");
    }
}