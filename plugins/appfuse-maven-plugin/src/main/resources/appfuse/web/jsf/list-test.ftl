<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.webapp.action;

import org.compass.gps.CompassGps;
import org.springframework.beans.factory.annotation.Autowired;

import ${basepackage}.webapp.action.BasePageTestCase;
<#if genericcore>
import ${appfusepackage}.service.GenericManager;
<#else>
import ${basepackage}.service.${pojo.shortName}Manager;
</#if>
import ${basepackage}.model.${pojo.shortName};

public class ${pojo.shortName}ListTest extends BasePageTestCase {
    private ${pojo.shortName}List bean;
<#if genericcore>
    @Autowired
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager;
<#else>
    @Autowired
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>

    @Autowired
    private CompassGps compassGps;
        
    @Override @SuppressWarnings("unchecked")
    protected void onSetUp() throws Exception {
        super.onSetUp();
        bean = new ${pojo.shortName}List();
        bean.set${pojo.shortName}Manager(${pojoNameLower}Manager);

        compassGps.index();

        // add a test ${pojoNameLower} to the database
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();

        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>

        ${pojoNameLower}Manager.save(${pojoNameLower});
    }

    @Override
    protected void onTearDown() throws Exception {
        super.onTearDown();
        bean = null;
    }

    public void testGetAll${util.getPluralForWord(pojo.shortName)}() throws Exception {
        assertTrue(bean.get${util.getPluralForWord(pojo.shortName)}().size() >= 1);
        assertFalse(bean.hasErrors());
    }

    public void testSearch() throws Exception {
        bean.setQuery("*");
        assertEquals("success", bean.search());
        assertTrue(bean.get${util.getPluralForWord(pojo.shortName)}().size() == 4);
    }
}