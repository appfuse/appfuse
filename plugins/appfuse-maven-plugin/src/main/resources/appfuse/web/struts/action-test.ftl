<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#assign managerClass = 'GenericManager'>
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
<#assign managerClass = pojo.shortName + 'Manager'>
</#if>
import ${pojo.packageName}.${pojo.shortName};
import ${basepackage}.webapp.action.BaseActionTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ${pojo.shortName}ActionTest extends BaseActionTestCase {
    private ${pojo.shortName}Action action;

    @Autowired
    private ${managerClass} ${pojoNameLower}Manager;

    @Before
    public void onSetUp() {
        super.onSetUp();

        action = new ${pojo.shortName}Action();
        action.set${pojo.shortName}Manager(${pojoNameLower}Manager);

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

    @Test
    public void testGetAll${util.getPluralForWord(pojo.shortName)}() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.get${util.getPluralForWord(pojo.shortName)}().size() >= 1);
    }

    @Test
    public void testSearch() throws Exception {
        // regenerate indexes
        ${pojoNameLower}Manager.reindex();

        action.setQ("*");
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertEquals(4, action.get${util.getPluralForWord(pojo.shortName)}().size());
    }

    @Test
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.${setIdMethodName}(-1L);
        assertNull(action.get${pojo.shortName}());
        assertEquals("success", action.edit());
        assertNotNull(action.get${pojo.shortName}());
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.${setIdMethodName}(-1L);
        assertEquals("success", action.edit());
        assertNotNull(action.get${pojo.shortName}());

        ${pojo.shortName} ${pojoNameLower} = action.get${pojo.shortName}();
        // update required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>

        action.set${pojo.shortName}(${pojoNameLower});

        assertEquals("input", action.save());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }

    @Test
    public void testRemove() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.${setIdMethodName}(-2L);
        action.set${pojo.shortName}(${pojoNameLower});
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}
