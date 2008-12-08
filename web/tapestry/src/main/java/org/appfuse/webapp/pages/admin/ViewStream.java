package org.appfuse.webapp.pages.admin;

import com.opensymphony.clickstream.Clickstream;
import com.opensymphony.clickstream.ClickstreamRequest;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.slf4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Serge Eby
 * @version $Id: ViewStream.java 5 2008-08-30 09:59:21Z serge.eby $
 */
public class ViewStream {

    private static final String NULL_STRING = "null";

    @Inject
    private Logger logger;

    @Inject
    private Request request;

    @Persist
    @Property
    private Clickstream stream;

    @Inject
    private Context context;

    @Property
    private String sessionid;

    @Property
    private int index;


    void onActivate(String sid) {
        if (sid == null) {
            logger.debug("Null sid in onActivate()");
        }
        sessionid = sid;
    }

    String onPassivate() {
        return sessionid;
    }

    @SuppressWarnings("unchecked")
    void setupRender() {
        Map<String, Clickstream> clickstreams = (Map<String, Clickstream>) context.getAttribute("clickstreams");
        if (clickstreams != null) {
            logger.debug("Found clickstreams object in context");
            stream = clickstreams.get(sessionid);
        } else {
            logger.debug("Null clickstreams object in context");
        }
    }

    public int getNext() {
        return index + 1;
    }

    public String getStreamReferrer() {
        String referrer = stream.getInitialReferrer();
        if (referrer == null) {
            return NULL_STRING;
        }
        return referrer;
    }

    public String getHostname() {
        String hostname = stream.getHostname();
        if (hostname == null) {
            return NULL_STRING;
        }
        return hostname;
    }

    public String getSessionId() {
        return sessionid;
    }

    public Date getStreamStart() {
        return stream.getStart();
    }


    public Date getStreamFinish() {
        return stream.getLastRequest();
    }

    public String getBotStream() {
        return stream != null && stream.isBot() ? "Yes" : "No";
    }

    public int getStreamSize() {
        List list = stream.getStream();
        return stream != null ? stream.getStream().size() : 0;
    }

    public String getSessionLength() {
        long streamLength = stream.getLastRequest().getTime() - stream.getStart().getTime();
        StringBuffer sb = new StringBuffer(" ");
        if (streamLength > 3600000) {
            sb.append(" ").append(streamLength / 3600000).append(" hours");
        }
        if (streamLength > 60000) {
            sb.append(" ").append((streamLength / 60000) % 60).append(" minutes");
        }
        if (streamLength > 1000) {
            sb.append(" ").append((streamLength / 1000) % 60).append(" seconds");
        }

        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public List<ClickstreamRequest> getStreamList() {
        return (List<ClickstreamRequest>) stream.getStream();
    }

}
