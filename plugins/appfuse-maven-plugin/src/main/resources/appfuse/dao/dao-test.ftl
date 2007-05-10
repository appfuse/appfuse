<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethod = pojo.getGetterSignature(pojo.identifierProperty) + "()">
package ${basepackage}.dao;

import org.appfuse.dao.BaseDaoTestCase;
import ${basepackage}.model.${pojo.shortName};
import org.springframework.dao.DataAccessException;

import java.util.List;

public class ${pojo.shortName}DaoTest extends BaseDaoTestCase {
    private ${pojo.shortName}Dao ${pojo.shortName.toLowerCase()}Dao = null;

    public void set${pojo.shortName}Dao(${pojo.shortName}Dao ${pojo.shortName.toLowerCase()}Dao) {
        this.${pojo.shortName.toLowerCase()}Dao = ${pojo.shortName.toLowerCase()}Dao;
    }

    public void testAddAndRemove${pojo.shortName}() throws Exception {
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();

        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)}<#rt/>
            <#if field.value.typeName == "java.lang.String" && column.isUnique()><#lt/> + Math.random()</#if><#lt/>);
        </#if>
    </#foreach>
</#foreach>

        ${pojo.shortName.toLowerCase()} = ${pojo.shortName.toLowerCase()}Dao.save(${pojo.shortName.toLowerCase()});
        flush();

        ${pojo.shortName.toLowerCase()} = (${pojo.shortName}) ${pojo.shortName.toLowerCase()}Dao.get(${pojo.shortName.toLowerCase()}.getId());

        assertNotNull(${pojo.shortName.toLowerCase()}.${getIdMethod});

        log.debug("removing ${pojo.shortName.toLowerCase()}...");

        ${pojo.shortName.toLowerCase()}Dao.remove(${pojo.shortName.toLowerCase()}.${getIdMethod});
        flush();

        try {
            ${pojo.shortName.toLowerCase()}Dao.get(${pojo.shortName.toLowerCase()}.${getIdMethod});
            fail("${pojo.shortName} found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
}
