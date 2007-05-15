package ${basepackage}.webapp.action;

import ${appfusepackage}.webapp.action.BasePageTestCase;
import ${appfusepackage}.service.GenericManager;
import ${basepackage}.model.${pojo.shortName};

public class ${pojo.shortName}ListTest extends BasePageTestCase {
    private ${pojo.shortName}List bean;

    protected void setUp() throws Exception {
        super.setUp();
        bean = (${pojo.shortName}List) getManagedBean("${pojo.shortName.toLowerCase()}List");
        GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager =
                (GenericManager<${pojo.shortName}, Long>) applicationContext.getBean("${pojo.shortName.toLowerCase()}Manager");

        // add a test ${pojo.shortName.toLowerCase()} to the database
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
        ${pojo.shortName.toLowerCase()}.setFirstName("Abbie Loo");
        ${pojo.shortName.toLowerCase()}.setLastName("Raible");
        ${pojo.shortName.toLowerCase()}Manager.save(${pojo.shortName.toLowerCase()});
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