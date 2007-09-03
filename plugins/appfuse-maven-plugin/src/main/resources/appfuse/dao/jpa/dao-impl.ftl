package ${basepackage}.dao.jpa;

import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.dao.${pojo.shortName}Dao;
import ${appfusepackage}.dao.jpa.GenericDaoJpa;

public class ${pojo.shortName}DaoJpa extends GenericDaoJpa<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> implements ${pojo.shortName}Dao {

    public ${pojo.shortName}DaoJpa() {
        super(${pojo.shortName}.class);
    }
}
