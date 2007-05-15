package ${basepackage}.webapp.pages;

import java.util.HashMap;
import java.util.Map;

import ${appfusepackage}.webapp.pages.BasePageTestCase;
import ${appfusepackage}.webapp.pages.MockRequestCycle;
import org.apache.tapestry.engine.RequestCycle;

public class ${pojo.shortName}ListTest extends BasePageTestCase {
    private ${pojo.shortName}List page;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        // these can be mocked if you want a more "pure" unit test
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("${pojo.shortName.toLowerCase()}Manager", applicationContext.getBean("${pojo.shortName.toLowerCase()}Manager"));
        page = (${pojo.shortName}List) getPage(${pojo.shortName}List.class, map);
    }

    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        page = null;
    }

    public void testSearch() throws Exception {
        assertTrue(page.get${pojo.shortName}s().size() >= 1);
    }

    public void testEdit() throws Exception {
        RequestCycle cycle = new MockRequestCycle(this.getClass().getPackage().getName());
        cycle.setServiceParameters(new Object[] {1L});
        page.edit(cycle);
        assertFalse(page.hasErrors());
    }
}