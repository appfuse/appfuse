<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.pages;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import ${appfusepackage}.webapp.pages.BasePageTestCase;

public class ${pojo.shortName}ListTest extends BasePageTestCase {
    @Test
    public void temporaryTest() {
        assertTrue(true);
    }

    /*private ${pojo.shortName}List page;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        // these can be mocked if you want a more "pure" unit test
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("${pojoNameLower}Manager", applicationContext.getBean("${pojoNameLower}Manager"));
        page = (${pojo.shortName}List) getPage(${pojo.shortName}List.class, map);
    }

    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        page = null;
    }

    public void testSearch() throws Exception {
        assertTrue(page.get${util.getPluralForWord(pojo.shortName)}().size() >= 1);
    }

    public void testEdit() throws Exception {
        RequestCycle cycle = new MockRequestCycle(this.getClass().getPackage().getName());
        cycle.setListenerParameters(new Object[] {-1L});
        page.edit(cycle);
        assertFalse(page.hasErrors());
    }*/
}