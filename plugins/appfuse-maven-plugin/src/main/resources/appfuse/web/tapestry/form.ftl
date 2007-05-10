package ${basepackage}.webapp.pages;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import org.appfuse.service.GenericManager;
import ${basepackage}.model.${pojo.shortName};
import org.appfuse.webapp.pages.BasePage;

public abstract class ${pojo.shortName}Form extends BasePage implements PageBeginRenderListener {
    public abstract GenericManager<${pojo.shortName}, Long> get${pojo.shortName}Manager();
    public abstract void set${pojo.shortName}(${pojo.shortName} ${pojo.shortName.toLowerCase()});
    public abstract ${pojo.shortName} get${pojo.shortName}();

    public void pageBeginRender(PageEvent event) {
        if (get${pojo.shortName}() == null) {
            set${pojo.shortName}(new ${pojo.shortName}());
        }
    }

    public ILink cancel(IRequestCycle cycle) {
        log.debug("Entering 'cancel' method");
        return getEngineService().getLink(false, "${pojo.shortName}List");
    }

    public ILink delete(IRequestCycle cycle) {
        log.debug("entered 'delete' method");

        get${pojo.shortName}Manager().remove(get${pojo.shortName}().getId());

        ${pojo.shortName}List nextPage = (${pojo.shortName}List) cycle.getPage("${pojo.shortName}List");
        nextPage.setMessage(getText("${pojo.shortName.toLowerCase()}.deleted"));
        return getEngineService().getLink(false, nextPage.getPageName());
    }

    public ILink save(IRequestCycle cycle) {
        if (getDelegate().getHasErrors()) {
            return null;
        }

        boolean isNew = (get${pojo.shortName}().getId() == null);

        get${pojo.shortName}Manager().save(get${pojo.shortName}());

        String key = (isNew) ? "${pojo.shortName.toLowerCase()}.added" : "${pojo.shortName.toLowerCase()}.updated";

        if (isNew) {
            ${pojo.shortName}List nextPage = (${pojo.shortName}List) cycle.getPage("${pojo.shortName}List");
            nextPage.setMessage(getText(key));
            return getEngineService().getLink(false, nextPage.getPageName());
        } else {
            setMessage(getText(key));
            return null; // return to current page
        }
    }
}
