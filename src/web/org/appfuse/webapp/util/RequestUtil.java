package org.appfuse.webapp.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.TagUtils;


/**
 * RequestUtil utility class Good ol' copy-n-paste from  <a
 * href="http://www.javaworld.com/javaworld/jw-02-2002/ssl/utilityclass.txt">
 * http://www.javaworld.com/javaworld/jw-02-2002/ssl/utilityclass.txt</a>
 * which is referenced in the following article: <a
 * href="http://www.javaworld.com/javaworld/jw-02-2002/jw-0215-ssl.html">
 * http://www.javaworld.com/javaworld/jw-02-2002/jw-0215-ssl.html</a>
 */
public class RequestUtil {
    private static final String STOWED_REQUEST_ATTRIBS =
        "ssl.redirect.attrib.stowed";
    private static Log log = LogFactory.getLog(RequestUtil.class);

    //private static String ALGORITHM;

    /**
     * Creates query String from request body parameters
     *
     * @param aRequest DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getRequestParameters(HttpServletRequest aRequest) {
        // set the ALGORIGTHM as defined for the application
        //ALGORITHM = (String) aRequest.getAttribute(Constants.ENC_ALGORITHM);
        Map m = aRequest.getParameterMap();

        return createQueryStringFromMap(m, "&").toString();
    }

    /**
     * Builds a query string from a given map of parameters
     *
     * @param m A map of parameters
     * @param ampersand String to use for ampersands (e.g. "&" or "&amp;" )
     *
     * @return query string (with no leading "?")
     */
    public static StringBuffer createQueryStringFromMap(Map m, String ampersand) {
        StringBuffer aReturn = new StringBuffer("");
        Set aEntryS = m.entrySet();
        Iterator aEntryI = aEntryS.iterator();

        while (aEntryI.hasNext()) {
            Map.Entry aEntry = (Map.Entry) aEntryI.next();
            Object o = aEntry.getValue();

            if (o == null) {
                append(aEntry.getKey(), "", aReturn, ampersand);
            } else if (o instanceof String) {
                append(aEntry.getKey(), o, aReturn, ampersand);
            } else if (o instanceof String[]) {
                String[] aValues = (String[]) o;

                for (int i = 0; i < aValues.length; i++) {
                    append(aEntry.getKey(), aValues[i], aReturn, ampersand);
                }
            } else {
                append(aEntry.getKey(), o, aReturn, ampersand);
            }
        }

        return aReturn;
    }

    /**
     * Appends new key and value pair to query string
     *
     * @param key parameter name
     * @param value value of parameter
     * @param queryString existing query string
     * @param ampersand string to use for ampersand (e.g. "&" or "&amp;")
     *
     * @return query string (with no leading "?")
     */
    private static StringBuffer append(Object key, Object value,
                                       StringBuffer queryString,
                                       String ampersand) {
        if (queryString.length() > 0) {
            queryString.append(ampersand);
        }

        TagUtils tagUtils = TagUtils.getInstance();

        // Use encodeURL from Struts' RequestUtils class - it's JDK 1.3 and 1.4 compliant
        queryString.append(tagUtils.encodeURL(key.toString()));
        queryString.append("=");
        queryString.append(tagUtils.encodeURL(value.toString()));

        return queryString;
    }

    /**
     * Stores request attributes in session
     *
     * @param aRequest the current request
     */
    public static void stowRequestAttributes(HttpServletRequest aRequest) {
        if (aRequest.getSession().getAttribute(STOWED_REQUEST_ATTRIBS) != null) {
            return;
        }

        Enumeration e = aRequest.getAttributeNames();
        Map map = new HashMap();

        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            map.put(name, aRequest.getAttribute(name));
        }

        aRequest.getSession().setAttribute(STOWED_REQUEST_ATTRIBS, map);
    }

    /**
     * Returns request attributes from session to request
     *
     * @param aRequest DOCUMENT ME!
     */
    public static void reclaimRequestAttributes(HttpServletRequest aRequest) {
        Map map =
            (Map) aRequest.getSession().getAttribute(STOWED_REQUEST_ATTRIBS);

        if (map == null) {
            return;
        }

        Iterator itr = map.keySet().iterator();

        while (itr.hasNext()) {
            String name = (String) itr.next();
            aRequest.setAttribute(name, map.get(name));
        }

        aRequest.getSession().removeAttribute(STOWED_REQUEST_ATTRIBS);
    }

    /**
     * Convenience method to set a cookie
     *
     * @param response
     * @param name
     * @param value
     * @param path
     * @return HttpServletResponse
     */
    public static void setCookie(HttpServletResponse response, String name,
                                 String value, String path) {
        if (log.isDebugEnabled()) {
            log.debug("Setting cookie '" + name + "' on path '" + path + "'");
        }

        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(false);
        cookie.setPath(path);
        cookie.setMaxAge(3600 * 24 * 30); // 30 days

        response.addCookie(cookie);
    }

    /**
     * Convenience method to get a cookie by name
     *
     * @param request the current request
     * @param name the name of the cookie to find
     *
     * @return the cookie (if found), null if not found
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;

        if (cookies == null) {
            return returnCookie;
        }

        for (int i = 0; i < cookies.length; i++) {
            Cookie thisCookie = cookies[i];

            if (thisCookie.getName().equals(name)) {
                // cookies with no value do me no good!
                if (!thisCookie.getValue().equals("")) {
                    returnCookie = thisCookie;

                    break;
                }
            }
        }

        return returnCookie;
    }

    /**
     * Convenience method for deleting a cookie by name
     *
     * @param response the current web response
     * @param cookie the cookie to delete
     *
     * @return the modified response
     */
    public static void deleteCookie(HttpServletResponse response,
                                    Cookie cookie, String path) {
        if (cookie != null) {
            // Delete the cookie by setting its maximum age to zero
            cookie.setMaxAge(0);
            cookie.setPath(path);
            response.addCookie(cookie);
        }
    }
}
