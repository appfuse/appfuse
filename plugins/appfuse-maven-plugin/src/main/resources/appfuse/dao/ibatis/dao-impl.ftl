package ${basepackage}.dao.ibatis;

import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.dao.${pojo.shortName}Dao;
import ${appfusepackage}.dao.hibernate.GenericDaoiBatis;

public class ${pojo.shortName}DaoiBatis extends GenericDaoiBatis<${pojo.shortName}, Long> implements ${pojo.shortName}Dao {

    public ${pojo.shortName}DaoiBatis() {
        super(${pojo.shortName}.class);
    }
}
