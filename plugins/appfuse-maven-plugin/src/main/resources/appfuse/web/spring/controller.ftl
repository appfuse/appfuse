<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.model.${pojo.shortName};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/${util.getPluralForWord(pojoNameLower)}.*")
public class ${pojo.shortName}Controller {
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

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        return new ModelAndView().addObject(${pojoNameLower}Manager.getAll());
    }
}
