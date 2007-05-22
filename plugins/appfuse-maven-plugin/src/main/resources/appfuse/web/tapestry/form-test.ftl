<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.pages;

import org.apache.tapestry.engine.ILink;
import ${basepackage}.model.${pojo.shortName};
import ${appfusepackage}.webapp.pages.MockRequestCycle;
import ${appfusepackage}.webapp.pages.BasePageTestCase;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>

import java.util.HashMap;
import java.util.Map;

public class ${pojo.shortName}FormTest extends BasePageTestCase {
    private ${pojo.shortName}Form page;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        // these can be mocked if you want a more "pure" unit test
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("${pojoNameLower}Manager", applicationContext.getBean("${pojoNameLower}Manager"));
        page = (${pojo.shortName}Form) getPage(${pojo.shortName}Form.class, map);
    }

    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        page = null;
    }

    public void testAdd() throws Exception {
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();

        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)}<#rt/>
            <#if field.value.typeName == "java.lang.String" && column.isUnique()><#lt/> + Math.random()</#if><#lt/>);
        </#if>
    </#foreach>
</#foreach>
        
        page.set${pojo.shortName}(${pojoNameLower});

        ILink link = page.save(new MockRequestCycle(this.getClass().getPackage().getName()));
        assertNotNull(page.get${pojo.shortName}());
        assertFalse(page.hasErrors());
        assertEquals("${pojo.shortName}List" + EXTENSION, link.getURL());
    }

    @SuppressWarnings("unchecked")
    public void testSave() {
        <#if genericcore>
        GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager = (GenericManager) applicationContext.getBean("${pojoNameLower}Manager");
        <#else>
        ${pojo.shortName}Manager ${pojoNameLower}Manager = (${pojo.shortName}Manager) applicationContext.getBean("${pojoNameLower}Manager"); 
        </#if>

        ${pojo.shortName} ${pojoNameLower} = ${pojoNameLower}Manager.get(1L);

        // update required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)});
        </#if>
    </#foreach>
</#foreach>
        
        page.set${pojo.shortName}(${pojoNameLower});

        ILink link = page.save(new MockRequestCycle(this.getClass().getPackage().getName()));
        assertNotNull(page.get${pojo.shortName}());
        assertFalse(page.hasErrors());
        assertNull(link);
    }

    public void testRemove() throws Exception {
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.${setIdMethodName}(2L);
        page.set${pojo.shortName}(${pojoNameLower});
        page.delete(new MockRequestCycle(this.getClass().getPackage().getName()));
        assertFalse(page.hasErrors());
    }
}