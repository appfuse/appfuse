package ${basepackage}.webapp.action;

import ${basepackage}.model.${pojo.shortName};
import org.appfuse.webapp.action.BasePageTestCase;

public class ${pojo.shortName}FormTest extends BasePageTestCase {
    private ${pojo.shortName}Form bean;

    protected void setUp() throws Exception {
        super.setUp();
        bean = (${pojo.shortName}Form) getManagedBean("${pojo.shortName.toLowerCase()}Form");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        bean = null;
    }

    public void testAdd() throws Exception {
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
        // set required fields
        ${pojo.shortName.toLowerCase()}.setFirstName("firstName");
        ${pojo.shortName.toLowerCase()}.setLastName("lastName");
        bean.set${pojo.shortName}(${pojo.shortName.toLowerCase()});

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
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = bean.get${pojo.shortName}();

        // update fields
        ${pojo.shortName.toLowerCase()}.setFirstName("firstName");
        ${pojo.shortName.toLowerCase()}.setLastName("lastName");
        bean.set${pojo.shortName}(${pojo.shortName.toLowerCase()});

        assertEquals("edit", bean.save());
        assertFalse(bean.hasErrors());
    }

    public void testRemove() throws Exception {
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
        ${pojo.shortName.toLowerCase()}.setId(2L);
        bean.set${pojo.shortName}(${pojo.shortName.toLowerCase()});

        assertEquals("list", bean.delete());
        assertFalse(bean.hasErrors());
    }
}