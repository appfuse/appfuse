<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.action;

import ${pojo.packageName}.${pojo.shortName};
import ${appfusepackage}.webapp.action.BasePageTestCase;

public class ${pojo.shortName}FormTest extends BasePageTestCase {
    private ${pojo.shortName}Form bean;

    protected void setUp() throws Exception {
        super.setUp();
        bean = (${pojo.shortName}Form) getManagedBean("${pojoNameLower}Form");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        bean = null;
    }

    public void testAdd() throws Exception {
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        // set required fields
        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)}<#rt/>
            <#if field.value.typeName == "java.lang.String" && column.isUnique()><#lt/> + Math.random()</#if><#lt/>);
        </#if>
    </#foreach>
</#foreach>
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("list", bean.save());
        assertFalse(bean.hasErrors());
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        bean.setId(1L);

        assertEquals("edit", bean.edit());
        assertNotNull(bean.get${pojo.shortName}());
        assertFalse(bean.hasErrors());
    }

    public void testSave() {
        bean.setId(1L);

        assertEquals("edit", bean.edit());
        assertNotNull(bean.get${pojo.shortName}());
        ${pojo.shortName} ${pojoNameLower} = bean.get${pojo.shortName}();

        // update fields
        // update required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)});
        </#if>
    </#foreach>
</#foreach>
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("edit", bean.save());
        assertFalse(bean.hasErrors());
    }

    public void testRemove() throws Exception {
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.setId(2L);
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("list", bean.delete());
        assertFalse(bean.hasErrors());
    }
}