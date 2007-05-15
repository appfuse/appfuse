package ${basepackage}.dao.jpa;

import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.dao.${pojo.shortName}Dao;
import ${appfusepackage}.dao.hibernate.GenericDaoJpa;

public class ${pojo.shortName}DaoJpa extends GenericDaoJpa<${pojo.shortName}, Long> implements ${pojo.shortName}Dao {

    public ${pojo.shortName}DaoJpa() {
        super(${pojo.shortName}.class);
    }
}
