package ${basepackage}.dao.hibernate;

import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.dao.${pojo.shortName}Dao;
import ${appfusepackage}.dao.hibernate.GenericDaoHibernate;

public class ${pojo.shortName}DaoHibernate extends GenericDaoHibernate<${pojo.shortName}, Long> implements ${pojo.shortName}Dao {

    public ${pojo.shortName}DaoHibernate() {
        super(${pojo.shortName}.class);
    }
}
