package ${basepackage}.webapp.action;

import java.io.Serializable;
import java.util.List;

import org.appfuse.webapp.action.BasePage;
import org.appfuse.service.GenericManager;

public class ${pojo.shortName}List extends BasePage implements Serializable {
    private GenericManager ${pojo.shortName.toLowerCase()}Manager;

    public void set${pojo.shortName}Manager(GenericManager manager) {
        this.${pojo.shortName.toLowerCase()}Manager = manager;
    }

    public ${pojo.shortName}List() {
        setSortColumn("id"); // sets the default sort column
    }

    public List get${pojo.shortName}s() {
        return sort(${pojo.shortName.toLowerCase()}Manager.getAll());
    }
}

