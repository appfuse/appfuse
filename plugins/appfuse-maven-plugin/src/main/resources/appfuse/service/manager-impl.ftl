<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.service.impl;

import ${basepackage}.dao.${pojo.shortName}Dao;
import ${basepackage}.model.${pojo.shortName};
import ${basepackage}.service.${pojo.shortName}Manager;
import ${appfusepackage}.service.impl.GenericManagerImpl;

import java.util.List;
import javax.jws.WebService;

@WebService(serviceName = "${pojo.shortName}Service", endpointInterface = "${basepackage}.service.${pojo.shortName}Manager")
public class ${pojo.shortName}ManagerImpl extends GenericManagerImpl<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> implements ${pojo.shortName}Manager {
    ${pojo.shortName}Dao ${pojoNameLower}Dao;

    public ${pojo.shortName}ManagerImpl(${pojo.shortName}Dao ${pojoNameLower}Dao) {
        super(${pojoNameLower}Dao);
        this.${pojoNameLower}Dao = ${pojoNameLower}Dao;
    }
}