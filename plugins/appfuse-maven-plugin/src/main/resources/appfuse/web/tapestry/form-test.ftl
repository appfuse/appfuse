package ${basepackage}.webapp.pages;

import org.apache.tapestry.engine.ILink;
import ${basepackage}.model.${pojo.shortName};
import org.appfuse.webapp.pages.MockRequestCycle;
import org.appfuse.webapp.pages.BasePageTestCase;
import org.appfuse.service.GenericManager;

import java.util.HashMap;
import java.util.Map;

public class ${pojo.shortName}FormTest extends BasePageTestCase {
    private ${pojo.shortName}Form page;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        // these can be mocked if you want a more "pure" unit test
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("${pojo.shortName.toLowerCase()}Manager", applicationContext.getBean("${pojo.shortName.toLowerCase()}Manager"));
        page = (${pojo.shortName}Form) getPage(${pojo.shortName}Form.class, map);
    }

    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        page = null;
    }

    public void testAdd() throws Exception {
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
        // update the object's fields
        ${pojo.shortName.toLowerCase()}.setFirstName("Abbie");
        ${pojo.shortName.toLowerCase()}.setLastName("Loo");
        page.set${pojo.shortName}(${pojo.shortName.toLowerCase()});

        ILink link = page.save(new MockRequestCycle(this.getClass().getPackage().getName()));
        assertNotNull(page.get${pojo.shortName}());
        assertFalse(page.hasErrors());
        assertEquals("${pojo.shortName}List" + EXTENSION, link.getURL());
    }

    public void testSave() {
        GenericManager<${pojo.shortName}, Long> ${pojo.shortName.toLowerCase()}Manager =
                (GenericManager<${pojo.shortName}, Long>) applicationContext.getBean("${pojo.shortName.toLowerCase()}Manager");
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = ${pojo.shortName.toLowerCase()}Manager.get(1L);

        // update fields
        ${pojo.shortName.toLowerCase()}.setFirstName("Jack");
        ${pojo.shortName.toLowerCase()}.setLastName("Jack");
        page.set${pojo.shortName}(${pojo.shortName.toLowerCase()});

        ILink link = page.save(new MockRequestCycle(this.getClass().getPackage().getName()));
        assertNotNull(page.get${pojo.shortName}());
        assertFalse(page.hasErrors());
        assertNull(link);
    }

    public void testRemove() throws Exception {
        ${pojo.shortName} ${pojo.shortName.toLowerCase()} = new ${pojo.shortName}();
        ${pojo.shortName.toLowerCase()}.setId(2L);
        page.set${pojo.shortName}(${pojo.shortName.toLowerCase()});
        page.delete(new MockRequestCycle(this.getClass().getPackage().getName()));
        assertFalse(page.hasErrors());
    }
}
