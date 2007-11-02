<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
package ${basepackage}.dao;

import ${appfusepackage}.dao.BaseDaoTestCase;
import ${basepackage}.model.${pojo.shortName};
import <#if daoframework == "jpa">javax.persistence.EntityNotFoundException<#else>org.springframework.dao.DataAccessException</#if>;

import java.util.List;

public class ${pojo.shortName}DaoTest extends BaseDaoTestCase {
    private ${pojo.shortName}Dao ${pojoNameLower}Dao;

    public void set${pojo.shortName}Dao(${pojo.shortName}Dao ${pojoNameLower}Dao) {
        this.${pojoNameLower}Dao = ${pojoNameLower}Dao;
    }

    public void testAddAndRemove${pojo.shortName}() throws Exception {
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();

        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>

        log.debug("adding ${pojoNameLower}...");
        ${pojoNameLower} = ${pojoNameLower}Dao.save(${pojoNameLower});
        <#lt/><#if daoframework == "daoframework">flush();</#if><#rt/>

        ${pojoNameLower} = ${pojoNameLower}Dao.get(${pojoNameLower}.${getIdMethodName}());

        assertNotNull(${pojoNameLower}.${getIdMethodName}());

        log.debug("removing ${pojoNameLower}...");

        ${pojoNameLower}Dao.remove(${pojoNameLower}.${getIdMethodName}());
        <#lt/><#if daoframework == "daoframework">flush();</#if><#rt/>

        try {
            ${pojoNameLower}Dao.get(${pojoNameLower}.${getIdMethodName}());
            fail("${pojo.shortName} found in database");
        } catch (<#if daoframework == "jpa">EntityNotFoundException<#else>DataAccessException</#if> e) {
            log.debug("Expected exception: " + e.getMessage());
            assertNotNull(e);
        }
    }
}