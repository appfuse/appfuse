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
import ${appfusepackage}.webapp.action.BasePageTestCase;

public class ${pojo.shortName}FormTest extends BasePageTestCase {
    private ${pojo.shortName}Form bean;
<#if genericcore>
    private GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager;
<#else>
    private ${pojo.shortName}Manager ${pojoNameLower}Manager;
</#if>
        
<#if genericcore>
    public void set${pojo.shortName}Manager(GenericManager<${pojo.shortName}, ${identifierType}> ${pojoNameLower}Manager) {
<#else>
    public void set${pojo.shortName}Manager(${pojo.shortName}Manager ${pojoNameLower}Manager) {
</#if>
        this.${pojoNameLower}Manager = ${pojoNameLower}Manager;
    }

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        bean = new ${pojo.shortName}Form();
        bean.set${pojo.shortName}Manager(${pojoNameLower}Manager);
    }

    @Override
    protected void onTearDown() throws Exception {
        super.onTearDown();
        bean = null;
    }

    public void testAdd() throws Exception {
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
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("list", bean.save());
        assertFalse(bean.hasErrors());
    }

    public void testEdit() throws Exception {
        log.debug("testing edit...");
        bean.${setIdMethodName}(1L);

        assertEquals("edit", bean.edit());
        assertNotNull(bean.get${pojo.shortName}());
        assertFalse(bean.hasErrors());
    }

    public void testSave() {
        log.debug("testing save...");
        bean.${setIdMethodName}(1L);

        assertEquals("edit", bean.edit());
        assertNotNull(bean.get${pojo.shortName}());
        ${pojo.shortName} ${pojoNameLower} = bean.get${pojo.shortName}();

        // update required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)});
        </#if>
    </#foreach>
</#foreach>
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("edit", bean.save());
        assertFalse(bean.hasErrors());
    }

    public void testRemove() throws Exception {
        log.debug("testing remove...");
        ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        ${pojoNameLower}.${setIdMethodName}(2L);
        bean.set${pojo.shortName}(${pojoNameLower});

        assertEquals("list", bean.delete());
        assertFalse(bean.hasErrors());
    }
}