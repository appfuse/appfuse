package ${basepackage}.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.GenericManager;
import ${basepackage}.model.${pojo.shortName};

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ${pojo.shortName}Controller implements Controller {
    private final Log log = LogFactory.getLog(${pojo.shortName}Controller.class);
    private GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager = null;

    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager) {
        this.${pojo.shortName.toLowerCase()}Manager = ${pojo.shortName.toLowerCase()}Manager;
    }

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
        log.debug("entering 'handleRequest' method...");

        return new ModelAndView().addObject(${pojo.shortName.toLowerCase()}Manager.getAll());
    }
}
