package ${basepackage}.dao.hibernate;

import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.dao.${pojo.shortName}Dao;
import ${appfusepackage}.dao.hibernate.GenericDaoHibernate;

public class ${pojo.shortName}DaoHibernate extends GenericDaoHibernate<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> implements ${pojo.shortName}Dao {

    public ${pojo.shortName}DaoHibernate() {
        super(${pojo.shortName}.class);
    }
}
