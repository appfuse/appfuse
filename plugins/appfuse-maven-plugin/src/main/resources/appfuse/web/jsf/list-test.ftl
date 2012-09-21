<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import org.springframework.beans.factory.annotation.Autowired;

import ${basepackage}.webapp.action.BasePageTestCase;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.model.${pojo.shortName};

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ${pojo.shortName}ListTest extends BasePageTestCase {
    private ${pojo.shortName}List bean;
<#if genericcore>
    @Autowired
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager;
<#else>
    @Autowired
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>

    @Before
    public void onSetUp() {
        super.onSetUp();

        bean = new ${pojo.shortName}List();
        bean.set${pojo.shortName}Manager(${pojoNameLower}Manager);

        // add a test ${pojoNameLower} to the database
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();

        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(<#rt/>
            <#if field.value.typeName == "java.lang.String" && column.isUnique()><#lt/>${data.generateRandomStringValue(column)}<#else>${data.getValueForJavaTest(column)}</#if><#lt/>);
        </#if>
    </#foreach>
</#foreach>

        ${pojoNameLower}Manager.save(${pojoNameLower});
    }

    @After
    public void onTearDown() {
        super.onTearDown();
        bean = null;
    }

    @Test
    public void testGetAll${util.getPluralForWord(pojo.shortName)}() throws Exception {
        assertTrue(bean.get${util.getPluralForWord(pojo.shortName)}().size() >= 1);
        assertFalse(bean.hasErrors());
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
        ${pojoNameLower}Manager.reindex();

        bean.setQuery("*");
        assertEquals("success", bean.search());
        assertEquals(4, bean.get${util.getPluralForWord(pojo.shortName)}().size());
    }
}