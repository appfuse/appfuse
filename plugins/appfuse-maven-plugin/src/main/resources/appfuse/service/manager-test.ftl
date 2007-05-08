package ${basepackage}.service.impl;

import java.util.ArrayList;
import java.util.List;

import ${basepackage}.dao.${pojo.shortName}Dao;
import ${basepackage}.model.${pojo.shortName};
import org.appfuse.service.impl.BaseManagerMockTestCase;
import org.jmock.Mock;

public class ${pojo.shortName}ManagerImplTest extends BaseManagerMockTestCase {
    private ${pojo.shortName}ManagerImpl manager = null;
    private Mock dao = null;
    private ${pojo.shortName} ${pojo.shortName.toLowerCase()} = null;

    protected void setUp() throws Exception {
        dao = new Mock(${pojo.shortName}Dao.class);
        manager = new ${pojo.shortName}ManagerImpl((${pojo.shortName}Dao) dao.proxy());
    }

    protected void tearDown() throws Exception {
        manager = null;
    }

    public void testGet${pojo.shortName}() {
        log.debug("testing get${pojo.shortName}");

        Long id = 7L;
        ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("get").with(eq(id)).will(returnValue(${pojo.shortName.toLowerCase()}));

        ${pojo.shortName} result = manager.get(id);
        assertSame(${pojo.shortName.toLowerCase()}, result);
        dao.verify();
    }

    public void testGet${pojo.shortName}s() {
        log.debug("testing get${pojo.shortName}s");

        List ${pojo.shortName.toLowerCase()}s = new ArrayList();

        // set expected behavior on dao
        dao.expects(once()).method("getAll").will(returnValue(${pojo.shortName.toLowerCase()}s));

        List result = manager.getAll();
        assertSame(${pojo.shortName.toLowerCase()}s, result);
        dao.verify();
    }

    public void testSave${pojo.shortName}() {
        log.debug("testing save${pojo.shortName}");

        ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("save").with(same(${pojo.shortName.toLowerCase()})).isVoid();

        manager.save(${pojo.shortName.toLowerCase()});
        dao.verify();
    }

    public void testRemove${pojo.shortName}() {
        log.debug("testing remove${pojo.shortName}");

        Long id = 11L;
        ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();

        // set expected behavior on dao
        dao.expects(once()).method("remove").with(eq(id)).isVoid();

        manager.remove(id);
        dao.verify();
    }
}