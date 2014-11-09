<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${pojo.packageName}.${pojo.shortName};
import ${basepackage}.webapp.action.BasePageTestCase;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.transaction.Transactional;

@Transactional
public class ${pojo.shortName}FormTest extends BasePageTestCase {
    private ${pojo.shortName}Form bean;
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
        bean = new ${pojo.shortName}Form();
        bean.set${pojo.shortName}Manager(${pojoNameLower}Manager);
    }

    @After
    public void onTearDown() {
        super.onTearDown();
        bean = null;
    }

    @Test
    public void testAdd() throws Exception {
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
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("list", bean.save());
        assertFalse(bean.hasErrors());
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        bean.${setIdMethodName}(-1L);

        assertEquals("edit", bean.edit());
        assertNotNull(bean.get${pojo.shortName}());
        assertFalse(bean.hasErrors());
    }

    @Test
    public void testSave() {
        log.debug("testing save...");
        bean.${setIdMethodName}(-1L);

        assertEquals("edit", bean.edit());
        assertNotNull(bean.get${pojo.shortName}());
        ${pojo.shortName} ${pojoNameLower} = bean.get${pojo.shortName}();

        // update required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("edit", bean.save());
        assertFalse(bean.hasErrors());
    }

    @Test
    public void testRemove() throws Exception {
        log.debug("testing remove...");
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.${setIdMethodName}(-2L);
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("list", bean.delete());
        assertFalse(bean.hasErrors());
    }
}