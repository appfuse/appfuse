<#assign pojoNameLower = pojo.shortName.substring(0,1).toLowerCase()+pojo.shortName.substring(1)>
package ${basepackage}.webapp.action;

import net.sourceforge.stripes.mock.MockRoundtrip;
import net.sourceforge.stripes.mock.MockServletContext;
import org.junit.After;
import org.junit.Test;

import javax.servlet.Filter;

import static org.junit.Assert.*;

public class ${pojo.shortName}ListBeanTest {
    private MockServletContext servletContext;

    @Test
    public void testExecute() throws Exception {
        servletContext = new StripesTestFixture().getServletContext();

        MockRoundtrip trip = new MockRoundtrip(servletContext, ${pojo.shortName}ListBean.class);
        trip.execute();

        ${pojo.shortName}ListBean bean = trip.getActionBean(${pojo.shortName}ListBean.class);
        assertTrue(bean.get${util.getPluralForWord(pojo.shortName)}().size() > 0);
    }

    @After
    public void onTearDown() {
        // http://www.stripesframework.org/jira/browse/STS-714
        for (Filter filter : servletContext.getFilters()) {
            filter.destroy();
        }
    }
}
