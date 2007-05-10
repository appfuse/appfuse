package ${basepackage}.webapp.action;

import org.appfuse.service.GenericManager;
import ${basepackage}.model.${pojo.shortName};
import org.appfuse.webapp.action.BaseAction;

import java.util.List;

public class ${pojo.shortName}Action extends BaseAction {
    private GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager;
    private List ${pojo.shortName.toLowerCase()}s;
    private ${pojo.shortName} ${pojo.shortName.toLowerCase()};
    private Long id;

    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager) {
        this.${pojo.shortName.toLowerCase()}Manager = ${pojo.shortName.toLowerCase()}Manager;
    }

    public List get${pojo.shortName}s() {
        return ${pojo.shortName.toLowerCase()}s;
    }

    public String list() {
        ${pojo.shortName.toLowerCase()}s = ${pojo.shortName.toLowerCase()}Manager.getAll();
        return SUCCESS;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ${pojo.shortName} get${pojo.shortName}() {
        return ${pojo.shortName.toLowerCase()};
    }

    public void set${pojo.shortName}(${pojo.shortName} ${pojo.shortName.toLowerCase()}) {
        this.${pojo.shortName.toLowerCase()} = ${pojo.shortName.toLowerCase()};
    }

    public String delete() {
        ${pojo.shortName.toLowerCase()}Manager.remove(${pojo.shortName.toLowerCase()}.getId());
        saveMessage(getText("${pojo.shortName.toLowerCase()}.deleted"));

        return SUCCESS;
    }

    public String edit() {
        if (id != null) {
            ${pojo.shortName.toLowerCase()} = ${pojo.shortName.toLowerCase()}Manager.get(id);
        } else {
            ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
        }

        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }

        if (delete != null) {
            return delete();
        }

        boolean isNew = (${pojo.shortName.toLowerCase()}.getId() == null);

        ${pojo.shortName.toLowerCase()}Manager.save(${pojo.shortName.toLowerCase()});

        String key = (isNew) ? "${pojo.shortName.toLowerCase()}.added" : "${pojo.shortName.toLowerCase()}.updated";
        saveMessage(getText(key));

        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}