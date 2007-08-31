<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
<#assign getIdMethodName = pojo.getGetterSignature(pojo.identifierProperty)>
<#assign setIdMethodName = 'set' + pojo.getPropertyName(pojo.identifierProperty)>
<#assign identifierType = pojo.getJavaTypeName(pojo.identifierProperty, jdk5)>
package ${basepackage}.service.impl;

import java.util.ArrayList;
import java.util.List;

import ${basepackage}.dao.${pojo.shortName}Dao;
import ${basepackage}.model.${pojo.shortName};
import ${appfusepackage}.service.impl.BaseManagerMockTestCase;
import org.jmock.Mock;

public class ${pojo.shortName}ManagerImplTest extends BaseManagerMockTestCase {
    private ${pojo.shortName}ManagerImpl manager = null;
    private Mock dao = null;
    private ${pojo.shortName} ${pojoNameLower} = null;

    protected void setUp() throws Exception {
        dao = new Mock(${pojo.shortName}Dao.class);
        manager = new ${pojo.shortName}ManagerImpl((${pojo.shortName}Dao) dao.proxy());
    }

    protected void tearDown() throws Exception {
        manager = null;
    }

    public void testGet${pojo.shortName}() {
        log.debug("testing get...");

        ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)} ${pojo.identifierProperty.name} = 7L;
        ${pojoNameLower} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("get").with(eq(${pojo.identifierProperty.name})).will(returnValue(${pojoNameLower}));

        ${pojo.shortName} result = manager.get(${pojo.identifierProperty.name});
        assertSame(${pojoNameLower}, result);
    }

    public void testGet${util.getPluralForWord(pojo.shortName)}() {
        log.debug("testing getAll...");

        List ${util.getPluralForWord(pojoNameLower)} = new ArrayList();

        // set expected behavior on dao
        dao.expects(once()).method("getAll").will(returnValue(${util.getPluralForWord(pojoNameLower)}));

        List result = manager.getAll();
        assertSame(${util.getPluralForWord(pojoNameLower)}, result);
    }

    public void testSave${pojo.shortName}() {
        log.debug("testing save...");

        ${pojoNameLower} = new ${pojo.shortName}();
        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(field.value.typeName)}<#rt/>
            <#if field.value.typeName == "java.lang.String" && column.isUnique()><#lt/> + Math.random()</#if><#lt/>);
        </#if>
    </#foreach>
</#foreach>
        
        // set expected behavior on dao
        dao.expects(once()).method("save").with(same(${pojoNameLower})).isVoid();

        ${pojoNameLower} = manager.save(${pojoNameLower});
    }

    public void testRemove${pojo.shortName}() {
        log.debug("testing remove...");

        ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)} ${pojo.identifierProperty.name} = 11L;
        ${pojoNameLower} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("remove").with(eq(${pojo.identifierProperty.name})).isVoid();

        manager.remove(${pojo.identifierProperty.name});
    }
}