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

import org.jmock.Expectations;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class ${pojo.shortName}ManagerImplTest extends BaseManagerMockTestCase {
    private ${pojo.shortName}ManagerImpl manager = null;
    private ${pojo.shortName}Dao dao = null;

    @Before
    public void setUp() {
        dao = context.mock(${pojo.shortName}Dao.class);
        manager = new ${pojo.shortName}ManagerImpl(dao);
    }

    @After
    public void tearDown() {
        manager = null;
    }

    @Test
    public void testGet${pojo.shortName}() {
        log.debug("testing get...");

        final ${identifierType} ${pojo.identifierProperty.name} = 7L;
        final ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(${pojo.identifierProperty.name})));
            will(returnValue(${pojoNameLower}));
        }});

        ${pojo.shortName} result = manager.get(${pojo.identifierProperty.name});
        assertSame(${pojoNameLower}, result);
    }

    @Test
    public void testGet${util.getPluralForWord(pojo.shortName)}() {
        log.debug("testing getAll...");

        final List ${util.getPluralForWord(pojoNameLower)} = new ArrayList();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).getAll();
            will(returnValue(${util.getPluralForWord(pojoNameLower)}));
        }});

        List result = manager.getAll();
        assertSame(${util.getPluralForWord(pojoNameLower)}, result);
    }

    @Test
    public void testSave${pojo.shortName}() {
        log.debug("testing save...");

        final ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>
        
        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).save(with(same(${pojoNameLower})));
        }});

        manager.save(${pojoNameLower});
    }

    @Test
    public void testRemove${pojo.shortName}() {
        log.debug("testing remove...");

        final ${identifierType} ${pojo.identifierProperty.name} = -11L;

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).remove(with(equal(${pojo.identifierProperty.name})));
        }});

        manager.remove(${pojo.identifierProperty.name});
    }
}