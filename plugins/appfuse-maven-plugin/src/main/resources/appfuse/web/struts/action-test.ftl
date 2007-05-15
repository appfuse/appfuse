<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
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
import ${appfusepackage}.webapp.action.BaseActionTestCase;
import org.springframework.mock.web.MockHttpServletRequest;

public class ${pojo.shortName}ActionTest extends BaseActionTestCase {
    private ${pojo.shortName}Action action;

    public void set${pojo.shortName}Action(${pojo.shortName}Action action) {
        this.action = action;
    }

    @Override @SuppressWarnings("unchecked")
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        ${managerClass} ${pojoNameLower}Manager = (${managerClass}) applicationContext.getBean("${pojoNameLower}Manager");

        // add a test ${pojoNameLower} to the database
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

        ${pojoNameLower}Manager.save(${pojoNameLower});
    }

    public void testSearch() throws Exception {
        assertEquals(action.list(), ActionSupport.SUCCESS);
        assertTrue(action.get${pojo.shortName}s().size() >= 1);
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(1L);
        assertNull(action.get${pojo.shortName}());
        assertEquals("success", action.edit());
        assertNotNull(action.get${pojo.shortName}());
        assertFalse(action.hasActionErrors());
    }

    public void testSave() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(1L);
        assertEquals("success", action.edit());
        assertNotNull(action.get${pojo.shortName}());

        ${pojo.shortName} ${pojoNameLower} = action.get${pojo.shortName}();
        // update required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)});
        </#if>
    </#foreach>
</#foreach>

        action.set${pojo.shortName}(${pojoNameLower});

        assertEquals("input", action.save());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }

    public void testRemove() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.setId(2L);
        action.set${pojo.shortName}(${pojoNameLower});
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
}