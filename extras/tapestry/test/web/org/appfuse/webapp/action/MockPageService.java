package org.appfuse.webapp.action;

import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.engine.PageService;
import org.apache.tapestry.link.StaticLink;

public class MockPageService extends PageService {

    public ILink getLink(boolean post, Object name) {
        return new StaticLink(name + BasePageTestCase.EXTENSION);
    }
}
