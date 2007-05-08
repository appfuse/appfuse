package ${basepackage}.service.impl;

import ${basepackage}.dao.${pojo.shortName}Dao;
import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.service.${pojo.shortName}Manager;
import org.appfuse.service.impl.GenericManagerImpl;

import java.util.List;

public class ${pojo.shortName}ManagerImpl extends GenericManagerImpl<${pojo.shortName}, Long> implements ${pojo.shortName}Manager {
    ${pojo.shortName}Dao ${pojo.shortName.toLowerCase()}Dao;

    public ${pojo.shortName}ManagerImpl(${pojo.shortName}Dao ${pojo.shortName.toLowerCase()}Dao) {
        super(${pojo.shortName.toLowerCase()}Dao);
        this.${pojo.shortName.toLowerCase()}Dao = ${pojo.shortName.toLowerCase()}Dao;
    }
}
