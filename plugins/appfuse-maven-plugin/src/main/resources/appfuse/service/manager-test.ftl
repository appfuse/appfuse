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
        log.debug("testing get${pojo.shortName}");

        ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)} ${pojo.identifierProperty.name} = 7L;
        ${pojoNameLower} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("get").with(eq(${pojo.identifierProperty.name})).will(returnValue(${pojoNameLower}));

        ${pojo.shortName} result = manager.get(${pojo.identifierProperty.name});
        assertSame(${pojoNameLower}, result);
        dao.verify();
    }

    public void testGet${pojo.shortName}s() {
        log.debug("testing get${pojo.shortName}s");

        List ${pojoNameLower}s = new ArrayList();

        // set expected behavior on dao
        dao.expects(once()).method("getAll").will(returnValue(${pojoNameLower}s));

        List result = manager.getAll();
        assertSame(${pojoNameLower}s, result);
        dao.verify();
    }

    public void testSave${pojo.shortName}() {
        log.debug("testing save${pojo.shortName}");

        ${pojoNameLower} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("save").with(same(${pojoNameLower})).isVoid();

        manager.save(${pojoNameLower});
        dao.verify();
    }

    public void testRemove${pojo.shortName}() {
        log.debug("testing remove${pojo.shortName}");

        ${pojo.getJavaTypeName(pojo.identifierProperty, jdk5)} ${pojo.identifierProperty.name} = 11L;
        ${pojoNameLower} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("remove").with(eq(${pojo.identifierProperty.name})).isVoid();

        manager.remove(${pojo.identifierProperty.name});
        dao.verify();
    }
}