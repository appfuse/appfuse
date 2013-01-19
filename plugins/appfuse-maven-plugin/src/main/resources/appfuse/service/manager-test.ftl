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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

public class ${pojo.shortName}ManagerImplTest extends BaseManagerMockTestCase {

    @InjectMocks
    private ${pojo.shortName}ManagerImpl manager;

    @Mock
    private ${pojo.shortName}Dao dao;


    @Test
    public void testGet${pojo.shortName}() {
        log.debug("testing get...");
        //given
        final ${identifierType} ${pojo.identifierProperty.name} = 7L;
        final ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        given(dao.get(${pojo.identifierProperty.name})).willReturn(${pojoNameLower});

        //when
        ${pojo.shortName} result = manager.get(${pojo.identifierProperty.name});

        //then
        assertSame(${pojoNameLower}, result);
    }

    @Test
    public void testGet${util.getPluralForWord(pojo.shortName)}() {
        log.debug("testing getAll...");
        //given
        final List ${util.getPluralForWord(pojoNameLower)} = new ArrayList();
        given(dao.getAll()).willReturn(${util.getPluralForWord(pojoNameLower)});

        //when
        List result = manager.getAll();

        //then
        assertSame(${util.getPluralForWord(pojoNameLower)}, result);
    }

    @Test
    public void testSave${pojo.shortName}() {
        log.debug("testing save...");

        //given
        final ${pojo.shortName} ${pojoNameLower} = new ${pojo.shortName}();
        // enter all required fields
<#foreach field in pojo.getAllPropertiesIterator()>
    <#foreach column in field.getColumnIterator()>
        <#if !field.equals(pojo.identifierProperty) && !column.nullable && !c2h.isCollection(field) && !c2h.isManyToOne(field) && !c2j.isComponent(field)>
            <#lt/>        ${pojoNameLower}.set${pojo.getPropertyName(field)}(${data.getValueForJavaTest(column)});
        </#if>
    </#foreach>
</#foreach>
        


        given(dao.save(${pojoNameLower})).willReturn(${pojoNameLower});

        //when
        manager.save(${pojoNameLower});

        //then
        verify(dao).save(${pojoNameLower});
    }

    @Test
    public void testRemove${pojo.shortName}() {
        log.debug("testing remove...");

        //given
        final ${identifierType} ${pojo.identifierProperty.name} = -11L;
        willDoNothing().given(dao).remove(${pojo.identifierProperty.name});

        //when
        manager.remove(${pojo.identifierProperty.name});

        //then
        verify(dao).remove(${pojo.identifierProperty.name});
    }
}