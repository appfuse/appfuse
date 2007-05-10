package ${basepackage}.webapp.controller;

import org.apache.commons.lang.StringUtils;
import org.appfuse.service.GenericManager;
import ${basepackage}.model.${pojo.shortName};
import org.appfuse.webapp.controller.BaseFormController;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class ${pojo.shortName}FormController extends BaseFormController {
    private GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager = null;

    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager) {
        this.${pojo.shortName.toLowerCase()}Manager = ${pojo.shortName.toLowerCase()}Manager;
    }

    public ${pojo.shortName}FormController() {
        setCommandClass(${pojo.shortName}.class);
        setCommandName("${pojo.shortName.toLowerCase()}");
    }

    protected Object formBackingObject(HttpServletRequest request)
    throws Exception {
        String id = request.getParameter("id");

        if (!StringUtils.isBlank(id)) {
            return ${pojo.shortName.toLowerCase()}Manager.get(new Long(id));
        }

        return new ${pojo.shortName}();
    }

    public ModelAndView onSubmit(HttpServletRequest request,
                                 HttpServletResponse response, Object command,
                                 BindException errors)
    throws Exception {
        log.debug("entering 'onSubmit' method...");

        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = (${pojo.shortName}) command;
        boolean isNew = (${pojo.shortName.toLowerCase()}.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();

        if (request.getParameter("delete") != null) {
            ${pojo.shortName.toLowerCase()}Manager.remove(${pojo.shortName.toLowerCase()}.getId());
            saveMessage(request, getText("${pojo.shortName.toLowerCase()}.deleted", locale));
        } else {
            ${pojo.shortName.toLowerCase()}Manager.save(${pojo.shortName.toLowerCase()});
            String key = (isNew) ? "${pojo.shortName.toLowerCase()}.added" : "${pojo.shortName.toLowerCase()}.updated";
            saveMessage(request, getText(key, locale));

            if (!isNew) {
                success = "redirect:${pojo.shortName.toLowerCase()}form.html?id=" + ${pojo.shortName.toLowerCase()}.getId();
            }
        }

        return new ModelAndView(success);
    }
}