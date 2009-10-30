<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.dao.ibatis;

import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.dao.${pojo.shortName}Dao;
import ${appfusepackage}.dao.ibatis.GenericDaoiBatis;
import org.springframework.stereotype.Repository;

@Repository("${pojoNameLower}Dao")
public class ${pojo.shortName}DaoiBatis extends GenericDaoiBatis<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> implements ${pojo.shortName}Dao {

    public ${pojo.shortName}DaoiBatis() {
        super(${pojo.shortName}.class);
    }
}
