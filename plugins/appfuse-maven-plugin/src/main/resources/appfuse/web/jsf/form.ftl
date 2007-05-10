package ${basepackage}.webapp.action;

import java.io.Serializable;
import ${basepackage}.model.${pojo.shortName};
import org.appfuse.webapp.action.BasePage;
import org.appfuse.service.GenericManager;

public class ${pojo.shortName}Form extends BasePage implements Serializable {
    private GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager;
    private ${pojo.shortName} ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
    private Long id;

    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, Long> manager) {
        this.${pojo.shortName.toLowerCase()}Manager = manager;
    }

    public ${pojo.shortName} get${pojo.shortName}() {
        return ${pojo.shortName.toLowerCase()};
    }

    public void set${pojo.shortName}(${pojo.shortName} ${pojo.shortName.toLowerCase()}) {
        this.${pojo.shortName.toLowerCase()} = ${pojo.shortName.toLowerCase()};
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String delete() {
        ${pojo.shortName.toLowerCase()}Manager.remove(${pojo.shortName.toLowerCase()}.getId());
        addMessage("${pojo.shortName.toLowerCase()}.deleted");

        return "list";
    }

    public String edit() {
        if (id != null) {
            ${pojo.shortName.toLowerCase()} = ${pojo.shortName.toLowerCase()}Manager.get(id);
        } else {
            ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
        }

        return "edit";
    }

    public String save() {
        boolean isNew = (${pojo.shortName.toLowerCase()}.getId() == null);
        ${pojo.shortName.toLowerCase()}Manager.save(${pojo.shortName.toLowerCase()});

        String key = (isNew) ? "${pojo.shortName.toLowerCase()}.added" : "${pojo.shortName.toLowerCase()}.updated";
        addMessage(key);

        if (isNew) {
            return "list";
        } else {
            return "edit";
        }
    }
} 