package org.appfuse.webapp.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.appfuse.Constants;


/**
 * SslUtil utility class Good ol' copy-n-paste from  <a
 * href="http://www.javaworld.com/javaworld/jw-02-2002/ssl/utilityclass.txt">
 * http://www.javaworld.com/javaworld/jw-02-2002/ssl/utilityclass.txt</a>
 * which is referenced in the following article: <a
 * href="http://www.javaworld.com/javaworld/jw-02-2002/jw-0215-ssl.html">
 * http://www.javaworld.com/javaworld/jw-02-2002/jw-0215-ssl.html</a>
 */
public class SslUtil {
    //~ Static fields/initializers =============================================

    public static final String HTTP = "http";
    public static final String HTTPS = "https";
    public static final String HTTP_PORT_PARAM = "listenPort_http";
    public static final String HTTPS_PORT_PARAM = "listenPort_https";
    private static String HTTP_PORT = null;
    private static String HTTPS_PORT = null;
    public static final String STD_HTTP_PORT = "80";
    public static final String STD_HTTPS_PORT = "443";

    //~ Methods ================================================================

    public static String getRedirectString(HttpServletRequest request,
                                           ServletContext ctx, boolean isSecure) {
        // get the port numbers from the application context
        Map config = (HashMap) ctx.getAttribute(Constants.CONFIG);
        HTTP_PORT = (String) config.get(Constants.HTTP_PORT);
        HTTPS_PORT = (String) config.get(Constants.HTTPS_PORT);

        // get the scheme we want to use for this page and
        // get the scheme used in this request
        String desiredScheme = isSecure ? HTTPS : HTTP;
        String usingScheme = request.getScheme();

        // Determine the port number we want to use
        // and the port number we used in this request
        String desiredPort = isSecure ? HTTPS_PORT : HTTP_PORT;
        String usingPort = String.valueOf(request.getServerPort());

        String urlString = null;

        // Must also check ports, because of IE multiple redirect problem
        if (!desiredScheme.equals(usingScheme) ||
                !desiredPort.equals(usingPort)) {
            urlString =
                buildNewUrlString(request, desiredScheme, usingScheme,
                                  desiredPort, usingPort);

            // Temporarily store attributes in session
            RequestUtil.stowRequestAttributes(request);
        } else {
            // Retrieve attributes from session
            RequestUtil.reclaimRequestAttributes(request);
        }

        return urlString;
    }

    /**
     * Builds the URL that we will redirect to
     *
     * @param request DOCUMENT ME!
     * @param desiredScheme DOCUMENT ME!
     * @param usingScheme DOCUMENT ME!
     * @param desiredPort DOCUMENT ME!
     * @param usingPort DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String buildNewUrlString(HttpServletRequest request,
                                            String desiredScheme,
                                            String usingScheme,
                                            String desiredPort, String usingPort) {
        StringBuffer url = request.getRequestURL();

        url.replace(0, usingScheme.length(), desiredScheme);

        // Find the port used within the URL string
        int startIndex = url.toString().indexOf(usingPort);

        if (startIndex == -1) { // Port not found in URL

            if ((!(STD_HTTPS_PORT.equals(desiredPort) &&
                    HTTPS.equals(desiredScheme))) &&
                    (!(STD_HTTP_PORT.equals(desiredPort) &&
                    HTTP.equals(desiredScheme)))) {
                startIndex =
                    url.toString().indexOf("/",
                                           url.toString().indexOf("/",
                                                                  url.toString()
                                                                     .indexOf("/") +
                                                                  1) + 1);
                url.insert(startIndex, ":" + desiredPort);
            }
        } else { // Port found in URL

            if ((STD_HTTPS_PORT.equals(desiredPort) &&
                    HTTPS.equals(desiredScheme)) ||
                    (STD_HTTP_PORT.equals(desiredPort) &&
                    HTTP.equals(desiredScheme))) {
                url.delete(startIndex - 1, startIndex + usingPort.length());
            } else { // desired port is not a default port

                // Replace requested port with desired port number in URL string
                url.replace(startIndex, startIndex + usingPort.length(),
                            desiredPort);
            }
        }

        // add query string, if any
        String queryString = request.getQueryString();

        if ((queryString != null) && (queryString.length() != 0)) {
            url.append("?" + queryString);
        } else {
            queryString = RequestUtil.getRequestParameters(request);

            if ((queryString != null) && (queryString.length() != 0)) {
                url.append("?" + queryString);
            }
        }

        return url.toString();
    }
}
