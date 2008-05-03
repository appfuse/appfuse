<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.controller;

import org.apache.commons.lang.StringUtils;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.model.${pojo.shortName};
import ${appfusepackage}.webapp.controller.BaseFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class ${pojo.shortName}FormController extends BaseFormController {
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager = null;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager = null;
</#if>

<#if genericcore>
    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager) {
<#else>
    public void set${pojo.shortName}Manager(${pojo.shortName}Manager ${pojoNameLower}Manager) {
</#if>
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    public ${pojo.shortName}FormController() {
        setCommandClass(${pojo.shortName}.class);
        setCommandName("${pojoNameLower}");
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String ${pojo.identifierProperty.name} = request.getParameter("${pojo.identifierProperty.name}");

        if (!StringUtils.isBlank(${pojo.identifierProperty.name})) {
            return ${pojoNameLower}Manager.get(new ${identifierType}(${pojo.identifierProperty.name}));
        }

        return new ${pojo.shortName}();
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        log.debug("entering 'onSubmit' method...");

        ${pojo.shortName} ${pojoNameLower} = (${pojo.shortName}) command;
        boolean isNew = (${pojoNameLower}.${getIdMethodName}() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            ${pojoNameLower}Manager.remove(${pojoNameLower}.${getIdMethodName}());
            saveMessage(request, getText("${pojoNameLower}.deleted", locale));
        } else {
            ${pojoNameLower}Manager.save(${pojoNameLower});
            String key = (isNew) ? "${pojoNameLower}.added" : "${pojoNameLower}.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                success = "redirect:${pojoNameLower}form.html?${pojo.identifierProperty.name}=" + ${pojoNameLower}.${getIdMethodName}();
            }
        }

        return new ModelAndView(success);
    }
}
