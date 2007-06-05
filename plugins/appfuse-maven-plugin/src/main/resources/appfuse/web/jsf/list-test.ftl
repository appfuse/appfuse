<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.action;

import ${appfusepackage}.webapp.action.BasePageTestCase;
import ${appfusepackage}.service.GenericManager;
import ${basepackage}.model.${pojo.shortName};

public class ${pojo.shortName}ListTest extends BasePageTestCase {
    private ${pojo.shortName}List bean;

    @Override @SuppressWarnings("unchecked")
    protected void setUp() throws Exception {
        super.setUp();
        bean = (${pojo.shortName}List) getManagedBean("${pojoNameLower}List");
        GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}> ${pojoNameLower}Manager =
                (GenericManager<${pojo.shortName}, ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)}>) applicationContext.getBean("${pojoNameLower}Manager");

        // add a test ${pojoNameLower} to the database
        // add a test ${pojoNameLower} to the database
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();

        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)}<#rt/>
            <#if field.value.typeName == "java.lang.String" && column.isUnique()><#lt/> + Math.random()</#if><#lt/>);
        </#if>
    </#foreach>
</#foreach>

        ${pojoNameLower}Manager.save(${pojoNameLower});
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        bean = null;
    }

    public void testSearch() throws Exception {
        assertTrue(bean.get${pojo.shortName}s().size() >= 1);
        assertFalse(bean.hasErrors());
    }
}