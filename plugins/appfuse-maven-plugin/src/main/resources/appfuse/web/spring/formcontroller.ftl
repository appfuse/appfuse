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
import ${basepackage}.webapp.controller.BaseFormController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
@RequestMapping("/${pojoNameLower}form*")
public class ${pojo.shortName}FormController extends BaseFormController {
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager = null;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager = null;
</#if>

    @Autowired
<#if genericcore>
    public void set${pojo.shortName}Manager(@Qualifier("${pojoNameLower}Manager") GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager) {
<#else>
    public void set${pojo.shortName}Manager(${pojo.shortName}Manager ${pojoNameLower}Manager) {
</#if>
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    public ${pojo.shortName}FormController() {
        setCancelView("redirect:${util.getPluralForWord(pojoNameLower)}");
        setSuccessView("redirect:${util.getPluralForWord(pojoNameLower)}");
    }

    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected ${pojo.shortName} showForm(HttpServletRequest request)
    throws Exception {
        String ${pojo.identifierProperty.name} = request.getParameter("${pojo.identifierProperty.name}");

        if (!StringUtils.isBlank(${pojo.identifierProperty.name})) {
            return ${pojoNameLower}Manager.get(new ${identifierType}(${pojo.identifierProperty.name}));
        }

        return new ${pojo.shortName}();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(${pojo.shortName} ${pojoNameLower}, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }

        if (validator != null) { // validator is null during testing
            validator.validate(${pojoNameLower}, errors);

            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return "${pojoNameLower}form";
            }
        }

        log.debug("entering 'onSubmit' method...");

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
                success = "redirect:${pojoNameLower}form?${pojo.identifierProperty.name}=" + ${pojoNameLower}.${getIdMethodName}();
            }
        }

        return success;
    }
}
