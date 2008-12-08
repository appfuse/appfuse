package org.appfuse.webapp.pages.admin;

import com.opensymphony.clickstream.Clickstream;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks users activity
 *
 * @author Serge Eby
 * @version $Id: ClickStreams.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ClickStreams {

    @Inject
    private Logger logger;

    @Persist(PersistenceConstants.FLASH)
    private String showBots = "false";

    @Inject
    private Request request;

    @Inject
    private Context context;

    @Inject
    private ComponentResources resources;

    @InjectPage
    private ViewStream viewstream;

    @Persist
    @Property
    private Map<String, Clickstream> clickstreams;


    @Persist
    @Property
    private Map<String, Clickstream> activeStreams;

    @Property
    private Map.Entry<String, Clickstream> entry;

    @Property
    private String key;

    private Clickstream stream;


    @Property
    private int index;

    void onActivate(String context) {
        if (context == null) {
            context = "false";
        }
        this.showBots = context;

    }

    String onPassivate() {
        return showBots;
    }

    @SuppressWarnings("unchecked")
    void setupRender() {
        if (clickstreams == null) {
            logger.debug("Initializing clickstreams object from servlet context");
            clickstreams = (Map<String, Clickstream>) context.getAttribute("clickstreams");

        }
        if (clickstreams == null) {
            logger.debug("Clickstreams still null !!!!");
        }


        if ("both".equals(showBots)) {
            activeStreams = clickstreams;
            return;
        }

        // Update streams accordingly
        activeStreams = new HashMap<String, Clickstream>();

        for (Map.Entry<String, Clickstream> entry : clickstreams.entrySet()) {
            String key = entry.getKey();
            Clickstream value = entry.getValue();

            if ("true".equals(showBots) && value.isBot()) {
                activeStreams.put(key, value);
            } else if ("false".equals(showBots) && !value.isBot()) {
                activeStreams.put(key, value);
            }
        }


    }


    public int getNext() {
        return index + 1;
    }


    public String getShowBots() {
        String showBotsParam = request.getParameter("showbots");
        if (showBotsParam != null) {
            if ("true".equals(showBotsParam)) {
                showBots = "true";
            } else if ("both".equals(showBotsParam)) {
                showBots = "both";
            }
        }
        return showBots;
    }

    public boolean isClickStreamEmpty() {
        return (activeStreams == null || activeStreams.keySet().size() == 0);
    }


    public String getStreamHostname() {
        Clickstream value = entry.getValue();
        String hostname = (value.getHostname() != null && !"".equals(value.getHostname()) ?
                value.getHostname() : "Stream");
        return hostname;
    }

    public int getStreamSize() {
        return entry.getValue().getStream().size();
    }


    public Object getAllStreamsLink() {
        return createLink("both");
    }

    public Object getBotStreamsLink() {
        return createLink("true");
    }

    public Object getNoBotStreamsLink() {
        return createLink("false");
    }

    private Object createLink(String flag) {
        return resources.createPageLink(resources.getPageName(), true, flag);
    }

}
